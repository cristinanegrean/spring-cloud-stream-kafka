package cristina.tech.worker;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface DressesTrade {
    String INBOUND_DRESSES = "idresses";
    String INBOUND_RATINGS = "iratings";

    @Input(INBOUND_DRESSES)
    SubscribableChannel idresses();

    @Input(INBOUND_RATINGS)
    SubscribableChannel iratings();
}
