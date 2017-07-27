package cristina.tech.worker.event;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import cristina.tech.worker.domain.Rating;

public class DressRatedEvent extends DressEvent {

    private static final long serialVersionUID = 1126074635410771214L;

    @JsonUnwrapped
    private Rating payload;

    public DressRatedEvent(String payloadKey, Rating payload, Long timestamp) {
        super(DressEventType.RATED, payloadKey, timestamp);
        this.payload = payload;
    }

}
