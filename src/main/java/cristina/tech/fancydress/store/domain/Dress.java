package cristina.tech.fancydress.store.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import cristina.tech.fancydress.DressStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


@Entity
@Table(name = "dress")
@NoArgsConstructor(force = true) //Default C-tor needed by Jackson JSON.
@Data
@EqualsAndHashCode(callSuper = true)
public class Dress extends AbstractEntity {
    private static final long serialVersionUID = 9026074635410771215L;

    /**
     * Unique identifier of a dress, as of producer message sink/ingest, not the database ID.
     */
    private String id;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private DressStatus status = DressStatus.CREATED;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brand")
    private Brand brand;

    private String color;
    private String name;
    private String season;

    @Range(min = 0, max = 5)
    @JsonProperty("average_rating")
    private Integer averageRating = 0;

    private BigDecimal price;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "dress_image", joinColumns = @JoinColumn(name = "dress_uid", referencedColumnName = "uid"))
    @Column(name = "thumb_url")
    private List<String> thumbnails;

    public Dress(String id) {
        this.id = id;
    }

}