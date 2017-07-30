package cristina.tech.fancydress.store.repository;


import cristina.tech.fancydress.store.domain.Brand;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BrandRepository extends CrudRepository<Brand, Integer> {

    Optional<Brand> findByName(@Param("name") String name);

}
