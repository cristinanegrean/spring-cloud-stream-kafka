package cristina.tech.fancydress.store.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "brand")
@NoArgsConstructor(force = true)
@Data
@EqualsAndHashCode(callSuper=true)
public class Brand extends AbstractEntity {
    private static final long serialVersionUID = 26074635410771215L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Integer id;

    @NotEmpty(message = "Brand name cannot be empty")
    private String name;

    @URL
    private String logoUrl;

    public Brand(String name, String logoUrl) {
        this.name = name;
        this.logoUrl = logoUrl;
    }
}