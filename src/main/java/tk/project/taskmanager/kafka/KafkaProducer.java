package tk.project.taskmanager.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import tk.project.taskmanager.kafka.event.EventSource;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@ConditionalOnProperty(prefix = "app", name = "kafka.enabled", matchIfMissing = false)
public class KafkaProducer {
    private final KafkaTemplate<String, EventSource> kafkaTemplateByteArray;
    public static final String TASK_NOTIFICATIONS_TOPIC = "task-notifications-topic";

    public KafkaProducer(@Autowired KafkaTemplate<String, EventSource> kafkaTemplateByteArray) {
        this.kafkaTemplateByteArray = kafkaTemplateByteArray;
    }

    public void sendEvent(final String topic, final String key, final EventSource event) {
        Assert.hasText(topic, "Topic must not be blank");
        Assert.hasText(key, "Key must not be blank");
        Assert.notNull(event, "KafkaEvent must not be null");

        try {
            kafkaTemplateByteArray.send(topic, key, event).get(1L, TimeUnit.MINUTES);
            log.info("Kafka send completely");

        } catch (Exception e) {
            log.warn("Kafka fail send: {}", e.getMessage());
        }
    }
}