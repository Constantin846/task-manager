package tk.project.taskmanager.task;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tk.project.taskmanager.task.dto.find.FindTaskDto;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    @Query(value = """
            select new tk.project.taskmanager.task.dto.find.FindTaskDto(t.id, t.title, t.description, t.user.id)
            from Task t
            """)
    List<FindTaskDto> findTaskDtoAll(Pageable pageable);
}
