package cristina.tech.fancydress.store.repository;

import cristina.tech.fancydress.store.domain.Rating;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RatingRepository extends CrudRepository<Rating, Integer> {

    @Query(value = "SELECT ROUND(AVG(stars)) FROM rating WHERE dress_id = :dressId GROUP BY dress_id", nativeQuery = true)
    Integer getAverageRating(@Param("dressId") String dressId);
}