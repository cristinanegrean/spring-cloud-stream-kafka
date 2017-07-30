package cristina.tech;


import cristina.tech.fancydress.FancyDressApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = FancyDressApplication.class)
@ActiveProfiles("test")
@WebAppConfiguration
public class FancyDressApplicationTest {

    @Test
    public void contextLoads() { //test that application context is initialized correctly by Spring Boot
    }

}
