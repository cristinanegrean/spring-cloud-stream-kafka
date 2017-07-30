package cristina.tech.worker.event;

import cristina.tech.worker.domain.Dress;
import cristina.tech.worker.domain.DressStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DressMessageEvent extends DressEvent {
    private static final long serialVersionUID = 1126074635410771217L;

    private Dress payload;
    private DressStatus status;

    @Override
    public DressEventType getEventType() {
        return (status == DressStatus.UPDATED ? DressEventType.UPDATED : DressEventType.CREATED);
    }

}
