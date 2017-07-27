package cristina.tech;


import cristina.tech.worker.domain.Brand;
import cristina.tech.worker.domain.Dress;
import cristina.tech.worker.domain.DressStatus;
import cristina.tech.worker.event.DressEvent;
import cristina.tech.worker.event.DressEventType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.support.MessageBuilder;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;


/**
 * Start class or Spring Boot Runnable JAR for Event-driven Stream Processing.
 * <p></p>Typically, a streaming data pipeline includes consuming
 * events from external systems, data processing, and polyglot persistence.</p>
 * <p>These phases are commonly referred to as Source, Processor, and Sink in Spring Cloud
 * Stream terminology. </p>
 * <p>When using @EnableBinding(Source.class), Spring Cloud Stream automatically
 * creates a message channel with the name output which is used by the @InboundChannelAdapter.
 * Also see: spring.cloud.stream.bindings.output.destination in application.yml
 * </p>
 *
 * The application has only a sink (consumer) and the source (producer) is the Python
 * script, see <code>/src/main/resources/producer.py</code> and is run a separate
 * process.
 */
@SpringBootApplication
public class FancyDressApplication {

    public static void main(String[] args) {
        SpringApplication.run(FancyDressApplication.class, args);
    }
}
