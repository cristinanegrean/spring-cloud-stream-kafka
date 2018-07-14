package cristina.tech.fancydress.store.repository;

import cristina.tech.fancydress.store.domain.Brand;
import cristina.tech.fancydress.store.domain.Dress;
import cristina.tech.fancydress.store.domain.Rating;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class DressRepositoryCrudTests {

    @Autowired
    private TestEntityManager entityManager; // alternative to JPA EntityManager designed for tests

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private DressRepository dressRepository;

    @Autowired
    private RatingRepository ratingRepository;

    private final static DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Test
    public void saveValid() {
        Brand brand = new Brand("dressy", "https://dressy.logo");
        entityManager.persist(brand);

        assertTrue(brandRepository.findByName(brand.getName()).isPresent());

        Dress dress = new Dress("one", brand);
        dress.setName("two");
        dress.setColor("blue");
        dress.setSeason("summer");

        dress.setBrand(brand);
        entityManager.persist(dress);

        // check id has been assigned
        assertThat(dress.getId()).isEqualTo("one");

        Optional<Dress> dressById = dressRepository.findById("one");
        assertTrue(dressById.isPresent());
        assertThat(dressById.get().getBrand()).isEqualTo(brand);
        assertThat(dressById.get().getAverageRating()).isEqualTo(0);
        assertThat(dressById.get().getName()).isEqualTo("two");
        assertThat(dressById.get().getColor()).isEqualTo("blue");
        assertThat(dressById.get().getSeason()).isEqualTo("summer");
    }

    @Test
    public void getTrending() {
        Brand brand = new Brand("vintage", "http://vintage.com");
        entityManager.persist(brand);

        Dress dress1 = new Dress("1", brand);
        dress1.setName("mini");
        Dress dress2 = new Dress("2", brand);
        dress2.setName("maxi");

        // no out-of-order data here, dress data arrives first
        entityManager.persist(dress1);
        entityManager.persist(dress2);

        // rating data arrives
        Rating rating1dress1 = new Rating("1", "1");
        Rating rating2dress2 = new Rating("2", "2");
        Rating rating3dress2 = new Rating("3", "2");

        rating1dress1.setStars(5);
        rating1dress1.setEventTime(now());

        rating2dress2.setStars(2);
        rating2dress2.setEventTime(now());

        rating3dress2.setStars(3);
        rating3dress2.setEventTime(now());

        entityManager.persist(rating1dress1);
        entityManager.persist(rating2dress2);
        entityManager.persist(rating3dress2);

        // assert all persisted
        assertThat(dressRepository.count()).isEqualTo(2);
        assertThat(ratingRepository.count()).isEqualTo(3);

        // check links
        assertThat(ratingRepository.countRatingsByDressIdWithJoin("1")).isEqualTo(1);
        assertThat(ratingRepository.countRatingsByDressIdWithJoin("2")).isEqualTo(2);


        String startDate = now().minusMinutes(1).format(formatter);
        String endDate = now().plusMinutes(1).format(formatter);

        // assert find top N in 2 minutes window
        List<Object[]> trendingDresses =
                dressRepository.findTopNTrendingTimeWindow(startDate, endDate, 2);
        assertThat(trendingDresses).hasSize(2);

        // assert ordering, dress 2 with 2 ratings on top, dress 1 with one rating on bottom
        Object[] top1DressDetails = trendingDresses.get(0);
        Object[] top2DressDetails = trendingDresses.get(1);

        assertThat(top1DressDetails[0]).isEqualTo("2"); // dress id
        assertThat(top1DressDetails[1]).isEqualTo(BigInteger.valueOf(2L));  // dress ratings count
        assertThat(top1DressDetails[2]).isEqualTo("maxi"); // dress name
        assertThat(top1DressDetails[7]).isEqualTo("vintage"); // dress brand name

        assertThat(top2DressDetails[0]).isEqualTo("1");
        assertThat(top2DressDetails[1]).isEqualTo(BigInteger.valueOf(1L));
        assertThat(top2DressDetails[2]).isEqualTo("mini");
        assertThat(top2DressDetails[7]).isEqualTo("vintage");

        // assert top N is filtering correctly the result set count
        trendingDresses =
                dressRepository.findTopNTrendingTimeWindow(startDate, endDate, 1);
        assertThat(trendingDresses).hasSize(1);

    }

    private static LocalDateTime now() {
        return LocalDateTime.now();
    }

}
