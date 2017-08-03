package cristina.tech.fancydress.store.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "rating")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(force = true)
public class Rating extends AbstractEntity {

    private static final long serialVersionUID = 74635410771214L;

    @Column(name = "rating_id")
    @NotEmpty
    private String ratingId;

    @Column(name="dress_id")
    @NotEmpty
    private String dressId;

    @NotNull
    private Integer stars;

    @Column(name="event_time")
    @NotNull
    private LocalDateTime eventTime;

    public Rating(String ratingId, String dressId) {
        this.ratingId = ratingId;
        this.dressId = dressId;
    }

}
