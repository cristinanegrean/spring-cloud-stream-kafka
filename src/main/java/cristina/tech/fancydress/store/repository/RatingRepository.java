package cristina.tech.fancydress.store.repository;

import cristina.tech.fancydress.store.domain.Rating;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RatingRepository extends CrudRepository<Rating, Integer> {

    @Query("select r.stars from Rating r where r.dressId = :dressId")
    List<Integer> listStarsByDressId(@Param("dressId") String dressId);
}