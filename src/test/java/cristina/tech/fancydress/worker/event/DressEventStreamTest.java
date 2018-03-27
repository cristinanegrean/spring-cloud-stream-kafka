package cristina.tech.fancydress.worker.event;

import cristina.tech.fancydress.BootifulDressApplication;
import cristina.tech.fancydress.store.repository.BrandRepository;
import cristina.tech.fancydress.store.repository.DressRepository;
import cristina.tech.fancydress.store.service.DressEventStoreService;
import cristina.tech.fancydress.store.service.RatingEventStoreService;
import cristina.tech.fancydress.worker.domain.Brand;
import cristina.tech.fancydress.worker.domain.Dress;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.Mockito.*;


/**
 * Test in isolation {@link DressEventStream} by using Mockito.
 */
@RunWith(SpringRunner.class)
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
        DressMessageEvent dressMessageEvent = new DressMessageEvent();
        dressMessageEvent.setEventType(DressEventType.UPDATED);
        dressMessageEvent.setPayloadKey("dressy-ten");
        Dress dress = new Dress();
        dress.setId("dressy-ten");
        dress.setName("new-name");
        Brand brand = new Brand();
        brand.setName("revamped");
        dress.setBrand(brand);
        dressMessageEvent.setPayload(dress);

        cristina.tech.fancydress.store.domain.Brand storedBrand = new cristina.tech.fancydress.store.domain.Brand(brand.getName(), null);
        cristina.tech.fancydress.store.domain.Dress dressToBeUpdated = new cristina.tech.fancydress.store.domain.Dress(dress.getId(), storedBrand);
        when(dressRepository.findById(dressMessageEvent.getPayloadKey())).thenReturn(Optional.of(dressToBeUpdated));
        when(brandRepository.findByName(brand.getName())).thenReturn(Optional.of(storedBrand));
        when(dressEventStoreService.apply(dressMessageEvent)).thenReturn(true);

        dressEventStream.receiveDressMessageEvent(dressMessageEvent);

        verify(dressEventStoreService, times(1)).apply(dressMessageEvent);
    }

}
