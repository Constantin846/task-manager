package tk.project.taskmanager.task.service;

import org.springframework.data.domain.Pageable;
import tk.project.taskmanager.task.dto.create.CreateTaskDto;
import tk.project.taskmanager.task.dto.find.FindTaskDto;
import tk.project.taskmanager.task.dto.update.UpdateTaskDto;

import java.util.List;
import java.util.UUID;

public interface TaskService {
    UUID create(CreateTaskDto taskDto);

    List<FindTaskDto> findAll(Pageable pageable);

    FindTaskDto findById(UUID taskId);

    UpdateTaskDto put(UpdateTaskDto taskDto);

    void deleteById(UUID taskId);
}
