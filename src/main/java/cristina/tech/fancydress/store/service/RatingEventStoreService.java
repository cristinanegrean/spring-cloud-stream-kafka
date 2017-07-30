package cristina.tech.fancydress.store.service;

import cristina.tech.fancydress.DressStatus;
import cristina.tech.fancydress.store.domain.Brand;
import cristina.tech.fancydress.store.domain.Dress;
import cristina.tech.fancydress.store.domain.Rating;
import cristina.tech.fancydress.store.repository.BrandRepository;
import cristina.tech.fancydress.store.repository.DressRepository;
import cristina.tech.fancydress.store.repository.RatingRepository;
import cristina.tech.fancydress.worker.event.DressEventType;
import cristina.tech.fancydress.worker.event.DressMessageEvent;
import cristina.tech.fancydress.worker.event.RatingMessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

@Service
@Transactional
public class RatingEventStoreService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private DressRepository dressRepository;

    /**
     * Saving orphan ratings is allowed for now, if dress with payload dress id is not found in DB yet.
     * @param ratingMessageEvent any message comming from the iratings channel
     */
    @Transactional
    public void apply(RatingMessageEvent ratingMessageEvent) {
        Rating rating = fromRatingMessageEvent(ratingMessageEvent);

        if (rating != null) {
            ratingRepository.save(rating);
        }

        // does the associated dress already exist?
        Optional<Dress> dressOptional = dressRepository.findById(ratingMessageEvent.getPayload().getDressId());
        if (dressOptional.isPresent()) { //yes, update the average stars aggregate field
            Dress dress = dressOptional.get();
            List<Integer> allStars = ratingRepository.listStarsByDressId(dress.getId());

            OptionalDouble average = allStars
                .stream()
                .mapToDouble(s -> s)
                .average();
            if (average.isPresent()) {
                dress.setAverageRating((int) Math.round(average.getAsDouble()));
                dressRepository.save(dress);
            }
        }
    }

    @Transactional
    private Rating fromRatingMessageEvent(RatingMessageEvent ratingMessageEvent) {
        if (ratingMessageEvent == null || ratingMessageEvent.getPayload() == null) {
            return null;
        }

        Rating rating = new Rating(ratingMessageEvent.getPayloadKey(), ratingMessageEvent.getPayload().getDressId());
        rating.setEventTime(LocalDateTime.ofInstant(
                Instant.ofEpochMilli(ratingMessageEvent.getTimestamp()), ZoneId.systemDefault()));
        rating.setStars(ratingMessageEvent.getPayload().getStars());

        return rating;
    }

}
