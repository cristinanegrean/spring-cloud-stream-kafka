package cristina.tech.fancydress.store.repository;

import cristina.tech.fancydress.store.domain.Dress;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "dresses", path = "dresses")
public interface DressRepository extends PagingAndSortingRepository<Dress, Integer> {

    /** Not to confuse with find by database id (uid), see {@link org.springframework.data.repository.CrudRepository#findOne(java.io.Serializable)}
     * This is the lookup by the dress identified assigned by the producer and is a text/string format.
     */
    Optional<Dress> findById(@Param("id") String id);

}