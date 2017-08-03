package cristina.tech.fancydress.store.repository;

import cristina.tech.fancydress.store.domain.Rating;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RatingRepository extends CrudRepository<Rating, Integer> {

    /**
     * Gets the average rating based on all ratings of a dress.
     *
     * @param dressId the unique dress identifier
     * @return rounded to integer average rating or <code>0</code> if no rating
     */
    @Query(value = "SELECT coalesce(round(avg(stars)), 0) FROM rating WHERE dress_id = :dressId GROUP BY dress_id", nativeQuery = true)
    Integer getAverageRating(@Param("dressId") String dressId);

    @Query(value = "SELECT count(r.*) FROM rating r INNER JOIN dress d ON r.dress_id = d.id AND r.dress_id = :dressId", nativeQuery = true)
    Integer countRatingsByDressIdWithJoin(@Param("dressId") String dressId);
}