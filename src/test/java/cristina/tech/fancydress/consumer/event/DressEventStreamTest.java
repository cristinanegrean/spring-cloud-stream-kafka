package cristina.tech.fancydress.consumer.event;

import cristina.tech.fancydress.BootifulDressApplication;
import cristina.tech.fancydress.DressStatus;
import cristina.tech.fancydress.store.repository.BrandRepository;
import cristina.tech.fancydress.store.repository.DressRepository;
import cristina.tech.fancydress.store.service.DressEventStoreService;
import cristina.tech.fancydress.store.service.RatingEventStoreService;
import cristina.tech.fancydress.consumer.domain.ConsumerDomainTypes.Brand;
import cristina.tech.fancydress.consumer.domain.ConsumerDomainTypes.Dress;
import cristina.tech.fancydress.consumer.event.ConsumerEventTypes.DressMessageEvent;
import cristina.tech.fancydress.consumer.event.ConsumerEventTypes.RatingMessageEvent;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;


/**
 * Test in isolation {@link DressEventStream} by using Mockito.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = BootifulDressApplication.class)
public class DressEventStreamTest {

    @MockBean
    private DressEventStoreService dressEventStoreService;

    @MockBean
    private RatingEventStoreService ratingEventStoreService;

    @MockBean
    private DressRepository dressRepository;

    @MockBean
    private BrandRepository brandRepository;

    @Autowired
    private DressEventStream dressEventStream;

    @Test
    public void testReceiveDressMessageEventNoApply() {
        DressMessageEvent dressMessageEvent = mock(DressMessageEvent.class);
        when(dressEventStoreService.apply(dressMessageEvent)).thenReturn(false);
        dressEventStream.receiveDressMessageEvent(dressMessageEvent);
        verify(dressEventStoreService, times(1)).apply(dressMessageEvent);
    }

    @Test
    public void testReceiveRatingMessageEventNoApply() {
        RatingMessageEvent ratingMessageEvent = mock(RatingMessageEvent.class);
        when(ratingEventStoreService.apply(ratingMessageEvent)).thenReturn(false);
        dressEventStream.receiveRatingMessageEvent(ratingMessageEvent);

        verify(ratingEventStoreService, times(1)).apply(ratingMessageEvent);
    }

    @Test
    public void testReceiveDressMessageEventSuccessfulApply() {
        Brand brand = new Brand("revamped");
        Dress dress = new Dress("dressy-ten", "new-name", brand);

        DressMessageEvent dressMessageEvent = new DressMessageEvent("dressy-ten", DressEventType.UPDATED,
                Instant.now().toEpochMilli(), dress, DressStatus.UPDATED);


        cristina.tech.fancydress.store.domain.Brand storedBrand = new cristina.tech.fancydress.store.domain.Brand(brand.name(), null);
        cristina.tech.fancydress.store.domain.Dress dressToBeUpdated = new cristina.tech.fancydress.store.domain.Dress(dress.id(), storedBrand);
        when(dressRepository.findById(dressMessageEvent.payloadKey())).thenReturn(Optional.of(dressToBeUpdated));
        when(brandRepository.findByName(brand.name())).thenReturn(Optional.of(storedBrand));
        when(dressEventStoreService.apply(dressMessageEvent)).thenReturn(true);

        dressEventStream.receiveDressMessageEvent(dressMessageEvent);

        verify(dressEventStoreService, times(1)).apply(dressMessageEvent);
    }

}
