package cristina.tech.worker.event;

import cristina.tech.worker.domain.Dress;

public class DressRatedEvent extends DressEvent {

    private static final long serialVersionUID = 1126074635410771214L;

    public DressRatedEvent(String payloadKey, Dress payload, long timestamp) {
        super(DressEventType.RATED, payloadKey, payload, timestamp);
    }

}
