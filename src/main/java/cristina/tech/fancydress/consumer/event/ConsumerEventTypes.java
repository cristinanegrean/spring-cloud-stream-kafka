package cristina.tech.fancydress.consumer.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import cristina.tech.fancydress.DressStatus;
import cristina.tech.fancydress.consumer.domain.ConsumerDomainTypes.Dress;
import cristina.tech.fancydress.consumer.domain.ConsumerDomainTypes.Rating;

public interface ConsumerEventTypes {

    record DressEvent(@JsonProperty("payload_key") String payloadKey, @JsonIgnore DressEventType eventType, Long timestamp) {}

    record DressMessageEvent(@JsonProperty("payload_key") String payloadKey, @JsonIgnore DressEventType eventType, Long timestamp, Dress payload, DressStatus status) {
        public DressEventType getEventType() {
            return (status == DressStatus.UPDATED ? DressEventType.UPDATED : DressEventType.CREATED);
        }
    }

    record RatingMessageEvent(@JsonProperty("payload_key") String payloadKey, @JsonIgnore DressEventType eventType, Long timestamp, Rating payload) {
        public DressEventType getEventType() {
            return DressEventType.RATED;
        }
    }
}
