package tk.project.taskmanager.kafka.handler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tk.project.taskmanager.kafka.event.Event;
import tk.project.taskmanager.kafka.event.EventSource;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class EventHandlerConfig {

    @Bean
    public <T extends EventSource> Map<Event, EventHandler<T>> eventHandlers(Set<EventHandler<T>> eventHandlers) {
        return eventHandlers.stream()
                .collect(Collectors.toMap(EventHandler::getEvent, Function.identity()));
    }
}