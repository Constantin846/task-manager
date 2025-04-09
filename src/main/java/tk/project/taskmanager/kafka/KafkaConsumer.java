package tk.project.taskmanager.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import tk.project.taskmanager.exceptions.EventHandlerNotFoundException;
import tk.project.taskmanager.kafka.event.Event;
import tk.project.taskmanager.kafka.event.EventSource;
import tk.project.taskmanager.kafka.handler.EventHandler;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app.kafka", name = "enabled", matchIfMissing = false)
public class KafkaConsumer {

    private final Map<Event, EventHandler<EventSource>> eventHandlers;

    @KafkaListener(topics = "${app.kafka.task-notifications-topic}",
                    containerFactory = "kafkaListenerContainerFactoryEventSource")
    public void listen(EventSource eventSource, Acknowledgment acknowledgment) {
        log.info("Receive message: {}", eventSource);

        if (!eventHandlers.containsKey(eventSource.getEvent())) {
            acknowledgment.acknowledge();
            String msg = String.format("Handler for eventsource not found for: %s", eventSource.getEvent());
            log.warn(msg);
            throw new EventHandlerNotFoundException(msg, eventSource.getEvent().name());
        }

        eventHandlers.get(eventSource.getEvent()).handleEvent(eventSource);
        acknowledgment.acknowledge();
    }
}
