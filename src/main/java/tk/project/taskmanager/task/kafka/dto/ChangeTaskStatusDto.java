package tk.project.taskmanager.task.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tk.project.taskmanager.task.model.TaskStatus;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeTaskStatusDto {

    private UUID taskId;

    private TaskStatus status;
}
