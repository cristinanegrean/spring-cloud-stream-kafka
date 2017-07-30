package cristina.tech.fancydress.worker.event;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * To test the JSON slice of {@link RatingMessageEvent}, as of testing that JSON serialization and deserialization is working as expected,
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
public class RatingMessageEventTest {
    @Autowired
    private JacksonTester<RatingMessageEvent> json;

    @Test
    public void testDeserialize() throws Exception {
        // deserialize JSON to RatingMessageEvent POJO
        RatingMessageEvent ratingMessageEvent = json.readObject("dress_rated.json");

        assertThat(ratingMessageEvent).isNotNull();
        assertThat(ratingMessageEvent.getPayloadKey()).isEqualTo("c29b98c2-00fb-4766-938e-9e511d5f5c55");
        assertThat(ratingMessageEvent.getEventType()).isEqualTo(DressEventType.RATED);
        assertThat(ratingMessageEvent.getPayload()).isNotNull();
        assertThat(ratingMessageEvent.getPayload().getRatingId()).isEqualTo(ratingMessageEvent.getPayloadKey());
        assertThat(ratingMessageEvent.getPayload().getDressId()).isEqualTo("NM521C00M-Q11");
        assertThat(ratingMessageEvent.getPayload().getStars()).isEqualTo(1);
    }

}
