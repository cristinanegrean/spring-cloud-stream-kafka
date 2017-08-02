package cristina.tech.fancydress.store.repository;

import cristina.tech.fancydress.store.domain.Dress;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RepositoryRestResource(collectionResourceRel = "dresses", path = "dresses")
public interface DressRepository extends PagingAndSortingRepository<Dress, Integer> {

    /**
     * Possibly replace Posgres aggregate function with Postgres window function dense_rank(),
     * see: <a href="https://www.postgresql.org/docs/9.5/static/functions-window.html"/>
     */
    public static final String TRENDING_DRESSES_QUERY =
            "select r.dress_id, count(r.rating_id) as ratingCount, d.* " +
                    "from rating r inner join dress d " +
                    "on r.dress_id = d.id " +
                    "and r.event_time between :startDate and :endDate " +
                    "inner join brand b " +
                    "on d.brand = b.uid " +
                    "group by r.dress_id, d.uid, d.price, d.name, d.season, d.color, d.average_rating, b.name order by count(r.rating_id) desc";

    /**
     * Looks up dress by unique identifier, as assigned by producer, and not database ID.
     * Not to confuse with find by database id (uid), see {@link org.springframework.data.repository.CrudRepository#findOne(java.io.Serializable)}
     * This is the lookup by the dress identified assigned by the producer and is a text/string format.
     */
    Optional<Dress> findById(@Param("id") String id);

    /**
     * Gets the most trending dresses as the dresses with most ratings (aggregate count)
     * within a time window.
     *
     * @param startDate time window start
     * @param endDate   time window end
     * @return most trending dresses page
     */
    @Query(value = TRENDING_DRESSES_QUERY, nativeQuery = true)
    CompletableFuture<List<Dress>> findMostRatedByTimeWindow(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}