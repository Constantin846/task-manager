package tk.project.taskmanager.task.dto.create;

import lombok.Data;
import tk.project.taskmanager.task.model.TaskStatus;

import java.util.UUID;

@Data
public class CreateTaskDto {

    private String title;

    private String description;

    private UUID userId;

    private TaskStatus status;
}
