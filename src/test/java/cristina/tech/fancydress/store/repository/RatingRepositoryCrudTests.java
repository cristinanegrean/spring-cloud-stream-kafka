package cristina.tech.fancydress.store.repository;

import cristina.tech.fancydress.store.domain.Rating;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RatingRepositoryCrudTests {

    private static final String DRESS_ONE = "dress one";
    private static final String DRESS_TWO = "dress two";

    @Autowired
    private TestEntityManager entityManager; // alternative to JPA EntityManager designed for tests

    @Autowired
    private RatingRepository ratingRepository;

    @Test
    public void saveValid() {
        Rating rating1 = new Rating("1st rating", DRESS_ONE);
        Rating rating2 = new Rating("2nd rating", DRESS_ONE);
        Rating rating3 = new Rating("3rd rating", DRESS_ONE);

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

        // check entity id generation uses uuid and entity content
        Rating rating1Store = ratingRepository.findOne("1st rating");
        assertThat(rating1Store).isNotNull();
        assertThat(rating1Store.getId()).isEqualTo("1st rating");
        assertThat(rating1Store.getDressId()).isEqualTo(DRESS_ONE);
        assertThat(rating1Store.getStars()).isEqualTo(4);
        assertThat(rating1Store.getEventTime()).isNotNull();

        // check 3 ratings found in DB
        assertThat(ratingRepository.count()).isEqualTo(3);

        // check count ratings with inner join to the dress entity
        assertThat(ratingRepository.countRatingsByDressIdWithJoin(DRESS_ONE)).isEqualTo(0);

        // and assert average rating (round to nearest integer) for each dress
        assertThat(ratingRepository.getAverageRating(DRESS_ONE)).isEqualTo(3);
        assertThat(ratingRepository.getAverageRating(DRESS_TWO)).isEqualTo(0);
    }

    private static final LocalDateTime now() {
        return LocalDateTime.now();
    }
}
