package cristina.tech.fancydress.store.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeId;
import cristina.tech.fancydress.DressStatus;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.List;


@Entity
@Table(name = "dress")
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

    public Dress() {
        // NoArgsConstructor
    }

    public Dress(String id, Brand brand) {
        this.uuid = id;
        this.brand = brand;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setStatus(DressStatus status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public DressStatus getStatus() {
        return status;
    }

    public Brand getBrand() {
        return brand;
    }

    public String getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public String getSeason() {
        return season;
    }

    public Integer getAverageRating() {
        return averageRating;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public List<String> getThumbnails() {
        return thumbnails;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public void setAverageRating(Integer averageRating) {
        this.averageRating = averageRating;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setThumbnails(List<String> thumbnails) {
        this.thumbnails = thumbnails;
    }
}