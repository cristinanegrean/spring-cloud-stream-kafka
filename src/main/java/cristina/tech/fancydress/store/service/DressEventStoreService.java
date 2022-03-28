package cristina.tech.fancydress.store.service;

import cristina.tech.fancydress.DressStatus;
import cristina.tech.fancydress.store.domain.Brand;
import cristina.tech.fancydress.store.domain.Dress;
import cristina.tech.fancydress.store.repository.BrandRepository;
import cristina.tech.fancydress.store.repository.DressRepository;
import cristina.tech.fancydress.store.repository.RatingRepository;
import cristina.tech.fancydress.consumer.event.DressEventType;
import cristina.tech.fancydress.consumer.event.ConsumerEventTypes.DressMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

@Service
@Transactional
public class DressEventStoreService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DressRepository dressRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private RatingRepository ratingRepository;

    /**
     * Apply dress created or updated event, by saving it to persistent storage.
     *
     * @param dressMessageEvent any message coming from the idresses channel
     * @return indicator flag if event has been applied, otherwise runtime exception is thrown
     */
    @Transactional
    public boolean apply(DressMessageEvent dressMessageEvent) {
        Dress dress = fromDressMessageEvent(
                dressMessageEvent, dressMessageEvent.getEventType() == DressEventType.CREATED);
        if (dress == null) {
            return false;
        }

        // set average rating, hence rating message event may have arrived
        // before dress create or update message event
        OptionalDouble averageRating = getAverageRating(dress.getUuid()); // attention database id is not generated yet
        dress.setAverageRating(averageRating.isPresent() ? (int) Math.round(averageRating.getAsDouble()) : 0);
        dressRepository.save(dress);
        return true;
    }

    private Dress fromDressMessageEvent(DressMessageEvent dressMessageEvent, boolean isCreate) {
        if (dressMessageEvent == null || dressMessageEvent.payloadKey() == null) {
            return null;
        }

        // is dress persisted due to a previous CREATE or UPDATE event?
        Optional<Dress> dressOptional = dressRepository.findById(dressMessageEvent.payloadKey());

        if (isCreate && dressOptional.isPresent()) {
            // handle out-of-order, CREATE message arrived after UPDATED message has already been processed, skip CREATE
            return null;
        }

        Dress dress = dressOptional.orElseGet(() -> new Dress(dressMessageEvent.payloadKey(), fromDressMessageEvent(dressMessageEvent.payload().brand())));
        dress.setStatus(isCreate ? DressStatus.CREATED : DressStatus.UPDATED);

        dress.setName(dressMessageEvent.payload().name());
        dress.setSeason(dressMessageEvent.payload().season());
        dress.setPrice(dressMessageEvent.payload().price());
        dress.setColor(dressMessageEvent.payload().color());

        // any image thumbnails?
        if (dressMessageEvent.payload().images() != null) {
            dressMessageEvent.payload().images().forEach(
                    i -> {
                        if (dress.getThumbnails() == null) { // init
                            dress.setThumbnails(new ArrayList<>());
                        } else if (!isCreate && dress.getThumbnails() != null && !dress.getThumbnails().isEmpty()) {
                            // when updating dress, current message thumbs will overwrite old ones
                            dress.getThumbnails().clear();
                        }

                        dress.getThumbnails().add(i.thumbUrl());
                    });
        }

        return dress;
    }

    private Brand fromDressMessageEvent(cristina.tech.fancydress.consumer.domain.ConsumerDomainTypes.Brand eventBrand) {
        if (eventBrand == null) {
            return null;
        }

        // does brand already exist?
        Optional<Brand> brandOptional = brandRepository.findByName(eventBrand.name());
        // found one by unique name
        return brandOptional.orElseGet(() -> brandRepository.save(new Brand(eventBrand.name(), eventBrand.logoUrl())));
    }

    /**
     * A mechanism to calculate the average for a dress based on the stored ratings and not relying on Postgres aggregate function
     * as in {@link RatingRepository#getAverageRating(String)}
     */
    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    public OptionalDouble getAverageRating(String dressId) {
        List<Integer> allStars = ratingRepository.listStarsByDressId(dressId);

        return allStars.stream().mapToDouble(s -> s).average();
    }

}
