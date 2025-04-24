package tk.project.taskmanager.task.dto.find;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import tk.project.taskmanager.task.model.TaskStatus;

import java.util.UUID;

@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class FindTaskDto {

    private UUID id;

    private String title;

    private String description;

    private UUID userId;

    private TaskStatus status;
}
