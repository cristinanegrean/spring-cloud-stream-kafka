#!/bin/sh

set -e

export REPO=cnegrean/springcloudstream-kafka-consumer-base-image
export VERSION="ibm-semeru-runtimes_jdk-17.0.2.8_openj9-ubuntu-focal"
export TAG="${REPO}:${VERSION}"

docker build -t ${TAG} .
docker push ${TAG}