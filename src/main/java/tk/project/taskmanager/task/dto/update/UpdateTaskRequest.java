package tk.project.taskmanager.task.dto.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateTaskRequest {

    @NotBlank(message = "Task title must be set")
    @Length(message = "Task title length must be between 1 and 64 characters inclusive", min = 1, max = 64)
    String title;

    @NotBlank(message = "Task description must be set")
    String description;

    @NotNull(message = "User id of task must be set")
    UUID userId;
}
