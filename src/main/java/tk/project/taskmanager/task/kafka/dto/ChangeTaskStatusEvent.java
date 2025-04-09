package tk.project.taskmanager.task.kafka.dto;

import lombok.Data;
import tk.project.taskmanager.kafka.event.Event;
import tk.project.taskmanager.kafka.event.EventSource;

@Data
public class ChangeTaskStatusEvent implements EventSource {

    private Event event = Event.CHANGE_TASK_STATUS;

    private ChangeTaskStatusDto changeTaskStatusDto;
}
