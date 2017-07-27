package cristina.tech.worker.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Embeddable data object, part of the {@link Dress} domain aggregate.
 */
@Data
@NoArgsConstructor(force = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Image implements Serializable {

    private static final long serialVersionUID = 1126074635410771215L;

    private String largeUrl;
    private String thumbUrl;

    public Image(String largeUrl, String thumbUrl) {
        this.largeUrl = largeUrl;
        this.thumbUrl = thumbUrl;
    }

}
