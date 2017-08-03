package cristina.tech.fancydress.store.repository;

import cristina.tech.fancydress.store.domain.Rating;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RatingRepositoryTest {

    private static final String DRESS_ID = "dress one";

    @Autowired
    private TestEntityManager entityManager; // alternative to JPA EntityManager designed for tests

    @Autowired
    private RatingRepository ratingRepository;

    @Test(expected = ConstraintViolationException.class)
    public void saveInvalid() {
        Rating rating = new Rating("rating-one", DRESS_ID);
        rating.setEventTime(LocalDateTime.now());

        // stars instance field not supplied, but not nullable
        // see @Test(expected = ConstraintViolationException.class)
        entityManager.persist(rating);
    }

    @Test
    public void saveValid() {
        Rating rating1 = new Rating("1st rating", DRESS_ID);
        Rating rating2 = new Rating("2nd rating", DRESS_ID);
        Rating rating3 = new Rating("3rd rating", DRESS_ID);

        rating1.setEventTime(now());
        rating2.setEventTime(now());
        rating3.setEventTime(now());
        rating1.setStars(4);
        rating2.setStars(3);
        rating3.setStars(3);

        // use-case, out-of-order data: ratings events arrive first, dress data may arrive later
        entityManager.persist(rating1);
        entityManager.persist(rating2);
        entityManager.persist(rating3);

        // check round to nearest integer, via native SQL Postgres function
        assertThat(ratingRepository.getAverageRating(DRESS_ID)).isEqualTo(3);
    }

    private static final LocalDateTime now() {
        return LocalDateTime.now();
    }
}
