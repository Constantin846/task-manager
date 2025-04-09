package tk.project.taskmanager.task.dto.find;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tk.project.taskmanager.task.model.TaskStatus;

import java.util.UUID;

@Data
@EqualsAndHashCode(of = "id")
public class FindTaskResponse {

    private UUID id;

    private String title;

    private String description;

    private UUID userId;

    private TaskStatus status;
}
