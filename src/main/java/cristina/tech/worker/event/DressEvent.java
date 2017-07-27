package cristina.tech.worker.event;

import cristina.tech.worker.domain.Dress;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * The domain event {@link DressEvent} tracks the type and state of events as
 * applied to the {@link cristina.tech.worker.domain.Dress} domain object. This event resource can be used
 * to event source the aggregate state of {@link cristina.tech.worker.domain.Dress}.
 * <p>
 * This event resource also provides a transaction log that can be used to append
 * actions to the event.
 */
@Getter
@NoArgsConstructor(force = true)
@ToString
public class DressEvent implements Serializable {

    private static final long serialVersionUID = 1126074635410771217L;

    private DressEventType status;
    private String payloadKey;
    private Dress payload;
    private long timestamp;

    public DressEvent(DressEventType status, String payloadKey, Dress payload, long timestamp) {
        this.status = status;
        this.payloadKey = payloadKey;
        this.payload = payload;
        this.timestamp = timestamp;
    }

}
