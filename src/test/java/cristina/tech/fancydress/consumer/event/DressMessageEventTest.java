package cristina.tech.fancydress.consumer.event;

import cristina.tech.fancydress.DressStatus;
import cristina.tech.fancydress.consumer.event.ConsumerEventTypes.DressMessageEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.ActiveProfiles;


import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * To test the JSON slice of {@link ConsumerEventTypes.DressMessageEvent}, as of testing that JSON serialization and deserialization is working as expected,
 * I will use @JsonTest annotation.
 * <ul>This will:
 * <li>Auto-configure Jackson or Gson</li>
 * <li>Add any Module or @JsonComponent beans that you’ve defined</li>
 * <li>Trigger initialization of any JacksonTester or GsonTester fields</li>
 * </ul>
 * <p>
 * {@link org.springframework.test.context.junit4.SpringRunner} tells JUnit to run using Spring’s testing support. SpringRunner is the new name for SpringJUnit4ClassRunner
 */
@JsonTest
@ActiveProfiles("test")
public class DressMessageEventTest {

    @Autowired
    private JacksonTester<DressMessageEvent> json;

    @Test
    public void deserializeDressMessageStatusCreated() throws Exception {
        // deserialize JSON to DressMessageEvent POJO
        DressMessageEvent dressMessageEvent = json.readObject("dress_created.json");

        assertThat(dressMessageEvent).isNotNull();
        assertThat(dressMessageEvent.payloadKey()).isEqualTo("AX821CA1M-Q11");
        assertThat(dressMessageEvent.status()).isEqualTo(DressStatus.CREATED);
        assertThat(dressMessageEvent.getEventType()).isEqualTo(DressEventType.CREATED);

        assertThat(dressMessageEvent.payload()).isNotNull();
        assertThat(dressMessageEvent.payload().id()).isEqualTo(dressMessageEvent.payloadKey());
        assertThat(dressMessageEvent.payload().color()).isEqualTo("Black");
        assertThat(dressMessageEvent.payload().name()).isEqualTo("Jersey dress - black");
        assertThat(dressMessageEvent.payload().price()).isEqualTo(BigDecimal.valueOf(24.04));
        assertThat(dressMessageEvent.payload().season()).isEqualTo("WINTER");
        assertThat(dressMessageEvent.payload().brand()).isNotNull();
        assertThat(dressMessageEvent.payload().brand().name()).isEqualTo("Anna Field Curvy");
        assertThat(dressMessageEvent.payload().brand().logoUrl()).isEqualTo("https://i3.ztat.net/brand/9b3cabce-c405-44d7-a62f-ee00d5245962.jpg");
        assertThat(dressMessageEvent.payload().images()).hasSize(2);
        assertThat(dressMessageEvent.timestamp()).isEqualTo(1487593122542L);
    }

    @Test
    public void deserializeDressMessageStatusUpdated() throws Exception {
        // deserialize JSON to DressMessageEvent POJO
        DressMessageEvent dressMessageEvent = json.readObject("dress_updated.json");

        assertThat(dressMessageEvent).isNotNull();
        assertThat(dressMessageEvent.payloadKey()).isEqualTo("A0M21C000-Q11");
        assertThat(dressMessageEvent.status()).isEqualTo(DressStatus.UPDATED);
        assertThat(dressMessageEvent.getEventType()).isEqualTo(DressEventType.UPDATED);

        assertThat(dressMessageEvent.payload()).isNotNull();
        assertThat(dressMessageEvent.payload().id()).isEqualTo(dressMessageEvent.payloadKey());
        assertThat(dressMessageEvent.payload().color()).isEqualTo("Black");
        assertThat(dressMessageEvent.payload().name()).isEqualTo("SPOLETO - Summer dress - caviar");
        assertThat(dressMessageEvent.payload().price()).isEqualTo(BigDecimal.valueOf(35.99));
        assertThat(dressMessageEvent.payload().season()).isEqualTo("WINTER");
        assertThat(dressMessageEvent.payload().brand()).isNotNull();
        assertThat(dressMessageEvent.payload().brand().name()).isEqualTo("And Less");
        assertThat(dressMessageEvent.payload().brand().logoUrl()).isEqualTo("https://i4.ztat.net/brand/e69a6da2-4a38-424b-a8a4-22e3031eda66.jpg");
        assertThat(dressMessageEvent.payload().images()).hasSize(1);
        assertThat(dressMessageEvent.timestamp()).isEqualTo(1487593122542L);
    }


}
