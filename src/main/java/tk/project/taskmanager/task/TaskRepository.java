package tk.project.taskmanager.task;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    @Query(value = """
            select t
            from Task t
            left join fetch t.user
            """)
    List<Task> findAllJoinFetchUser(Pageable pageable);
}
