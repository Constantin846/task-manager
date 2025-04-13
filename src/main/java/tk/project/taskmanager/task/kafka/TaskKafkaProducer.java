package tk.project.taskmanager.task.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.project.taskmanager.kafka.KafkaProducer;
import tk.project.taskmanager.logging.aspect.annotation.LogCoverMethod;
import tk.project.taskmanager.task.kafka.dto.ChangeTaskStatusDto;
import tk.project.taskmanager.task.kafka.dto.ChangeTaskStatusEvent;
import tk.project.taskmanager.task.model.TaskStatus;

import java.util.Objects;
import java.util.UUID;

@Component
public class TaskKafkaProducer {
    private final KafkaProducer kafkaProducer;

    public TaskKafkaProducer(@Autowired(required = false) KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @LogCoverMethod
    public void sendMsgChangeTaskStatus(UUID taskId, TaskStatus status) {
        if (Objects.isNull(kafkaProducer)) return;

        ChangeTaskStatusDto dto = new ChangeTaskStatusDto(taskId, status);
        ChangeTaskStatusEvent event = new ChangeTaskStatusEvent();
        event.setChangeTaskStatusDto(dto);

        kafkaProducer.sendEvent(kafkaProducer.getTaskNotificationsTopic(), taskId.toString(), event);
    }
}
