package cristina.tech.fancydress.store.controller;

import cristina.tech.fancydress.BootifulDressApplication;
import cristina.tech.fancydress.store.view.DressDetailView;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = BootifulDressApplication.class)
public class TrendingRestControllerTests {

    @Autowired
    private TestRestTemplate template;

    @Test
    public void testRequestNoParams() throws Exception {
        HttpHeaders headers = template.getForEntity("/trending", DressDetailView.class).getHeaders();
        assertThat(headers.getContentType().toString()).isEqualTo(
                MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    @Test
    public void testRequestInvalidCount() throws Exception {
        HttpStatus status = template.getForEntity("/trending?count=51", DressDetailView.class).getStatusCode();
        assertThat(status).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testRequestInvalidTimeWindowInterval() throws Exception {
        HttpStatus status = template.getForEntity("/trending?count=50&interval=not an interval", DressDetailView.class).getStatusCode();
        assertThat(status).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    }
}
