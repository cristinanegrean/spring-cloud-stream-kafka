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
@ToString
@NoArgsConstructor(force = true) //Jackson JSON needs this for deserialization
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Brand implements Serializable {

    String logoUrl;
    String name;

    public Brand(String logoUrl, String name) {
        this.logoUrl = logoUrl;
        this.name = name;
    }
}
