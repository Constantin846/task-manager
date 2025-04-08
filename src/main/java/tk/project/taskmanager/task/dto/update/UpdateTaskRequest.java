package tk.project.taskmanager.task.dto.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import tk.project.taskmanager.task.model.TaskStatus;

import java.util.UUID;

@Data
public class UpdateTaskRequest {

    @NotBlank(message = "Task title must be set")
    @Length(message = "Task title length must be between 1 and 64 characters inclusive", min = 1, max = 64)
    private String title;

    @NotBlank(message = "Task description must be set")
    private String description;

    @NotNull(message = "User id of task must be set")
    private UUID userId;

    private TaskStatus status = TaskStatus.CREATED;
}
