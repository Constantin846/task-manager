package tk.project.taskmanager.task.dto.create;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateTaskDto {

    String title;

    String description;

    UUID userId;
}
