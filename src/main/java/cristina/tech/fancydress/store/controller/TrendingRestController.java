package cristina.tech.fancydress.store.controller;

import cristina.tech.fancydress.store.service.TrendingDressesService;
import cristina.tech.fancydress.store.view.DressDetailView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TrendingRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrendingRestController.class);

    @Autowired
    private TrendingDressesService trendingDressesService;

    @RequestMapping(value = "/trending")
    public List<DressDetailView> trending(@RequestParam(value="count", defaultValue="50") Integer count) {
        LOGGER.info(String.format("Get top %d trending dresses detail", count));
        return trendingDressesService.getTrending(count);
    }
}
