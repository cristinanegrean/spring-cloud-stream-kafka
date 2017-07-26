package cristina.tech;


import cristina.tech.event.DressEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Profile;


/**
 * Consuming events. Sink: consumes from a Source {@link DressWorkerApplication}
 * When using @EnableBinding(Sink.class) Spring Cloud Stream automatically creates a message channel with the name
 * input which is used by the @StreamListener. Also see: spring.cloud.stream.bindings.input.destination config in
 * application.yml.
 */
@EnableAutoConfiguration
@EnableBinding(Sink.class)
@Profile({ "development", "docker" })
public class DressEventStream {
    private static Logger logger = LoggerFactory.getLogger(DressEventStream.class);

    @StreamListener(Sink.INPUT)
    public void loggerSink(DressEvent dressEvent) {
        logger.info("Received: " + dressEvent.toString());
    }

}
