package cristina.tech.fancydress;


import cristina.tech.fancydress.store.domain.Brand;
import cristina.tech.fancydress.store.domain.Dress;
import cristina.tech.fancydress.store.domain.Rating;
import cristina.tech.fancydress.store.repository.DressRepository;
import cristina.tech.fancydress.store.repository.RatingRepository;
import cristina.tech.fancydress.worker.event.DressEventType;
import cristina.tech.fancydress.worker.event.DressInboundChannels;
import cristina.tech.fancydress.worker.event.DressMessageEvent;
import cristina.tech.fancydress.worker.event.RatingMessageEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.springframework.http.HttpMethod.GET;

/**
 * Integration Tests: Streaming, Sink store to persistence layer, browse dresses via REST API.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = BootifulDressApplication.class)
@DirtiesContext
public class BootifulDressIntegrationTests {

    private static final String TEST_DRESS_ONE = "test-dress-one";
    private static final String TEST_DRESS_TWO = "test-dress-two";
    private static final String TEST_BRAND = "perfectly basics";
    private static final String TEST_SEASON = "all-around-year-no-seasonality";
    private static final String TEST_COLOR = "blue-crush";
    private static final String TEST_NAME = "pyjama dress";
    private static final BigDecimal TEST_PRICE = new BigDecimal(10.99);

    private static final String TEST_RATING_ID = "test-rating-id";
    private static final int TEST_RATING_STARS = 3;

    /**
     * TestRestTemplate is a convenience alternative to Spring’s RestTemplate that is useful in integration tests.
     * Redirects will not be followed (so you can assert the response location).
     * Cookies will be ignored (so the template is stateless)
     */
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DressInboundChannels dressInboundChannels;

    @Autowired
    private DressRepository dressRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Test
    public void contextLoads() {
        assertNotNull(this.dressInboundChannels.idresses());
        assertNotNull(this.dressInboundChannels.iratings());
    }

    @Test
    /**
     * Dress Repository is annotated as a {@link org.springframework.data.rest.core.annotation.RestResource}.
     */
    public void dressesRepositoryExposedAsRestResource() {
        // setup API response type to be a Dress HTTP Resource
        ParameterizedTypeReference<Resource<Dress>> responseType = new ParameterizedTypeReference<Resource<Dress>>() {
        };

        ResponseEntity<Resource<Dress>> dressesUri =
                restTemplate.exchange(UriComponentsBuilder.fromPath("/dresses")
                        .queryParam("page", "0")
                        .queryParam("size", "25")
                        .queryParam("sort", "name, asc")
                        .build().toString(), GET, null, responseType);

        // assert response on client rest template exchange
        assertThat(dressesUri.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(dressesUri.getHeaders().getContentType().toString()).isEqualTo("application/hal+json;charset=UTF-8");

        // assert body is empty, contains no persisted dress items
        assertThat(dressesUri.getBody().getContent().getId()).isEqualTo(null);
    }

    @Test
    /**
     * Stream, store a DressMessageEvent. Browse the dress as a HTTP Resource.
     */
    public void streamStoreBrowseDress() {
        // send a test dress message event to stream listener component
        this.dressInboundChannels.idresses().send(new GenericMessage<>(getDressMessageEvent(TEST_DRESS_ONE)));

        assertDressStored(TEST_DRESS_ONE, 0);
        browseDressUriCheckContent(TEST_DRESS_ONE, 0);
    }

    @Test
    /**
     * Stream, store a RatingMessageEvent and also a DressMessageEvent. Browse the dress by id, check average rating.
     */
    public void streamStoreRatingAndDressBrowseDressCheckAverageRating() {
        // send a test rating message event to stream listener component
        this.dressInboundChannels.iratings().send(new GenericMessage<>(getRatingMessageEvent(TEST_DRESS_TWO)));

        // send a test dress message event to stream listener component
        this.dressInboundChannels.idresses().send(new GenericMessage<>(getDressMessageEvent(TEST_DRESS_TWO)));

        // assert rating is stored
        Rating rating = ratingRepository.findOne(TEST_RATING_ID);
        assertThat(rating.getStars()).isEqualTo(TEST_RATING_STARS);
        assertThat(rating.getEventTime()).isNotNull();

        // assert that dress is stored, check that service call updated automatically average rating
        assertDressStored(TEST_DRESS_TWO, TEST_RATING_STARS);
        browseDressUriCheckContent(TEST_DRESS_TWO, TEST_RATING_STARS);
    }


    /**
     * Assert that a dress has been created in persistent store with given uuid
     */
    private void assertDressStored(String expectedId, Integer expectedAverageRating) {
        Optional<Dress> storeOptionalDress = dressRepository.findById(expectedId);
        assertThat(storeOptionalDress.isPresent());
        assertThat(storeOptionalDress.get().getId()).isEqualTo(expectedId);
        assertThat(ratingRepository.getAverageRating(expectedId)).isEqualTo(expectedAverageRating);
        assertThat(storeOptionalDress.get().getAverageRating()).isEqualTo(expectedAverageRating);
    }

    /**
     * Assert I can browse the dress details via the REST API.
     */
    private void browseDressUriCheckContent(String dressId, int expectedAverageRating) {
        ParameterizedTypeReference<Resource<Dress>> responseType = new ParameterizedTypeReference<Resource<Dress>>() {
        };
        ResponseEntity<Resource<Dress>> dressUriSearch =
                restTemplate.exchange(UriComponentsBuilder.fromPath("/dresses/" + dressId)
                        .build().toString(), GET, null, responseType);

        // assert response on client rest template exchange
        assertThat(dressUriSearch.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(dressUriSearch.getHeaders().getContentType().toString()).isEqualTo("application/hal+json;charset=UTF-8");

        // assert body matches dress message event details
        assertThat(dressUriSearch.getBody().getContent().getBrand().getName()).isEqualTo(TEST_BRAND);
        assertThat(dressUriSearch.getBody().getContent().getName()).isEqualTo(TEST_NAME);
        assertThat(dressUriSearch.getBody().getContent().getSeason()).isEqualTo(TEST_SEASON);
        assertThat(dressUriSearch.getBody().getContent().getAverageRating()).isEqualTo(expectedAverageRating);
        assertThat(dressUriSearch.getBody().getContent().getColor()).isEqualTo(TEST_COLOR);
        assertThat(dressUriSearch.getBody().getContent().getPrice().doubleValue()).isEqualTo(TEST_PRICE.doubleValue());
    }


    @Test
    /**
     * Test {@link Rating} entity is not exposed as standalone hypermedia Resource.
     * {@link RatingRepository} is not annotated as a {@link org.springframework.data.rest.core.annotation.RestResource}.
     */
    public void ratingsRepositoryNotExposedAsRestResource() {
        // setup API response type to be a Rating HTTP Resource
        ParameterizedTypeReference<Resource<Rating>> responseType = new ParameterizedTypeReference<Resource<Rating>>() {
        };

        ResponseEntity<Resource<Rating>> ratingsUri =
                restTemplate.exchange(UriComponentsBuilder.fromPath("/ratings")
                        .queryParam("page", "0")
                        .queryParam("size", "10")
                        .build().toString(), GET, null, responseType);
        assertThat(HttpStatus.NOT_FOUND.value()).isEqualTo(ratingsUri.getStatusCode().value());
    }

    @Test
    /**
     * Test {@link Brand} entity is not exposed as standalone hypermedia Resource.
     * {@link cristina.tech.fancydress.store.repository.BrandRepository}is not annotated as a
     * {@link org.springframework.data.rest.core.annotation.RestResource}.
     */
    public void brandsRepositoryNotExposedAsRestResource() {
        // setup API response type to be a Rating HTTP Resource
        ParameterizedTypeReference<Resource<Brand>> responseType = new ParameterizedTypeReference<Resource<Brand>>() {
        };

        ResponseEntity<Resource<Brand>> brandsUri =
                restTemplate.exchange(UriComponentsBuilder.fromPath("/brands")
                        .queryParam("page", "0")
                        .queryParam("size", "50")
                        .build().toString(), GET, null, responseType);
        assertThat(HttpStatus.NOT_FOUND.value()).isEqualTo(brandsUri.getStatusCode().value());
    }

    private static DressMessageEvent getDressMessageEvent(String dressId) {
        DressMessageEvent createDressEvent = new DressMessageEvent();
        createDressEvent.setStatus(DressStatus.CREATED);
        createDressEvent.setPayloadKey(dressId);
        cristina.tech.fancydress.worker.domain.Dress dress = new cristina.tech.fancydress.worker.domain.Dress();
        cristina.tech.fancydress.worker.domain.Brand brand = new cristina.tech.fancydress.worker.domain.Brand();
        brand.setName(TEST_BRAND);
        dress.setId(dressId);
        dress.setBrand(brand);
        dress.setSeason(TEST_SEASON);
        dress.setPrice(TEST_PRICE);
        dress.setColor(TEST_COLOR);
        dress.setName(TEST_NAME);
        createDressEvent.setPayload(dress);
        createDressEvent.setEventType(DressEventType.CREATED);
        createDressEvent.setTimestamp(Instant.now().toEpochMilli());

        return createDressEvent;
    }

    private static RatingMessageEvent getRatingMessageEvent(String dressId) {
        RatingMessageEvent dressRatedEvent = new RatingMessageEvent();
        dressRatedEvent.setPayloadKey(TEST_RATING_ID);
        cristina.tech.fancydress.worker.domain.Rating rating = new cristina.tech.fancydress.worker.domain.Rating();
        rating.setDressId(dressId);
        rating.setRatingId(TEST_RATING_ID);
        rating.setStars(TEST_RATING_STARS);
        dressRatedEvent.setPayload(rating);
        dressRatedEvent.setEventType(DressEventType.RATED);
        dressRatedEvent.setTimestamp(Instant.now().toEpochMilli());

        return dressRatedEvent;
    }
}
