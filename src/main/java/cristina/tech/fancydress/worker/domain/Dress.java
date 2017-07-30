package cristina.tech.fancydress.worker.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import cristina.tech.fancydress.DressStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@link Dress} core object contains information related to a fashionable dress.
 * The eventType of a dress is event sourced using events logged to the {@link cristina.tech.fancydress.worker.event.DressEvent}
 * <p>
 * By setting 'spring.cloud.stream.bindings.input.contentType' configuration to 'application/json''
 * in application.yml, Spring Cloud Stream automatically translates 'JSON' message to Java POJO.
 */
@NoArgsConstructor(force = true) //Jackson JSON needs this for deserialization
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Dress implements Serializable {

    private static final long serialVersionUID = 1126074635410771215L;

    private String id, name, color, season;

    private List<Image> images = new ArrayList<>();

    private Brand brand;

    @JsonIgnore
    private DressStatus status;

    private BigDecimal price;
}
