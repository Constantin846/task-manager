package tk.project.taskmanager.task.dto.find;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
public class FindTaskDto {

    private UUID id;

    private String title;

    private String description;

    private UUID userId;
}
