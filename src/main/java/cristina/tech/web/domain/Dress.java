package cristina.tech.web.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;


@Entity
@Table(name = "brand")
@NoArgsConstructor(force = true) //Default C-tor needed by Jackson JSON.
@Data
@EqualsAndHashCode(callSuper=true)
public class Dress extends AbstractEntity {
    private static final long serialVersionUID = 9026074635410771215L;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brand")
    @NotNull
    private Brand brand;

    private String color;
    private String name;
    private String season;

    @Range(min = 0, max = 5)
    @JsonProperty("average_rating")
    private Integer averageRating;
    private BigDecimal price;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "dress_image", joinColumns = @JoinColumn(name = "dress", referencedColumnName = "id"))
    @Column(name = "image")
    private List<String> images;

}
