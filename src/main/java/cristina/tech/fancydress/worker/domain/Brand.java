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
@NoArgsConstructor(force = true) //Jackson JSON needs this for deserialization
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Brand implements Serializable {

    private static final long serialVersionUID = 1026074635410771215L;

    @JsonProperty("logo_url")
    String logoUrl;
    String name;
}
