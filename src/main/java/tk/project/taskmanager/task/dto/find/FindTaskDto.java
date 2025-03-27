package tk.project.taskmanager.task.dto.find;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FindTaskDto {

    UUID id;

    String title;

    String description;

    UUID userId;
}
