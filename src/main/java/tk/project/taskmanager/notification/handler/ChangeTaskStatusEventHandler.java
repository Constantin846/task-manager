package tk.project.taskmanager.notification.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tk.project.taskmanager.kafka.event.Event;
import tk.project.taskmanager.kafka.handler.EventHandler;
import tk.project.taskmanager.logging.aspect.annotation.LogCoverMethod;
import tk.project.taskmanager.notification.NotificationService;
import tk.project.taskmanager.task.kafka.dto.ChangeTaskStatusDto;
import tk.project.taskmanager.task.kafka.dto.ChangeTaskStatusEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChangeTaskStatusEventHandler implements EventHandler<ChangeTaskStatusEvent> {
    @Value("${app.recipient.mail}")
    private String defaultRecipientMail;
    private static final String SUBJECT = "Change task status";
    private static final String MSG_PATTERN = "Status of task with id=%s has been changed to %s";
    private static final Event EVENT = Event.CHANGE_TASK_STATUS;
    private final NotificationService notificationService;

    @Override
    public Event getEvent() {
        return EVENT;
    }

    @Override
    @LogCoverMethod
    public void handleEvent(ChangeTaskStatusEvent eventSource) {
        ChangeTaskStatusDto dto = eventSource.getChangeTaskStatusDto();
        String message = String.format(MSG_PATTERN, dto.getTaskId(), dto.getStatus());

        notificationService.sendMessage(defaultRecipientMail, SUBJECT, message);
    }
}
