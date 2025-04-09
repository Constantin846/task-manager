package tk.project.taskmanager.kafka.handler;

import tk.project.taskmanager.kafka.event.Event;
import tk.project.taskmanager.kafka.event.EventSource;

public interface EventHandler<T extends EventSource> {

    Event getEvent();

    void handleEvent(T eventSource);
}
