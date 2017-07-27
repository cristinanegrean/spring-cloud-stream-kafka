package cristina.tech.worker.event;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import cristina.tech.worker.domain.Dress;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(force = true)
@ToString
public class DressCreatedEvent extends DressEvent {
    private static final long serialVersionUID = 1126074635410771217L;

    @JsonUnwrapped
    private Dress payload;

    public DressCreatedEvent(String payloadKey, Dress payload, Long timestamp) {
        super(DressEventType.CREATED, payloadKey, timestamp);
        this.payload = payload;
    }
}
