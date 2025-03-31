package tk.project.taskmanager.task.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tk.project.taskmanager.aspect.annotation.LogCoverMethod;
import tk.project.taskmanager.aspect.annotation.LogException;
import tk.project.taskmanager.aspect.annotation.LogStartMethod;
import tk.project.taskmanager.aspect.annotation.MeasureExecutionTime;
import tk.project.taskmanager.exceptions.TaskNotFoundException;
import tk.project.taskmanager.exceptions.UserNotFoundException;
import tk.project.taskmanager.task.Task;
import tk.project.taskmanager.task.TaskRepository;
import tk.project.taskmanager.task.dto.create.CreateTaskDto;
import tk.project.taskmanager.task.dto.find.FindTaskDto;
import tk.project.taskmanager.task.dto.mapper.TaskDtoMapper;
import tk.project.taskmanager.task.dto.update.UpdateTaskDto;
import tk.project.taskmanager.user.User;
import tk.project.taskmanager.user.UserRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskDtoMapper mapper;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    @LogCoverMethod
    @MeasureExecutionTime
    public UUID create(CreateTaskDto taskDto) {
        Task task = mapper.toTask(taskDto);
        return saveTask(task, taskDto.getUserId());
    }

    @Override
    @LogStartMethod
    public List<FindTaskDto> findAll(Pageable pageable) {
        return mapper.toFindTaskDto(taskRepository.findAllJoinFetchUser(pageable));
    }

    @Override
    @LogStartMethod
    @LogException
    @MeasureExecutionTime
    public FindTaskDto findById(UUID taskId) {
        Task task = getTaskById(taskId);
        return mapper.toFindTaskDto(task);
    }

    @Transactional
    @Override
    @LogCoverMethod
    @MeasureExecutionTime
    public UpdateTaskDto put(UpdateTaskDto taskDto) {
        Task task = mapper.toTask(taskDto);
        UUID id = saveTask(task, taskDto.getUserId());
        return mapper.toUpdateTaskDto(getTaskById(id));
    }

    @Transactional
    @Override
    @LogCoverMethod
    @MeasureExecutionTime
    public void deleteById(UUID taskId) {
        taskRepository.deleteById(taskId);
    }

    private UUID saveTask(Task task, UUID userId) {
        User user = getUserById(userId);
        task.setUser(user);
        task = taskRepository.save(task);
        return task.getId();
    }

    private Task getTaskById(UUID taskId) {
        return taskRepository.findById(taskId).orElseThrow(() ->
                new TaskNotFoundException(String.format("Task was not found by id: %s", taskId)));
    }

    private User getUserById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(String.format("User was not found by id: %s", userId)));
    }
}
