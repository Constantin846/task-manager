package tk.project.taskmanager;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
class TestContainers {

    @Container
    public static final KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:6.1.1")
    );

    @DynamicPropertySource
    public static void registerKafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("app.kafka.bootstrap-address", kafka::getBootstrapServers);
    }
}
