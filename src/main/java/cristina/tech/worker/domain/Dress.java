package cristina.tech.worker.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@link Dress} domain object contains information related to a fashionable dress.
 * The status of a dress is event sourced using events logged to the {@link cristina.tech.worker.event.DressEvent}
 * <p>
 * By setting 'spring.cloud.stream.bindings.input.contentType' configuration to 'application/json''
 * in application.yml, Spring Cloud Stream automatically translates 'JSON' message to Java POJO.
 */
@Data
@NoArgsConstructor(force = true) //Jackson JSON needs this for deserialization
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Dress implements Serializable {

    private static final long serialVersionUID = 1126074635410771215L;

    private String id, name, color, season;

    @JsonUnwrapped
    private List<Image> images = new ArrayList<>();

    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    //private ZonedDateTime activationDate;

    @JsonUnwrapped
    private Brand brand;

    @JsonIgnore
    private DressStatus status;

    private BigDecimal price;

    /**
     * Args C-tor.
     *
     * @return a partially constructed {@link} Dress POJO
     */
    public Dress(String id, String name, Brand brand, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.price = price;
    }
}
