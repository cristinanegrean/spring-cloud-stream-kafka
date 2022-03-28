package cristina.tech.fancydress.consumer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import cristina.tech.fancydress.DressStatus;

import java.math.BigDecimal;
import java.util.List;

public interface ConsumerDomainTypes {
    record Brand(@JsonProperty("logo_url") String logoUrl, String name) {}

    record Image(@JsonProperty("large_url") String largeUrl, @JsonProperty("thumb_url") String thumbUrl) {}

    record Rating(@JsonProperty("dress_id") String dressId, @JsonProperty("rating_id") String ratingId, Integer stars) {}

    record Dress(String id, String name, String color, String season, Brand brand, List<Image> images, @JsonIgnore DressStatus status, BigDecimal price) {}

}
