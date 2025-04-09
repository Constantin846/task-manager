package tk.project.taskmanager.kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import tk.project.taskmanager.kafka.event.EventSource;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConditionalOnProperty(prefix = "app.kafka", name = "enabled", matchIfMissing = false)
public class KafkaProducerConfig {
    @Value("${app.kafka.bootstrap-address}")
    private String bootstrapAddress;
    @Value("${app.kafka.task-notifications-topic}")
    private String taskNotificationsTopic;

    private ProducerFactory<String, EventSource> producerFactoryEventSource() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, false);
        configProps.put(ProducerConfig.ACKS_CONFIG, "1");
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, EventSource> kafkaTemplateEventSource() {
        return new KafkaTemplate<>(producerFactoryEventSource());
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic taskNotificationsTopic() {
        return new NewTopic(taskNotificationsTopic, 1, (short) 1);
    }
}
