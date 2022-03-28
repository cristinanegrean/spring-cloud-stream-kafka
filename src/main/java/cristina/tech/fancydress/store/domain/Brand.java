package cristina.tech.fancydress.store.domain;

import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Objects;

@Entity
@Table(name = "brand")
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

    public Brand() {
        // NoArgsConstructor
    }

    public Brand(String name, String logoUrl) {
        this.name = name;
        this.logoUrl = logoUrl;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Brand brand = (Brand) o;
        return Objects.equals(this.name, brand.name) &&
                Objects.equals(this.logoUrl, brand.logoUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, logoUrl);
    }

}