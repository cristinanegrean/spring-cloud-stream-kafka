package cristina.tech;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;


/**
 * Consuming events. Sink: consumes from a Source {@link EdmpSourceApplication} and writes the data to the desired persistence layer.
 * When using @EnableBinding(Sink.class) Spring Cloud Stream automatically creates a message channel with the name input which is used by the @StreamListener.
 */
@EnableBinding(Sink.class)
public class EdmpSinkApplication {
    private static Logger logger = LoggerFactory.getLogger(EdmpSinkApplication.class);

    @StreamListener(Sink.INPUT)
    public void loggerSink(SinkTimedMessage sinkTimedMessage) {
        logger.info("Received: " + sinkTimedMessage.toString());
    }

    private static class SinkTimedMessage {

        private String time;
        private String message;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getMessage() {
            return this.message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return "SinkTimedMessage{" +
                    "time='" + time + '\'' +
                    ", message='" + message + '\'' +
                    '}';
        }
    }

}
