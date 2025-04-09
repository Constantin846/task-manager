package tk.project.taskmanager.kafka;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import tk.project.taskmanager.kafka.event.EventSource;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app.kafka", name = "enabled", matchIfMissing = false)
public class KafkaProducer {
    private final KafkaTemplate<String, EventSource> kafkaTemplateEventSource;
    @Getter
    public String taskNotificationsTopic = "task-notifications-topic";

    public void sendEvent(final String topic, final String key, final EventSource event) {
        Assert.hasText(topic, "Topic must not be blank");
        Assert.hasText(key, "Key must not be blank");
        Assert.notNull(event, "KafkaEvent must not be null");

        try {
            kafkaTemplateEventSource.send(topic, key, event);
            log.info("Kafka send completely");

        } catch (Exception e) {
            log.warn("Kafka fail send: {}", e.getMessage());
        }
    }
}