package tk.project.taskmanager.task.kafka;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import tk.project.taskmanager.TaskManagerTest;
import tk.project.taskmanager.kafka.KafkaProducer;
import tk.project.taskmanager.task.kafka.dto.ChangeTaskStatusDto;
import tk.project.taskmanager.task.kafka.dto.ChangeTaskStatusEvent;
import tk.project.taskmanager.task.model.TaskStatus;

import java.util.UUID;

import static org.mockito.Mockito.verify;

class TaskKafkaProducerTest extends TaskManagerTest {
    @MockBean
    private KafkaProducer kafkaProducer;
    @Autowired
    private TaskKafkaProducer taskKafkaProducer;

    @Test
    @DisplayName("Send massage of changing task status successfully")
    void sendMsgChangeTaskStatus() {
        UUID taskId = UUID.randomUUID();
        TaskStatus taskStatus = TaskStatus.DONE;
        ChangeTaskStatusEvent event = new ChangeTaskStatusEvent();
        event.setChangeTaskStatusDto(new ChangeTaskStatusDto(taskId, taskStatus));

        taskKafkaProducer.sendMsgChangeTaskStatus(taskId, taskStatus);

        verify(kafkaProducer).sendEvent(kafkaProducer.getTaskNotificationsTopic(), taskId.toString(), event);
    }
}