import os
import sys
import json
import pickle
import time
import random

import click
import numpy as np

from random import shuffle
from uuid import uuid4
from enum import Enum

from kafka import KafkaProducer

with open('data/dresses.p', 'rb') as pickle_file:
    dresses = pickle.load(pickle_file)

STATUS_CREATED = 'CREATED'
STATUS_UPDATED = 'UPDATED'

DRESSES_TOPIC = 'dresses'
RATINGS_TOPIC = 'ratings'

CREAT_BURST_COUNT = 500

def auto_str(cls):
    def __str__(self):
        return '%s(%s)' % (
            type(self).__name__,
            ', '.join('%s=%s' % item for item in vars(self).items())
        )
    cls.__str__ = __str__
    return cls

class DressState(Enum):
    STARTUP = 1
    RUNNING = 2

@auto_str
class AppState():
    def __init__(self):
        self.next_creations = None
        self.dress_phase = None
        self.update_candidates = None
        self.ticks_to_next_creation = 0
        self.ticks_to_next_update = 0

def enveloped_message(status, key, payload):
    return json.dumps({
        'status': status,
        'payload_key': key,
        'payload': payload,
        'timestamp': int(time.time() * 1000)
    }).encode('utf-8')

def dress_sender(producer, app_state):
    return {
        DressState.STARTUP: startup_dress_sender,
        DressState.RUNNING: long_running_dress_sender
    }[app_state.dress_phase](producer, app_state)

def create_dress(producer, app_state):
    if app_state.next_creations:
        dress_index = app_state.next_creations.pop()
        app_state.update_candidates.append(dress_index)

        dress = dresses[dress_index]

        producer.send(
            DRESSES_TOPIC,
            key=dress['id'].encode('utf-8'),
            value=enveloped_message(STATUS_CREATED, dress['id'], dress)
            ).get(timeout=60)

        click.echo("Producer created dress with ID '%s'" % dress['id'])

    return app_state

def update_dress(producer, app_state):
    if app_state.update_candidates:
        dress_index = random.randint(0, len(app_state.update_candidates) - 1)
        dress = dresses[dress_index]

        new_price = int(
            dress['price'] *
            (1 + np.random.normal(.04, .01) * [-1,1][np.random.binomial(1,.5)]) *
            100
            ) / 100
        dress['price'] = new_price

        producer.send(
            DRESSES_TOPIC,
            key=dress['id'].encode('utf-8'),
            value=enveloped_message(STATUS_UPDATED, dress['id'], dress)
            ).get(timeout=60)

        click.echo("Producer updated dress with ID '%s'" % dress['id'])

    return app_state

def startup_dress_sender(producer, app_state):
    app_state = create_dress(producer, app_state)

    if len(app_state.update_candidates) >= CREAT_BURST_COUNT:
        app_state.dress_phase = DressState.RUNNING

    return app_state

def long_running_dress_sender(producer, app_state):
    app_state.ticks_to_next_creation -= 1

    if app_state.ticks_to_next_creation <= 0:
        app_state = create_dress(producer, app_state)
        app_state.ticks_to_next_creation = max(0, int(36000 / len(app_state.next_creations) + np.random.normal(0, 3)))

    app_state.ticks_to_next_update -= 1
    if app_state.ticks_to_next_update <= 0:
        app_state = update_dress(producer, app_state)
        app_state.ticks_to_next_update = max(0, int(36000 / len(app_state.update_candidates) + np.random.normal(0, 3)))

    return app_state

def send_rating(producer, dress):
    rating_id = str(uuid4())
    stars = max(1, min(5, int(np.random.normal(3.5, 1.0))))
    rating = enveloped_message(STATUS_CREATED, rating_id, {
        'rating_id': rating_id,
        'dress_id': dress['id'],
        'stars': stars
        })
    producer.send(
        RATINGS_TOPIC,
        key=rating_id.encode('utf-8'),
        value=rating
        ).get(timeout=60)
    click.echo("Rated dress with ID '%s' %d stars." % (dress['id'], stars))

def rating_sender(producer, app_state):
    rated_update_candidate_index = min(np.random.zipf(1.9) - 1, len(app_state.update_candidates) - 1)
    send_rating(producer, dresses[app_state.update_candidates[rated_update_candidate_index]])

    left_update_candidate_index = min(np.random.zipf(1.9) - 1, len(app_state.update_candidates) - 1)
    right_update_candidate_index = min(np.random.zipf(1.9) - 1, len(app_state.update_candidates) - 1)

    tmp = app_state.update_candidates[left_update_candidate_index]
    app_state.update_candidates[left_update_candidate_index] = app_state.update_candidates[right_update_candidate_index]
    app_state.update_candidates[right_update_candidate_index] = tmp

    return app_state

def prepare_dress_phase(app_state):
    if not app_state.next_creations:
        indexes = list(range(len(dresses)))
        shuffle(indexes)
        app_state.next_creations = indexes
        app_state.update_candidates = []

    if not app_state.dress_phase:
        app_state.dress_phase = DressState.STARTUP

    return app_state

def main():
    app_state_dir = sys.argv[1] if len(sys.argv) > 1 else '/tmp'
    app_state_filename = os.path.join(app_state_dir, 'app_state.p')
    click.echo("Attempting to write producer state to disk path '%s'..." % app_state_filename)
    if os.path.exists(app_state_filename):
        with open(app_state_filename, 'rb') as app_state_file:
            app_state = pickle.load(app_state_file)
    else:
        app_state = AppState()

    app_state = prepare_dress_phase(app_state)

    kafka_host_port = os.environ.get('KAFKA_HOST_PORT', 'localhost:9092')
    connect_attempts_remainig = 20
    click.echo("Attempting to connect to Kafka on '%s'..." % kafka_host_port)
    while connect_attempts_remainig >= 0:
        try:
            producer = KafkaProducer(bootstrap_servers=kafka_host_port)
            break
        except Exception as e:
            connect_attempts_remainig -= 1
            click.echo("Failed to connect to Kafka with error '%s'. Trying %d more times." % (e, connect_attempts_remainig))
            if connect_attempts_remainig == 0:
                click.echo("Could not connect to Kafka. Giving up.")
                raise e
            time.sleep(2)

    click.echo("Starting producer.")
    while True:
        app_state = dress_sender(producer, app_state)
        app_state = rating_sender(producer, app_state)

        time.sleep(.1)

        with open(app_state_filename, 'wb') as app_state_file:
            pickle.dump(app_state, app_state_file)


if __name__ == '__main__':
    main()
