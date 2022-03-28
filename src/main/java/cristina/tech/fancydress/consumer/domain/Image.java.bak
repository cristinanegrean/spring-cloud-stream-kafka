package cristina.tech.fancydress.worker.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Embeddable data object, part of the {@link Dress} core aggregate.
 */
@NoArgsConstructor(force = true)
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Image implements Serializable {

    private static final long serialVersionUID = 1126074635410771215L;

    @JsonProperty("large_url")
    private String largeUrl;

    @JsonProperty("thumb_url")
    private String thumbUrl;
}
