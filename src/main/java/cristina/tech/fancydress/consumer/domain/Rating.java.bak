package cristina.tech.fancydress.worker.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import cristina.tech.fancydress.worker.event.RatingMessageEvent;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * JSON Payload of {@link RatingMessageEvent}.
 */
@Data
@NoArgsConstructor(force = true) //Jackson JSON needs this for deserialization
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Rating implements Serializable {

    private static final long serialVersionUID = 1236074635410771215L;

    @JsonProperty("dress_id")
    private String dressId;

    @JsonProperty("rating_id")
    private String ratingId;
    private Integer stars;

}
