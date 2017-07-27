package cristina.tech.worker.event;

import cristina.tech.worker.domain.Dress;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(force = true)
@ToString
public class DressCreatedEvent extends DressEvent {
    private static final long serialVersionUID = 1126074635410771217L;

    public DressCreatedEvent(String payloadKey, Dress payload, long timestamp) {
        super(DressEventType.CREATED, payloadKey, payload, timestamp);
    }
}
