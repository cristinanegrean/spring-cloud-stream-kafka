package cristina.tech.fancydress.store.repository;

import cristina.tech.fancydress.store.domain.Dress;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "items", path = "dresses")
public interface DressRepository extends PagingAndSortingRepository<Dress, String> {

    /**
     * Possibly replace Posgres aggregate function with Postgres window function dense_rank(),
     * see: <a href="https://www.postgresql.org/docs/9.5/static/functions-window.html"/>
     * <p>
     * Ordering done directly in native SQL based on ratings count.
     */
    String TRENDING_DRESSES_NATIVE_QUERY_START =
            "select r.dress_id, count(r.id), d.name, d.season, d.color, d.price, d.average_rating, " +
                    "b.name as brandName " +
                    "from rating r inner join dress d " +
                    "on r.dress_id = d.id ";
    String EVENT_TIME_WINDOW = "and r.event_time between :startDate and :endDate ";
    String EVENT_TIME_INTERVAL = "and age(r.event_time) < cast(:interval AS interval) ";
    String TRENDING_DRESSES_NATIVE_QUERY_END =
            "inner join brand b on d.brand = b.id " +
                    "group by r.dress_id, d.name, d.season, d.color, d.price, d.average_rating, b.name " +
                    "order by count(r.id) desc limit :topN";


    String TRENDING_DRESSES_NATIVE_QUERY_H2 =
            TRENDING_DRESSES_NATIVE_QUERY_START + EVENT_TIME_WINDOW + (TRENDING_DRESSES_NATIVE_QUERY_END);

    String TRENDING_DRESSES_NATIVE_QUERY =
            TRENDING_DRESSES_NATIVE_QUERY_START + EVENT_TIME_INTERVAL + (TRENDING_DRESSES_NATIVE_QUERY_END);


    /**
     * Looks up dress by unique identifier, as assigned by producer, also the database ID.
     * Alternative to {@link org.springframework.data.repository.CrudRepository#findOne(java.io.Serializable)}
     * BUT does not throw exception if Dress Entity is not found
     * This is the lookup by the dress identified assigned by the producer and is a text/string format.
     */
    Optional<Dress> findById(@Param("id") String id);

    /**
     * Gets the most trending dresses as the dresses with most ratings (aggregate count)
     * within a time window. For tests as in-memory H2 database does not work well with Java8 LocalDateTime, that is the choice to use strings.
     *
     * @param startDate time window start
     * @param endDate   time window end
     * @return most trending dresses page
     */
    @Query(value = TRENDING_DRESSES_NATIVE_QUERY_H2, nativeQuery = true)
    List<Object[]> findTopNTrendingTimeWindow(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("topN") Integer topN);

    @Query(value = TRENDING_DRESSES_NATIVE_QUERY, nativeQuery = true)
    List<Object[]> findTopNTrendingTimeInterval(
            @Param("topN") Integer topN,
            @Param("interval") String interval);

}