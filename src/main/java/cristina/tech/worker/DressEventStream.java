package cristina.tech.worker;


import cristina.tech.FancyDressApplication;
import cristina.tech.worker.event.DressCreatedEvent;
import cristina.tech.worker.event.DressEvent;
import cristina.tech.worker.event.DressRatedEvent;
import cristina.tech.worker.event.DressUpdatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Profile;

/**
 * Consuming events. Sink: consumes from a Source {@link FancyDressApplication}
 * When using @EnableBinding(Sink.class) Spring Cloud Stream automatically creates a message channel with the name
 * input which is used by the @StreamListener. Also see: spring.cloud.stream.bindings.input.destination config in
 * application.yml.
 */
@EnableAutoConfiguration
@EnableBinding(DressesTrade.class)
@Profile({"development", "docker", "test"})
@Slf4j
public class DressEventStream {

    @StreamListener(
            target = DressesTrade.INBOUND_DRESSES,
            condition = "headers['status']=='CREATED'")
    public void receiveDressCreatedEvent(DressCreatedEvent dressEvent) {
        log.info("Received: " + dressEvent.toString());
    }

    @StreamListener(
            target = DressesTrade.INBOUND_DRESSES,
            condition = "headers['status']=='UPDATED'")
    public void receiveDressUpdatedEvent(DressUpdatedEvent dressEvent) {
        log.info("Received: " + dressEvent.toString());
    }

    @StreamListener(target = DressesTrade.INBOUND_RATINGS)
    public void receiveDressRateEvent(DressRatedEvent dressEvent) {
        log.info("Received: " + dressEvent.toString());
    }

}
