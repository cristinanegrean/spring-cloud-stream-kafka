package cristina.tech;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.support.MessageBuilder;

import java.util.Date;


/**
 * Event-driven Stream Processing. Typically, a streaming data pipeline includes consuming events from external systems, data processing, and polyglot persistence.
 * These phases are commonly referred to as Source, Processor, and Sink in Spring Cloud Stream terminology.
 * When using @EnableBinding(Source.class), Spring Cloud Stream automatically creates a message channel with the name output which is used by the @InboundChannelAdapter.
 */
@SpringBootApplication
@EnableBinding(Source.class)
public class EdmpSourceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EdmpSourceApplication.class, args);
	}

    /** Produce one event every 10 seconds with a Poller. */
    @Bean
    @InboundChannelAdapter(value = Source.OUTPUT, poller = @Poller(fixedDelay = "10000", maxMessagesPerPoll = "1"))
    public MessageSource<TimedMessage> timerMessageSource() {
        return () -> MessageBuilder.withPayload(new TimedMessage(new Date().getTime()+"", "Kafka")).build();
    }

    private static class TimedMessage {

        private String time;
        private String message;

        public TimedMessage(String time, String message) {
            this.time = time;
            this.message = message;
        }

        public String getTime() {
            return time;
        }

        public String getMessage() {
            return this.message;
        }
    }

}
