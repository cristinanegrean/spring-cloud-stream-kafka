package cristina.tech.fancydress.store.controller;

import cristina.tech.fancydress.store.service.TrendingDressesService;
import cristina.tech.fancydress.store.view.DressDetailView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@Slf4j
public class TrendingRestController {
    
    private static final String INTERVAL_DEFAULT = "30 second";
    private static final String COUNT_DEFAULT = "50";
    private static final String COUNT_ERROR_MESSAGE = "Trending list count param should be min 1 or max 50";
    private static final String INTERVAL_ERROR_MESSAGE = "The interval you provided has invalid format! Examples of valid formats: '30second', '20day', '1minute', '10hour', '3week', '1month', '1year'. ";
    private static final String REGEX_INTERVAL =
            "\\d+ ?(milliseconds|second|minute|hour|day|week|month|year)";

    @Autowired
    private TrendingDressesService trendingDressesService;

    @RequestMapping(path = "/trending", method = RequestMethod.GET)
    public List<DressDetailView> trending(
            @RequestParam(value="count", defaultValue=COUNT_DEFAULT) Integer count,
            @RequestParam(value="interval", defaultValue=INTERVAL_DEFAULT) String interval) {

        log.info("Get top {0} trending dresses detail over time window of {1}", count, interval);

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
