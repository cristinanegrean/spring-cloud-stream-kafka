package cristina.tech.worker.event;

import cristina.tech.worker.domain.Dress;
import cristina.tech.worker.domain.DressStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor(force = true)
@Data
@EqualsAndHashCode(callSuper=true)
public class DressCreatedEvent extends DressEvent {
    private static final long serialVersionUID = 1126074635410771217L;

    private Dress payload;

    public DressCreatedEvent(String payloadKey, Dress payload, Long timestamp) {
        super(DressEventType.CREATED, payloadKey, timestamp);
        this.payload = payload;
        this.payload.setStatus(DressStatus.CREATED);
    }
}
