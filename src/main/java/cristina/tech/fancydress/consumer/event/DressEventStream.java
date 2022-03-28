package cristina.tech.fancydress.consumer.event;

import cristina.tech.fancydress.consumer.event.ConsumerEventTypes.DressMessageEvent;
import cristina.tech.fancydress.consumer.event.ConsumerEventTypes.RatingMessageEvent;
import cristina.tech.fancydress.store.service.DressEventStoreService;
import cristina.tech.fancydress.store.service.RatingEventStoreService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Profile;


/**
 * Consuming events. Sink: consumes from a Python script Source {@link /src/main/resources/producer.py}
 * When using @EnableBinding(DressInboundChannels.class) Spring Cloud Stream automatically creates multi input
 * message channels: idresses and iratings, which will be used by the @StreamListener.
 * Also see: spring.cloud.stream.bindings.idresses and spring.cloud.stream.bindings.iratings configurations
 * in application.yml.
 */
@EnableBinding(DressInboundChannels.class)
@Profile({"development", "docker", "test"})
public class DressEventStream {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DressEventStoreService dressEventStoreService;

    @Autowired
    private RatingEventStoreService ratingEventStoreService;

    private static final String LOG_RECEIVED = "Received: ";

    @StreamListener(target = DressInboundChannels.INBOUND_DRESSES)
    public void receiveDressMessageEvent(DressMessageEvent dressMessageEvent) {
        logger.info(LOG_RECEIVED + dressMessageEvent);
        dressEventStoreService.apply(dressMessageEvent);
    }

    @StreamListener(target = DressInboundChannels.INBOUND_RATINGS)
    public void receiveRatingMessageEvent(RatingMessageEvent ratingMessageEvent) {
        logger.info(LOG_RECEIVED + ratingMessageEvent);
        ratingEventStoreService.apply(ratingMessageEvent);
    }

}
