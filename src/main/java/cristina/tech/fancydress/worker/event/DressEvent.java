package cristina.tech.fancydress.worker.event;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * The core event {@link DressEvent} tracks the type and state of events as
 * applied to the {@link cristina.tech.fancydress.worker.domain.Dress} core object. This event resource can be used
 * to event source the aggregate state of {@link cristina.tech.fancydress.worker.domain.Dress}.
 * <p>
 * This event resource also provides a transaction log that can be used to append
 * actions to the event.
 */
@Data
@ToString
@NoArgsConstructor(force = true) //Jackson JSON needs this for deserialization
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DressEvent implements Serializable {

    private static final long serialVersionUID = 1126074635410771217L;

    @JsonProperty("payload_key")
    private String payloadKey;

    @JsonIgnore
    private DressEventType eventType;

    private Long timestamp;


    public DressEvent(DressEventType eventType) {
        this.eventType = eventType;
    }
}
