package cristina.tech.worker.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Embeddable data object, part of the {@link Dress} domain aggregate.
 */
@Data
@ToString
@NoArgsConstructor(force = true) //Jackson JSON needs this for deserialization
public class Brand {

    String logoUrl;
    String name;

    public Brand(String logoUrl, String name) {
        this.logoUrl = logoUrl;
        this.name = name;
    }
}
