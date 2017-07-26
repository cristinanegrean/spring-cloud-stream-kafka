package cristina.tech.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The {@link Dress} domain object contains information related to a fashionable dress.
 * The status of a dress is event sourced using events logged to the {@link cristina.tech.event.DressEvent}
 * collection attached to this resource.
 * <p>
 * By setting 'spring.cloud.stream.bindings.input.contentType' configuration to 'application/json''
 * in application.yml, Spring Cloud Stream automatically translates 'JSON' message to Java POJO.
 * Using the 'spring.cloud.stream.bindings.input.contentType configuration with same value translates a
 * Java POJO to JSON message.
 */
@Getter
@ToString
@NoArgsConstructor(force = true) //Jackson JSON needs this for deserialization
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Dress implements Serializable {

    private static final long serialVersionUID = 1126074635410771215L;

    private String id, name, color, season;
    private List<Image> images = new ArrayList<>();
    private ZonedDateTime activationDate;
    private Brand brand;
    private DressStatus status;
    private BigDecimal price;

    /**
     * All args C-tor.
     *
     * @return a fully constructed {@link} Dress POJO
     */
    public Dress(String id, String name, String color, String season,
                 ZonedDateTime activationDate, Brand brand, DressStatus status, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.season = season;
        this.activationDate = activationDate;
        this.brand = brand;
        this.status = status;
        this.price = price;
    }
}
