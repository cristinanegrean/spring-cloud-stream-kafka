package cristina.tech;


import cristina.tech.domain.Brand;
import cristina.tech.domain.Dress;
import cristina.tech.domain.DressStatus;
import cristina.tech.domain.Image;
import cristina.tech.event.DressEvent;
import cristina.tech.event.DressEventType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.support.MessageBuilder;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;


/**
 * Event-driven Stream Processing. Typically, a streaming data pipeline includes consuming events from external
 * systems, data processing, and polyglot persistence.
 * These phases are commonly referred to as Source, Processor, and Sink in Spring Cloud Stream terminology.
 * When using @EnableBinding(Source.class), Spring Cloud Stream automatically creates a message channel with the
 * name output which is used by the @InboundChannelAdapter.
 * Also see: spring.cloud.stream.bindings.output.destination in application.yml
 */
@SpringBootApplication
@EnableBinding(Source.class)
public class DressWorkerApplication {

    private static final String ID = "AX821CA1M-Q11";
    private static final String NAME = "Jersey dress - black";
    private static final String COLOR = "Black";
    private static final String SEASON = "WINTER";
    private static final String BRAND_LOGO_URL = "https://i3.ztat.net/brand/9b3cabce-c405-44d7-a62f-ee00d5245962.jpg";
    private static final String BRAND_NAME = "Anna Field Curvy";
    private static final BigDecimal PRICE = new BigDecimal(24.04);

    public static void main(String[] args) {
        SpringApplication.run(DressWorkerApplication.class, args);
    }

    /**
     * Produce one event every 10 seconds with a Poller.
     */
    @Bean
    @InboundChannelAdapter(value = Source.OUTPUT, poller = @Poller(fixedDelay = "10000", maxMessagesPerPoll = "1"))
    public MessageSource<DressEvent> timerMessageSource() {
        return () -> MessageBuilder.withPayload(
                new DressEvent(DressEventType.CREATED, ID, getDress(),
                        Clock.systemDefaultZone().millis())).build();
    }

    private static final Dress getDress() {
        return new Dress(ID, NAME, COLOR, SEASON, null,
                new Brand(BRAND_LOGO_URL, BRAND_NAME),
                DressStatus.CREATED, PRICE);
    }

}
