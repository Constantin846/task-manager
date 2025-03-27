package tk.project.taskmanager.task.dto.find;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FindTaskResponse {

    UUID id;

    String title;

    String description;

    UUID userId;
}
