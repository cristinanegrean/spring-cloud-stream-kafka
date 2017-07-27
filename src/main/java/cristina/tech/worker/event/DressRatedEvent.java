package cristina.tech.worker.event;

import cristina.tech.worker.domain.DressStatus;
import cristina.tech.worker.domain.Rating;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(force = true)
@Data
@EqualsAndHashCode(callSuper=true)
public class DressRatedEvent extends DressEvent {

    private static final long serialVersionUID = 1126074635410771214L;

    private Rating payload;

    public DressRatedEvent(String payloadKey, Rating payload, Long timestamp) {
        super(DressEventType.RATED, payloadKey, timestamp);
        this.payload = payload;
    }

}
