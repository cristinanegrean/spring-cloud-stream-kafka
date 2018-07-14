package cristina.tech.fancydress.store.domain;

import com.fasterxml.jackson.annotation.JsonTypeId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "rating")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(force = true)
public class Rating extends AbstractEntity {

    private static final long serialVersionUID = 74635410771214L;

    /**
     * Unique identifier of a dress, as of producer message sink/ingest, also the database ID.
     */
    @Id
    @Column(name = "id", unique=true, nullable=false)
    @GeneratedValue(generator="customGenerator")
    @GenericGenerator(name="customGenerator", strategy="cristina.tech.fancydress.store.domain.UseUUIDOrGenerate")
    @JsonTypeId
    @NotEmpty
    private String id;

    /** Python producer UUID as of payload key, used in {@link UseUUIDOrGenerate} to assign as database id when present */
    @Transient
    private String uuid;


    @Column(name="dress_id")
    @NotEmpty
    private String dressId;

    @NotNull
    private Integer stars;

    @Column(name="event_time")
    @NotNull
    private LocalDateTime eventTime;

    public Rating(String ratingId, String dressId) {
        this.uuid = ratingId;
        this.dressId = dressId;
        this.stars = 0;
        eventTime = LocalDateTime.now();
    }

}
