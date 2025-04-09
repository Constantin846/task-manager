package tk.project.taskmanager.task.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tk.project.taskmanager.aspect.annotation.LogCoverMethod;
import tk.project.taskmanager.kafka.KafkaProducer;
import tk.project.taskmanager.task.kafka.dto.ChangeTaskStatusDto;
import tk.project.taskmanager.task.kafka.dto.ChangeTaskStatusEvent;
import tk.project.taskmanager.task.model.TaskStatus;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TaskKafkaProducer {
    private final KafkaProducer kafkaProducer;

    @LogCoverMethod
    public void sendMsgChangeTaskStatus(UUID taskId, TaskStatus status) {
        ChangeTaskStatusDto dto = new ChangeTaskStatusDto(taskId, status);
        ChangeTaskStatusEvent event = new ChangeTaskStatusEvent();
        event.setChangeTaskStatusDto(dto);

        kafkaProducer.sendEvent(kafkaProducer.getTaskNotificationsTopic(), taskId.toString(), event);
    }
}
