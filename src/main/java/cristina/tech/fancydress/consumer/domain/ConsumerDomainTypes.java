package cristina.tech.fancydress.consumer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import cristina.tech.fancydress.DressStatus;
import org.apache.kafka.common.protocol.types.Field;

import java.math.BigDecimal;
import java.util.List;

public interface ConsumerDomainTypes {
    record Brand(@JsonProperty("logo_url") String logoUrl, String name) {
        public Brand(String name) {
            this(null, name);
        }
    }

    record Image(@JsonProperty("large_url") String largeUrl, @JsonProperty("thumb_url") String thumbUrl) {}

    record Rating(@JsonProperty("dress_id") String dressId, @JsonProperty("rating_id") String ratingId, Integer stars) {}

    record Dress(String id, String name, String color, String season, Brand brand, List<Image> images, @JsonIgnore DressStatus status, BigDecimal price) {
        public Dress(String id, String name, Brand brand) {
            this(id, name, null, null, brand);
        }

        public Dress(String id, String name, String color, String season, Brand brand) {
            this(id, name, color, season, brand, null, null, null);
        }
    }

}
