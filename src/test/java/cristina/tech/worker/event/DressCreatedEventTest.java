package cristina.tech.worker.event;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * To test the JSON slice of {@link DressCreatedEvent}, as of testing that JSON serialization and deserialization is working as expected,
 * I will use @JsonTest annotation.
 * <ul>This will:
 * <li>Auto-configure Jackson or Gson</li>
 * <li>Add any Module or @JsonComponent beans that you’ve defined</li>
 * <li>Trigger initialization of any JacksonTester or GsonTester fields</li>
 * </ul>
 * <p>
 * {@link org.springframework.test.context.junit4.SpringRunner} tells JUnit to run using Spring’s testing support. SpringRunner is the new name for SpringJUnit4ClassRunner
 */
@RunWith(SpringRunner.class)
@JsonTest
@ActiveProfiles("test")
public class DressCreatedEventTest {

    @Autowired
    private JacksonTester<DressCreatedEvent> json;

    @Test
    public void testDeserialize() throws Exception {
        // deserialize JSON to DressCreatedEvent POJO
        DressCreatedEvent dressCreatedEvent = json.readObject("dress_created.json");

        assertThat(dressCreatedEvent).isNotNull();
        assertThat(dressCreatedEvent.getPayloadKey()).isEqualTo("AX821CA1M-Q11");
        assertThat(dressCreatedEvent.getPayload()).isNotNull();
        assertThat(dressCreatedEvent.getPayload().getId()).isEqualTo(dressCreatedEvent.getPayloadKey());
        assertThat(dressCreatedEvent.getPayload().getColor()).isEqualTo("Black");
        assertThat(dressCreatedEvent.getPayload().getName()).isEqualTo("Jersey dress - black");
        assertThat(dressCreatedEvent.getPayload().getPrice()).isEqualTo(BigDecimal.valueOf(24.04));
        assertThat(dressCreatedEvent.getPayload().getSeason()).isEqualTo("WINTER");
        assertThat(dressCreatedEvent.getPayload().getBrand()).isNotNull();
        assertThat(dressCreatedEvent.getPayload().getBrand().getName()).isEqualTo("Anna Field Curvy");
        assertThat(dressCreatedEvent.getPayload().getBrand().getLogoUrl()).isEqualTo("https://i3.ztat.net/brand/9b3cabce-c405-44d7-a62f-ee00d5245962.jpg");
        assertThat(dressCreatedEvent.getPayload().getImages()).hasSize(2);
    }

}
