package tk.project.taskmanager.task.dto.update;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateTaskDto {

    UUID id;

    String title;

    String description;

    UUID userId;
}
