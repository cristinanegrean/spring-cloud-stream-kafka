package cristina.tech.fancydress;

import com.fasterxml.jackson.annotation.JsonProperty;
import cristina.tech.fancydress.worker.domain.Dress;

/**
 * The {@link DressStatus} describes the state of an {@link Dress}.
 * The aggregate state of a {@link Dress} is sourced from attached core
 * events in the form of {@link cristina.tech.fancydress.worker.event.DressEvent}.
 */
public enum DressStatus {
    @JsonProperty("CREATED")
    CREATED,
    @JsonProperty("UPDATED")
    UPDATED
}
