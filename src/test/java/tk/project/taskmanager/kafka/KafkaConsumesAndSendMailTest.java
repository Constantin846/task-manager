package tk.project.taskmanager.kafka;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import tk.project.taskmanager.TaskManagerTest;
import tk.project.taskmanager.task.kafka.dto.ChangeTaskStatusDto;
import tk.project.taskmanager.task.kafka.dto.ChangeTaskStatusEvent;
import tk.project.taskmanager.task.model.TaskStatus;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

class KafkaConsumesAndSendMailTest extends TaskManagerTest {
    @Value("${app.recipient.mail}")
    private String defaultRecipientMail;
    @MockBean
    private JavaMailSender mailSender;

    @Test
    @DisplayName("Receive massage of changing task status and send mail successfully")
    void testKafkaReceivesMsgAndNotificationSendsEmail() {
        UUID taskId = UUID.randomUUID();
        TaskStatus taskStatus = TaskStatus.DONE;
        ChangeTaskStatusEvent event = new ChangeTaskStatusEvent();
        event.setChangeTaskStatusDto(new ChangeTaskStatusDto(taskId, taskStatus));

        ArgumentCaptor<SimpleMailMessage> captorMsg = ArgumentCaptor.forClass(SimpleMailMessage.class);

        testSendEventsToTopic(event);

        Awaitility.await()
                .atMost(10, TimeUnit.SECONDS)
                .pollInterval(2, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        verify(mailSender).send(captorMsg.capture()));

        SimpleMailMessage simpleMailMessage = captorMsg.getValue();
        String actualTextMessage = simpleMailMessage.getText();
        String[] actualMailTo = simpleMailMessage.getTo();

        assertTrue(actualTextMessage.contains(taskId.toString()));
        assertTrue(actualTextMessage.contains(taskStatus.name()));
        assertThat(actualMailTo).contains(defaultRecipientMail);
    }
}