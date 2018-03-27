package cristina.tech.fancydress.store.controller;

import cristina.tech.fancydress.store.service.TrendingDressesService;
import cristina.tech.fancydress.store.view.DressDetailView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class TrendingRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrendingRestController.class);
    private static final String INTERVAL_DEFAULT = "30 second";
    private static final String COUNT_DEFAULT = "50";
    private static final String COUNT_ERROR_MESSAGE = "Trending list count param should be min 1 or max 50";
    private static final String INTERVAL_ERROR_MESSAGE = "The interval you provided has invalid format! Examples of valid formats: '30second', '20day', '1minute', '10hour', '3week', '1month', '1year'. ";
    private static final String REGEX_INTERVAL =
            "\\d+ ?(milliseconds|second|minute|hour|day|week|month|year)";

    @Autowired
    private TrendingDressesService trendingDressesService;

    @RequestMapping(value = "/trending")
    public List<DressDetailView> trending(
            @RequestParam(value="count", defaultValue=COUNT_DEFAULT) Integer count,
            @RequestParam(value="interval", defaultValue=INTERVAL_DEFAULT) String interval) {

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(String.format("Get top %d trending dresses detail over time window of %s", count, interval));
        }

        // validate user input count
        if (count == 0 || count > Integer.valueOf(COUNT_DEFAULT)) {
            throw new InvalidTrendingCountException(COUNT_ERROR_MESSAGE);
        }
        // validate user input time window interval
        Pattern p = Pattern.compile(REGEX_INTERVAL);
        Matcher m = p.matcher(interval);
        if (! m.matches()) {
            throw new InvalidTrendingTimeWindowException(INTERVAL_ERROR_MESSAGE);
        }
        return trendingDressesService.getTrending(count, interval);
    }

    @ResponseStatus(value= HttpStatus.BAD_REQUEST, reason=COUNT_ERROR_MESSAGE)
    class InvalidTrendingCountException extends IllegalArgumentException {
        InvalidTrendingCountException(String message) {
            super(message);
        }
    }

    @ResponseStatus(value= HttpStatus.BAD_REQUEST, reason=INTERVAL_ERROR_MESSAGE)
    class InvalidTrendingTimeWindowException extends IllegalArgumentException {
        InvalidTrendingTimeWindowException(String message) {
            super(message);
        }
    }
}
