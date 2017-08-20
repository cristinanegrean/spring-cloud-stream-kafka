package cristina.tech.fancydress.store.view;

import cristina.tech.fancydress.store.controller.TrendingRestController;
import cristina.tech.fancydress.store.service.TrendingDressesService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TrendingRestController.class)
public class DressDetailViewTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TrendingDressesService trendingDressesService;

    private static final String TEST_JSON =
            "[{\"id\":\"NY221C002-C11\",\"price\":57.04,\"name\":\"Jumper dress - grey\",\"season\":\"WINTER\",\"color\":\"Grey\",\"averageRating\":3,\"brandName\":\"Native Youth\"}]";

    @Test
    public void testJsonView() throws Exception {
        DressDetailView dressDetailView = new DressDetailView();
        dressDetailView.setId("NY221C002-C11");

        dressDetailView.setName("Jumper dress - grey");
        dressDetailView.setSeason("WINTER");
        dressDetailView.setColor("Grey");
        dressDetailView.setPrice(new BigDecimal(57.04).setScale(2, BigDecimal.ROUND_CEILING));
        dressDetailView.setAverageRating((short) 3);
        dressDetailView.setBrandName("Native Youth");

        List<DressDetailView> dressDetailViews = new ArrayList<>(1);
        dressDetailViews.add(dressDetailView);

        given(this.trendingDressesService.getTrending(1, "30 second"))
                .willReturn(dressDetailViews);
        this.mvc.perform(get("/trending?count=1").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk()).andExpect(content().string(TEST_JSON));
    }

    @Test
    public void testMap() throws Exception {
        Object[] dressDetailViewRow = new Object[8];
        dressDetailViewRow[0] = "NY221C002-C11";
        dressDetailViewRow[1] = new BigInteger("11");
        dressDetailViewRow[2] = "Jumper dress - grey";
        dressDetailViewRow[3] = "WINTER";
        dressDetailViewRow[4] = "Grey";
        dressDetailViewRow[5] = new BigDecimal(57.04).setScale(2, BigDecimal.ROUND_CEILING);
        dressDetailViewRow[6] = (short) 3;
        dressDetailViewRow[7] = "Native Youth";

        DressDetailView dressDetailView = DressDetailView.map(dressDetailViewRow);
        assertNotNull(dressDetailView);
        assertThat(dressDetailView.getName()).isEqualTo("Jumper dress - grey");
    }
}
