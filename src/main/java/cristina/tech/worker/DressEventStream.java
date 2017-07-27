package cristina.tech.worker;


import cristina.tech.FancyDressApplication;
import cristina.tech.worker.event.DressCreatedEvent;
import cristina.tech.worker.event.DressRatedEvent;
import cristina.tech.worker.event.DressUpdatedEvent;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Profile({"development", "docker"})
@Data
public class DressEventStream {

    private static Logger logger = LoggerFactory.getLogger(DressEventStream.class);

    @StreamListener(
            target = DressesTrade.INBOUND_DRESSES,
            condition = "headers['status']=='CREATED'")
    public void receiveDressCreatedEvent(DressCreatedEvent dressCreatedEvent) {
        logger.info("Received: " + dressCreatedEvent.toString());
    }

    @StreamListener(
            target = DressesTrade.INBOUND_DRESSES,
            condition = "headers['status']=='UPDATED'")
    public void receiveDressUpdatedEvent(DressUpdatedEvent dressUpdatedEvent) {
        logger.info("Received: " + dressUpdatedEvent.toString());
    }

    @StreamListener(target = DressesTrade.INBOUND_RATINGS)
    public void receiveDressRateEvent(DressRatedEvent dressRatedEvent) {
        logger.info("Received: " + dressRatedEvent.toString());
    }

}
