package cristina.tech.fancydress.worker.event;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * Interface declares input message channels for AMQP integration.
 * Spring Cloud Stream provides the interfaces Source, Sink, and Processor; you can also define your own interfaces.
 * Spring Cloud Stream will create an implementation of the interface for you.
 * You can use this in the application by autowiring it, as in the following example of a test case.
 */
public interface DressInboundChannels  {
    String INBOUND_DRESSES = "idresses";
    String INBOUND_RATINGS = "iratings";

    /**
     * The @Input annotation identifies an input channel, through which received messages enter the application.
     * See  application.yml file configuration key: spring.stream:bindings:idresses:destination
     */
    @Input(INBOUND_DRESSES)
    SubscribableChannel idresses();

    /**
     * The @Input annotation identifies an input channel, through which received messages enter the application.
     * See  application.yml file configuration key: spring.stream:bindings:iratings:destination
     */
    @Input(INBOUND_RATINGS)
    SubscribableChannel iratings();
}
