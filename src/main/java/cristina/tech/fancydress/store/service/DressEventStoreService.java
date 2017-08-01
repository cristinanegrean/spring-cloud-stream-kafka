package cristina.tech.fancydress.store.service;

import cristina.tech.fancydress.DressStatus;
import cristina.tech.fancydress.store.domain.Brand;
import cristina.tech.fancydress.store.domain.Dress;
import cristina.tech.fancydress.store.repository.BrandRepository;
import cristina.tech.fancydress.store.repository.DressRepository;
import cristina.tech.fancydress.worker.event.DressEventType;
import cristina.tech.fancydress.worker.event.DressMessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Transactional
public class DressEventStoreService {

    @Autowired
    private DressRepository dressRepository;

    @Autowired
    private BrandRepository brandRepository;

    /**
     * If producer sends 2 or more dress messages with status 'CREATED' and same dress id, data integrity check is
     * enforced at the database level and subsequent messages (after 1st received and stored) will be rejected with error.
     *
     * @param dressMessageEvent any message comming from the idresses channel
     */
    @Transactional
    public void apply(DressMessageEvent dressMessageEvent) {
        Dress dress = fromDressMessageEvent(
                dressMessageEvent, dressMessageEvent.getEventType() == DressEventType.CREATED);

        if (dress != null) {
            dressRepository.save(dress);
        }
    }

    @Transactional
    private Dress fromDressMessageEvent(DressMessageEvent dressMessageEvent, boolean isCreate) {
        if (dressMessageEvent == null || dressMessageEvent.getPayloadKey() == null) {
            return null;
        }

        Dress dress;
        if (isCreate) {
            dress = new Dress(dressMessageEvent.getPayloadKey());
        } else {
            // find dress to update
            Optional<Dress> dressOptional = dressRepository.findById(dressMessageEvent.getPayloadKey());
            dress = dressOptional.isPresent() ? dressOptional.get() : new Dress(dressMessageEvent.getPayloadKey());
            dress.setStatus(DressStatus.UPDATED);
        }

        dress.setName(dressMessageEvent.getPayload().getName());
        dress.setSeason(dressMessageEvent.getPayload().getSeason());
        dress.setPrice(dressMessageEvent.getPayload().getPrice());
        dress.setColor(dressMessageEvent.getPayload().getColor());
        dress.setBrand(fromDressMessageEvent(dressMessageEvent.getPayload().getBrand()));

        // any image thumbnails?
        if (dressMessageEvent.getPayload().getImages() != null) {
            dressMessageEvent.getPayload().getImages().forEach(
                    i -> {
                        if (dress.getThumbnails() == null) { // init
                            dress.setThumbnails(new ArrayList<>());
                        } else if (!isCreate && dress.getThumbnails() != null && dress.getThumbnails().size() > 0) {
                            // when updating dress, current message thumbs will overwrite old ones
                            dress.getThumbnails().clear();
                        }

                        dress.getThumbnails().add(i.getThumbUrl());
                    });
        }

        return dress;
    }

    @Transactional
    private Brand fromDressMessageEvent(cristina.tech.fancydress.worker.domain.Brand eventBrand) {
        if (eventBrand == null) {
            return null;
        }

        // does brand already exist?
        Optional<Brand> brandOptional = brandRepository.findByName(eventBrand.getName());
        if (brandOptional.isPresent()) {
            return brandOptional.get(); // found one by unique name
        } else {
            return brandRepository.save(new Brand(eventBrand.getName(), eventBrand.getLogoUrl()));
        }
    }

}
