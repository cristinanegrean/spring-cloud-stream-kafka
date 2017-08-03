package cristina.tech;


import cristina.tech.fancydress.BootifulDressApplication;
import cristina.tech.fancydress.store.domain.Dress;
import cristina.tech.fancydress.store.domain.Rating;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = BootifulDressApplication.class)
public class BootifulDressApplicationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void dressesRepositoryExposedAsRestResource() {
        // setup API response type to be a Dress HTTP Resource
        ParameterizedTypeReference<Resource<Dress>> responseType =
                new ParameterizedTypeReference<Resource<Dress>>() {
                };

        ResponseEntity<Resource<Dress>> dressesUri =
                restTemplate.exchange(UriComponentsBuilder.fromPath("/dresses")
                        .queryParam("page", "0")
                        .queryParam("size", "25")
                        .queryParam("sort", "name, asc")
                        .build().toString(), GET, null, responseType);
        assertThat(HttpStatus.OK.value()).isEqualTo(dressesUri.getStatusCode().value());

    }

    @Test
    public void ratingsRepositoryNotExposedAsRestResource() {
        // setup API response type to be a Rating HTTP Resource
        ParameterizedTypeReference<Resource<Rating>> responseType =
                new ParameterizedTypeReference<Resource<Rating>>() {
                };

        ResponseEntity<Resource<Rating>> ratingsUri =
                restTemplate.exchange(UriComponentsBuilder.fromPath("/ratings")
                        .queryParam("page", "0")
                        .queryParam("size", "10")
                        .build().toString(), GET, null, responseType);
        assertThat(HttpStatus.NOT_FOUND.value()).isEqualTo(ratingsUri.getStatusCode().value());

    }

}
