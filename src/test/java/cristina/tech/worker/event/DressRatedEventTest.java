package cristina.tech.worker.event;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * To test the JSON slice of {@link DressRatedEvent}, as of testing that JSON serialization and deserialization is working as expected,
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
public class DressRatedEventTest {
    @Autowired
    private JacksonTester<DressRatedEvent> json;

    @Test
    public void testDeserialize() throws Exception {
        // deserialize JSON to DressRatedEvent POJO
        DressRatedEvent dressRatedEvent = json.readObject("dress_rated.json");

        assertThat(dressRatedEvent).isNotNull();
        assertThat(dressRatedEvent.getPayloadKey()).isEqualTo("c29b98c2-00fb-4766-938e-9e511d5f5c55");
        assertThat(dressRatedEvent.getStatus()).isEqualTo(DressEventType.RATED);
        assertThat(dressRatedEvent.getPayload()).isNotNull();
        assertThat(dressRatedEvent.getPayload().getRatingId()).isEqualTo(dressRatedEvent.getPayloadKey());
        assertThat(dressRatedEvent.getPayload().getDressId()).isEqualTo("NM521C00M-Q11");
        assertThat(dressRatedEvent.getPayload().getStars()).isEqualTo(1);
    }

}
