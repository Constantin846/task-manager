package tk.project.taskmanager.exceptions;

import lombok.Getter;

@Getter
public class EventHandlerNotFoundException extends RuntimeException {
    private String eventName;

    public EventHandlerNotFoundException(String message) {
        super(message);
    }

    public EventHandlerNotFoundException(String message, String eventName) {
        super(message);
        this.eventName = eventName;
    }
}
