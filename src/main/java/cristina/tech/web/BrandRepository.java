package cristina.tech.web;


import cristina.tech.web.domain.Brand;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "brands", path = "brands")
public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer> {

    Optional<Brand> findByName(@Param("name") String name);

}
