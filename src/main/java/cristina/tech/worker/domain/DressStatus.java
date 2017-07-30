package cristina.tech.worker.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The {@link DressStatus} describes the state of an {@link Dress}.
 * The aggregate state of a {@link Dress} is sourced from attached core
 * events in the form of {@link cristina.tech.worker.event.DressEvent}.
 */
public enum DressStatus {
    @JsonProperty("CREATED")
    CREATED,
    @JsonProperty("UPDATED")
    UPDATED
}
