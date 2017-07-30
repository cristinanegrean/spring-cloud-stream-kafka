package cristina.tech.worker.event;

import cristina.tech.web.BrandRepository;
import cristina.tech.web.DressRepository;
import cristina.tech.web.domain.Brand;
import cristina.tech.web.domain.Dress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Transactional
public class DressEventService {

    @Autowired
    private DressRepository dressRepository;
    @Autowired
    private BrandRepository brandRepository;

    /**
     * If producer sends 2 or more dress messages with status 'CREATED' and same dress id, data integrity check is
     * enforced at the database level and subsequent messages (after 1st received and stored) will be rejected with error.
     *
     * @param dressMessageEvent any message comming from the dresses channel
     */
    @Transactional
    public void apply(DressMessageEvent dressMessageEvent) {
        if (dressMessageEvent == null || dressMessageEvent.getPayloadKey() == null) {
            return;
        }

        if (dressMessageEvent.getEventType() == DressEventType.CREATED) {
            Dress dress = buildFromDressMessageEvent(dressMessageEvent);
            if (dress != null) {
                dressRepository.save(dress);
            }
        }
    }

    @Transactional
    private Dress buildFromDressMessageEvent(DressMessageEvent dressMessageEvent) {
        if (dressMessageEvent == null) {
            return null;
        }
        Dress dress = new Dress();
        dress.setId(dressMessageEvent.getPayloadKey());
        dress.setName(dressMessageEvent.getPayload().getName());
        dress.setSeason(dressMessageEvent.getPayload().getSeason());
        dress.setPrice(dressMessageEvent.getPayload().getPrice());
        dress.setColor(dressMessageEvent.getPayload().getColor());

        // any brand info?
        dress.setBrand(buildBrandFromDressMessageEvent(dressMessageEvent.getPayload().getBrand()));

        // any image thumbnails?
        if (dressMessageEvent.getPayload().getImages() != null) {
            dressMessageEvent.getPayload().getImages().forEach(
                    i -> {
                        if (dress.getThumbnails() == null) {
                            dress.setThumbnails(new ArrayList<>());
                        }
                        dress.getThumbnails().add(i.getThumbUrl());
                    });
        }

        return dress;
    }

    @Transactional
    private Brand buildBrandFromDressMessageEvent(cristina.tech.worker.domain.Brand eventBrand) {
        Brand brand = null;
        if (eventBrand == null) {
            return brand;
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
