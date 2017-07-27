package cristina.tech.worker.event;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The {@link DressEventType} represents a collection of possible events that describe
 * state transitions of {@link cristina.tech.worker.domain.DressStatus} on the {@link cristina.tech.worker.domain.Dress} aggregate.
 */
public enum DressEventType {
    @JsonProperty("CREATED")
    CREATED,
    @JsonProperty("UPDATED")
    UPDATED,
    @JsonProperty("CREATED")
    RATED
}
