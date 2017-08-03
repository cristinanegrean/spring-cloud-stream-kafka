package cristina.tech.fancydress.store.service;

import cristina.tech.fancydress.store.controller.TrendingRestController;
import cristina.tech.fancydress.store.repository.DressRepository;
import cristina.tech.fancydress.store.view.TrendingDressView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
public class TrendingDressesService {

    private static final int MAX_COUNT = 50;
    private static final String COUNT_ERROR_MESSAGE = "Trending list count param should be min 1 or max 50";
    private final static DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private static final Logger LOGGER = LoggerFactory.getLogger(TrendingDressesService.class);

    /**
     * for now hardcode temporal unit and duration, should be maybe supplied as param or
     * refreshable config.
     */
    private static final Duration duration = Duration.of(1, ChronoUnit.HOURS);

    @Autowired
    private DressRepository dressRepository;

    @Transactional(readOnly = true)
    public List<TrendingDressView> getTrending(int count) {
        // assert on count
        if (count == 0 || count > MAX_COUNT) {
            throw new IllegalArgumentException(COUNT_ERROR_MESSAGE);
        }

        String startDate = LocalDateTime.now().minusNanos(duration.toNanos()).format(formatter);
        String endDate = LocalDateTime.now().format(formatter);

        LOGGER.info(String.format("getTrending, count %d, start date %s, end date %s", count, startDate, endDate));
        return dressRepository.findTopNTrendingByTimeWindow(
                startDate, endDate, count).stream().map(TrendingDressView::map).collect(toList());
    }
}
