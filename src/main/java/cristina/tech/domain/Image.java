package cristina.tech.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Embeddable data object, part of the {@link Dress} domain aggregate.
 */
@Data
@ToString
@NoArgsConstructor(force = true)
public class Image implements Serializable {

    private static final long serialVersionUID = 1126074635410771215L;

    private String largeUrl;
    private String thumbUrl;

}
