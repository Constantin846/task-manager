package tk.project.taskmanager.kafka.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import tk.project.taskmanager.task.kafka.dto.ChangeTaskStatusEvent;

@JsonTypeInfo(use = JsonTypeInfo.Id.SIMPLE_NAME, include = JsonTypeInfo.As.PROPERTY,
        property = "event", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(name = "CHANGE_TASK_STATUS", value = ChangeTaskStatusEvent.class),
})
public interface EventSource {
    Event getEvent();
}
