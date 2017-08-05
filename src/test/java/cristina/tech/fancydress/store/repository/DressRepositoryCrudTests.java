package cristina.tech.fancydress.store.repository;

import cristina.tech.fancydress.store.domain.Brand;
import cristina.tech.fancydress.store.domain.Dress;
import cristina.tech.fancydress.store.domain.Rating;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class DressRepositoryCrudTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(DressRepositoryCrudTests.class);

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
        Dress dress = new Dress("one");
        dress.setName("two");
        dress.setColor("blue");
        dress.setSeason("summer");

        Brand brand = new Brand("dressy", "https://dressy.logo");
        entityManager.persist(brand);

        assertThat(brandRepository.findByName(brand.getName()).isPresent());

        dress.setBrand(brand);
        entityManager.persist(dress);

        // check id has been assigned
        assertThat(dress.getId()).isEqualTo("one");

        Dress dressById = dressRepository.findOne("one");
        assertThat(dressById.getBrand()).isEqualTo(brand);
        assertThat(dressById.getAverageRating()).isEqualTo(0);
        assertThat(dressById.getName()).isEqualTo("two");
        assertThat(dressById.getColor()).isEqualTo("blue");
        assertThat(dressById.getSeason()).isEqualTo("summer");
    }

    @Test
    public void getTrending() {
        Brand brand = new Brand("vintage", "http://vintage.com");
        entityManager.persist(brand);

        Dress dress1 = new Dress("one");
        dress1.setName("mini");
        dress1.setBrand(brand);
        Dress dress2 = new Dress("two");
        dress2.setName("maxi");
        dress2.setBrand(brand);

        // no out-of-order data here, dress data arrives first
        entityManager.persist(dress1);
        entityManager.persist(dress2);

        // rating data arrives
        Rating rating1dress1 = new Rating("one", "one");
        Rating rating2dress2 = new Rating("two", "two");
        Rating rating3dress2 = new Rating("three", "two");

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
        assertThat(ratingRepository.countRatingsByDressIdWithJoin("one")).isEqualTo(1);
        assertThat(ratingRepository.countRatingsByDressIdWithJoin("two")).isEqualTo(2);


        String startDate = now().minusMinutes(1).format(formatter);
        String endDate = now().plusMinutes(1).format(formatter);
        LOGGER.info(String.format("Start date: %s, end date: %s",
                startDate, endDate));

        // assert find top N in 2 minutes window
        List<Object[]> trendingDresses =
                dressRepository.findTopNTrendingByTimeWindow(startDate, endDate, 2);
        assertThat(trendingDresses).hasSize(2);

        // assert ordering, dress 2 with 2 ratings on top, dress 1 with one rating on bottom
        Object[] top1DressDetails = trendingDresses.get(0);
        Object[] top2DressDetails = trendingDresses.get(1);

        assertThat(top1DressDetails[0]).isEqualTo("two"); // dress id
        assertThat(top1DressDetails[1]).isEqualTo(BigInteger.valueOf(2L));  // dress ratings count
        assertThat(top1DressDetails[2]).isEqualTo("maxi"); // dress name
        assertThat(top1DressDetails[7]).isEqualTo("vintage"); // dress brand name

        assertThat(top2DressDetails[0]).isEqualTo("one");
        assertThat(top2DressDetails[1]).isEqualTo(BigInteger.valueOf(1L));
        assertThat(top2DressDetails[2]).isEqualTo("mini");
        assertThat(top2DressDetails[7]).isEqualTo("vintage");

        // assert top N is filtering correctly the result set count
        trendingDresses =
                dressRepository.findTopNTrendingByTimeWindow(startDate, endDate, 1);
        assertThat(trendingDresses).hasSize(1);

    }

    private static final LocalDateTime now() {
        return LocalDateTime.now();
    }

}
