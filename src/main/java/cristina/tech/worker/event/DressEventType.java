package cristina.tech.worker.event;

/**
 * The {@link DressEventType} represents a collection of possible events that describe
 * state transitions of {@link cristina.tech.worker.domain.DressStatus} on the {@link cristina.tech.worker.domain.Dress} aggregate.
 */
public enum DressEventType {
    CREATED,
    UPDATED,
    RATED
}
