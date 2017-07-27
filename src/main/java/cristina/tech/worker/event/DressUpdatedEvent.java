package cristina.tech.worker.event;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import cristina.tech.worker.domain.Dress;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(force = true)
@ToString
public class DressUpdatedEvent extends DressEvent {
    private static final long serialVersionUID = 1126074635410771216L;

    @JsonUnwrapped
    private Dress payload;

    public DressUpdatedEvent(String payloadKey, Dress payload, Long timestamp) {
        super(DressEventType.UPDATED, payloadKey, timestamp);
        this.payload = payload;
    }
}
