package cristina.tech.fancydress.worker.event;

import cristina.tech.fancydress.store.service.DressEventStoreService;
import cristina.tech.fancydress.store.service.RatingEventStoreService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class DressEventStream {

    @Autowired
    private DressEventStoreService dressEventStoreService;

    @Autowired
    private RatingEventStoreService ratingEventStoreService;

    private static final String LOG_RECEIVED = "Received: ";

    @StreamListener(target = DressInboundChannels.INBOUND_DRESSES)
    public void receiveDressMessageEvent(DressMessageEvent dressMessageEvent) {
        log.info(LOG_RECEIVED + dressMessageEvent.toString());
        dressEventStoreService.apply(dressMessageEvent);
    }

    @StreamListener(target = DressInboundChannels.INBOUND_RATINGS)
    public void receiveRatingMessageEvent(RatingMessageEvent ratingMessageEvent) {
        log.info(LOG_RECEIVED + ratingMessageEvent.toString());
        ratingEventStoreService.apply(ratingMessageEvent);
    }

}
