package cristina.tech.fancydress.consumer.event;

import cristina.tech.fancydress.DressStatus;

/**
 * The {@link DressEventType} represents a collection of possible events that describe
 * state transitions of {@link DressStatus} on the {@link cristina.tech.fancydress.consumer.domain.ConsumerDomainTypes.Dress} aggregate.
 */
public enum DressEventType {
    CREATED,
    UPDATED,
    RATED
}
