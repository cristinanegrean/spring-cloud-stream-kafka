package cristina.tech.fancydress.worker.event;

import cristina.tech.fancydress.DressStatus;

/**
 * The {@link DressEventType} represents a collection of possible events that describe
 * state transitions of {@link DressStatus} on the {@link cristina.tech.fancydress.worker.domain.Dress} aggregate.
 */
public enum DressEventType {
    CREATED,
    UPDATED,
    RATED
}
