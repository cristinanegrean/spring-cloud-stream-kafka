package cristina.tech.worker.event;

import cristina.tech.worker.domain.Dress;
import cristina.tech.worker.domain.DressStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor(force = true)
@Data
@EqualsAndHashCode(callSuper=true)
public class DressUpdatedEvent extends DressEvent {
    private static final long serialVersionUID = 1126074635410771216L;

    private Dress payload;

    public DressUpdatedEvent(String payloadKey, Dress payload, Long timestamp) {
        super(DressEventType.UPDATED, payloadKey, timestamp);
        this.payload = payload;
        this.payload.setStatus(DressStatus.UPDATED);
    }
}
