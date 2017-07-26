package cristina.tech.event;

/**
 * The {@link DressEventType} represents a collection of possible events that describe
 * state transitions of {@link cristina.tech.domain.DressStatus} on the {@link cristina.tech.domain.Dress} aggregate.
 */
public enum DressEventType {
    CREATED, UPDATED, RATED
}
