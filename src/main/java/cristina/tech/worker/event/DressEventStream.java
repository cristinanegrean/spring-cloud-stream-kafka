package cristina.tech.worker.event;


import cristina.tech.FancyDressApplication;
import cristina.tech.web.DressRepository;
import cristina.tech.worker.DressesTrade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Profile;

/**
 * Consuming events. Sink: consumes from a Python script Source {@link /src/main/resources/producer.py}
 * When using @EnableBinding(DressesTrade.class) Spring Cloud Stream automatically creates multi input message channels:
 * idresses and iratings, see {@link DressesTrade}, which will be used by the @StreamListener.
 * Also see: spring.cloud.stream.bindings.idresses and spring.cloud.stream.bindings.iratings configurations
 * in application.yml.
 */
@EnableAutoConfiguration
@EnableBinding(DressesTrade.class)
@Profile({"development", "docker"})
public class DressEventStream {

    @Autowired private DressEventService dressEventService;

    private static Logger logger = LoggerFactory.getLogger(DressEventStream.class);
    private static final String LOG_RECEIVED = "Received: ";

    @StreamListener(target = DressesTrade.INBOUND_DRESSES)
    public void receiveDressMessageEvent(DressMessageEvent dressMessageEvent) {
        logger.info(LOG_RECEIVED + dressMessageEvent.toString());
        dressEventService.apply(dressMessageEvent);
    }

    @StreamListener(target = DressesTrade.INBOUND_RATINGS)
    public void receiveRatingMessageEvent(RatingMessageEvent ratingMessageEvent) {
        logger.info(LOG_RECEIVED + ratingMessageEvent.toString());
    }

}
