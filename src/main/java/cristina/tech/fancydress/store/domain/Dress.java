package cristina.tech.fancydress.store.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeId;
import cristina.tech.fancydress.DressStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotEmpty;
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
     * Unique identifier of a dress, as of producer message sink/ingest, also the database ID.
     */
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(generator = "customGenerator")
    @GenericGenerator(name = "customGenerator", strategy = "cristina.tech.fancydress.store.domain.UseUUIDOrGenerate")
    @NotEmpty
    @JsonTypeId
    private String id;

    /**
     * Python producer UUID as of payload key, used in {@link UseUUIDOrGenerate} to assign as database id when present
     */
    @Transient
    @JsonIgnore
    private String uuid;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private DressStatus status = DressStatus.CREATED;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brand")
    @NotNull
    private Brand brand;

    private String color;
    private String name;
    private String season;

    @Range(min = 0, max = 5)
    @JsonProperty("average_rating")
    private Integer averageRating = 0;

    private BigDecimal price;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "dress_image", joinColumns = @JoinColumn(name = "dress_id", referencedColumnName = "id"))
    @Column(name = "thumb_url")
    private List<String> thumbnails;

    public Dress(String id, Brand brand) {
        this.uuid = id;
        this.brand = brand;
    }

}