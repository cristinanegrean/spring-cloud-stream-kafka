package cristina.tech.worker.event;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import cristina.tech.worker.domain.Dress;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * The domain event {@link DressEvent} tracks the type and state of events as
 * applied to the {@link cristina.tech.worker.domain.Dress} domain object. This event resource can be used
 * to event source the aggregate state of {@link cristina.tech.worker.domain.Dress}.
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

    private DressEventType status;
    private String payloadKey;
    private Long timestamp;

    public DressEvent(DressEventType status, String payloadKey, Long timestamp) {
        this.status = status;
        this.payloadKey = payloadKey;
        this.timestamp = timestamp;
    }

}
