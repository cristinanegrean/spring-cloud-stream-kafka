package cristina.tech.web;

import cristina.tech.web.domain.Dress;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "dresses", path = "dresses")
public interface DressRepository extends PagingAndSortingRepository<Dress, Integer> {

}