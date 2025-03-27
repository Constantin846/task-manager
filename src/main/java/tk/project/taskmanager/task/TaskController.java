package tk.project.taskmanager.task;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tk.project.taskmanager.aspect.annotation.LogStartMethod;
import tk.project.taskmanager.task.dto.create.CreateTaskDto;
import tk.project.taskmanager.task.dto.create.CreateTaskRequest;
import tk.project.taskmanager.task.dto.find.FindTaskDto;
import tk.project.taskmanager.task.dto.find.FindTaskResponse;
import tk.project.taskmanager.task.dto.mapper.TaskDtoMapper;
import tk.project.taskmanager.task.dto.update.UpdateTaskDto;
import tk.project.taskmanager.task.dto.update.UpdateTaskRequest;
import tk.project.taskmanager.task.dto.update.UpdateTaskResponse;
import tk.project.taskmanager.task.service.TaskService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private static final String ID = "id";
    private static final String ID_PATH = "/{id}";
    private final TaskDtoMapper mapper;
    private final TaskService taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @LogStartMethod
    public Map<String, UUID> create(@Valid @RequestBody CreateTaskRequest taskRequest) {
        CreateTaskDto taskDto = mapper.toCreateTaskDto(taskRequest);
        UUID id = taskService.create(taskDto);
        return Map.of(ID, id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @LogStartMethod
    public Collection<FindTaskResponse> findAll(Pageable pageable) {
        List<FindTaskDto> tasks = taskService.findAll(pageable);
        return mapper.toFindTaskResponse(tasks);
    }

    @GetMapping(ID_PATH)
    @ResponseStatus(HttpStatus.OK)
    @LogStartMethod
    public FindTaskResponse findById(@PathVariable(ID) UUID taskId) {
        FindTaskDto taskDto = taskService.findById(taskId);
        return mapper.toFindTaskResponse(taskDto);
    }

    @PutMapping(ID_PATH)
    @ResponseStatus(HttpStatus.OK)
    @LogStartMethod
    public UpdateTaskResponse updateById(@PathVariable(ID) UUID taskId,
                                         @Valid @RequestBody UpdateTaskRequest taskRequest) {
        UpdateTaskDto taskDto = mapper.toUpdateTaskDto(taskRequest);
        taskDto.setId(taskId);
        UpdateTaskDto updated = taskService.put(taskDto);
        return mapper.toUpdateTaskResponse(updated);
    }

    @DeleteMapping(ID_PATH)
    @ResponseStatus(HttpStatus.OK)
    @LogStartMethod
    public void deleteById(@PathVariable(ID) UUID taskId) {
        taskService.deleteById(taskId);
    }
}
