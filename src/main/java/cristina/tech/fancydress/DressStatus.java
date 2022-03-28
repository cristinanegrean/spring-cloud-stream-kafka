package cristina.tech.fancydress;

import com.fasterxml.jackson.annotation.JsonProperty;
import cristina.tech.fancydress.consumer.domain.ConsumerDomainTypes.Dress;
import cristina.tech.fancydress.consumer.event.ConsumerEventTypes.DressEvent;

/**
 * The {@link DressStatus} describes the state of an {@link Dress}.
 * The aggregate state of a {@link Dress} is sourced from attached core
 * events in the form of {@link DressEvent}.
 */
public enum DressStatus {
    @JsonProperty("CREATED")
    CREATED,
    @JsonProperty("UPDATED")
    UPDATED
}
