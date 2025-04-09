package tk.project.taskmanager.notification;

public interface NotificationService {
    void sendMessage(String to, String subject, String message);
}
