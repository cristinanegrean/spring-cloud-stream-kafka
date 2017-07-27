package cristina.tech.web.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "brand")
@NoArgsConstructor(force = true) //Default C-tor needed by Jackson JSON.
@Data
@EqualsAndHashCode(callSuper=true)
public class Brand extends AbstractEntity {
    private static final long serialVersionUID = 26074635410771215L;

    @NotEmpty(message = "Brand name cannot be empty")
    @Size(min = 2, max = 100, message = "Brand name must not be longer than 100 characters and shorter than 2 characters")
    private String name;

    @URL
    private String logoUrl;
}