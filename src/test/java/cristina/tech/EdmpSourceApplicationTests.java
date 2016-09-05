package cristina.tech;


import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = EdmpSourceApplication.class)
@WebAppConfiguration
public class EdmpSourceApplicationTests {

	@Test
    @Ignore
	public void contextLoads() {
	}

}
