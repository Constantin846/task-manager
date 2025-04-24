package tk.project.taskmanager.task.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import tk.project.taskmanager.exceptions.TaskNotFoundException;
import tk.project.taskmanager.exceptions.UserNotFoundException;
import tk.project.taskmanager.task.TaskRepository;
import tk.project.taskmanager.task.dto.create.CreateTaskDto;
import tk.project.taskmanager.task.dto.find.FindTaskDto;
import tk.project.taskmanager.task.dto.mapper.TaskDtoMapper;
import tk.project.taskmanager.task.dto.update.UpdateTaskDto;
import tk.project.taskmanager.task.kafka.TaskKafkaProducer;
import tk.project.taskmanager.task.model.Task;
import tk.project.taskmanager.task.model.TaskStatus;
import tk.project.taskmanager.user.User;
import tk.project.taskmanager.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {
    @Spy
    private TaskDtoMapper mapper = TaskDtoMapper.MAPPER;
    @Mock
    private TaskKafkaProducer taskKafkaProducer;
    @Mock
    private TaskRepository taskRepositoryMock;
    @Mock
    private UserRepository userRepositoryMock;
    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    @DisplayName("Create task successfully")
    void createTask() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);

        CreateTaskDto createTaskDto = new CreateTaskDto();
        createTaskDto.setUserId(userId);

        Task task = new Task();
        task.setUser(user);

        Task expectedTask = new Task();
        expectedTask.setId(UUID.randomUUID());
        expectedTask.setUser(user);

        when(userRepositoryMock.findById(userId)).thenReturn(Optional.of(user));
        when(taskRepositoryMock.save(task)).thenReturn(expectedTask);

        UUID actualTaskId = taskService.create(createTaskDto);

        assertEquals(expectedTask.getId(), actualTaskId);
    }

    @Test
    @DisplayName("Failed to create task because user not found")
    void createTaskFailedWithNotFoundUserException() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);

        CreateTaskDto createTaskDto = new CreateTaskDto();
        createTaskDto.setUserId(userId);

        when(userRepositoryMock.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> taskService.create(createTaskDto));
    }

    @Test
    @DisplayName("Find all tasks successfully")
    void findAllTasks() {
        FindTaskDto expectedTaskDto = new FindTaskDto();
        expectedTaskDto.setId(UUID.randomUUID());
        expectedTaskDto.setTitle("title");
        expectedTaskDto.setDescription("description");
        expectedTaskDto.setStatus(TaskStatus.CREATED);

        Task task = new Task();
        task.setId(expectedTaskDto.getId());
        task.setTitle(expectedTaskDto.getTitle());
        task.setDescription(expectedTaskDto.getDescription());
        task.setStatus(expectedTaskDto.getStatus());

        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        Pageable pageable = PageRequest.of(0, 10);

        when(taskRepositoryMock.findAllJoinFetchUser(pageable)).thenReturn(tasks);

        List<FindTaskDto> actualTasks = taskService.findAll(pageable);
        FindTaskDto actualTaskDto = actualTasks.get(0);

        assertEquals(expectedTaskDto.getId(), actualTaskDto.getId());
        assertEquals(expectedTaskDto.getTitle(), actualTaskDto.getTitle());
        assertEquals(expectedTaskDto.getDescription(), actualTaskDto.getDescription());
        assertEquals(expectedTaskDto.getStatus(), actualTaskDto.getStatus());
    }

    @Test
    @DisplayName("Find task by id successfully")
    void findTaskById() {
        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setTitle("title");
        task.setDescription("description");
        task.setStatus(TaskStatus.CREATED);

        when(taskRepositoryMock.findById(task.getId())).thenReturn(Optional.of(task));

        FindTaskDto actualTaskDto = taskService.findById(task.getId());

        assertEquals(task.getId(), actualTaskDto.getId());
        assertEquals(task.getTitle(), actualTaskDto.getTitle());
        assertEquals(task.getDescription(), actualTaskDto.getDescription());
        assertEquals(task.getStatus(), actualTaskDto.getStatus());
    }

    @Test
    @DisplayName("Task was not found by id")
    void findTaskByIdFailed() {
        Task task = new Task();
        task.setId(UUID.randomUUID());

        when(taskRepositoryMock.findById(task.getId())).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.findById(task.getId()));
    }

    @Test
    @DisplayName("Put task successfully")
    void putTask() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);

        UpdateTaskDto expectedTaskDto = new UpdateTaskDto();
        expectedTaskDto.setId(UUID.randomUUID());
        expectedTaskDto.setTitle("title");
        expectedTaskDto.setDescription("description");
        expectedTaskDto.setUserId(userId);
        expectedTaskDto.setStatus(TaskStatus.CREATED);

        Task task = new Task();
        task.setId(expectedTaskDto.getId());
        task.setTitle(expectedTaskDto.getTitle());
        task.setDescription(expectedTaskDto.getDescription());
        task.setUser(user);
        task.setStatus(expectedTaskDto.getStatus());

        when(taskRepositoryMock.findTaskStatusById(expectedTaskDto.getId())).thenReturn(Optional.of(TaskStatus.AWAIT));
        when(userRepositoryMock.findById(userId)).thenReturn(Optional.of(user));
        when(taskRepositoryMock.findById(expectedTaskDto.getId())).thenReturn(Optional.of(task));
        when(taskRepositoryMock.save(task)).thenReturn(task);

        UpdateTaskDto actualTaskDto = taskService.put(expectedTaskDto);

        assertEquals(expectedTaskDto.getId(), actualTaskDto.getId());
        assertEquals(expectedTaskDto.getTitle(), actualTaskDto.getTitle());
        assertEquals(expectedTaskDto.getDescription(), actualTaskDto.getDescription());
        assertEquals(expectedTaskDto.getUserId(), actualTaskDto.getUserId());
        assertEquals(expectedTaskDto.getStatus(), actualTaskDto.getStatus());
    }

    @Test
    @DisplayName("Failed to put task because user not found")
    void putTaskFailedWithNotFoundUserException() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);

        UpdateTaskDto updateTaskDto = new UpdateTaskDto();
        updateTaskDto.setId(UUID.randomUUID());
        updateTaskDto.setUserId(userId);
        updateTaskDto.setStatus(TaskStatus.CREATED);

        when(taskRepositoryMock.findTaskStatusById(updateTaskDto.getId())).thenReturn(Optional.of(TaskStatus.AWAIT));
        when(userRepositoryMock.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> taskService.put(updateTaskDto));
    }

    @Test
    @DisplayName("Delete task successfully")
    void deleteTaskById() {
        Task task = new Task();

        taskService.deleteById(task.getId());

        verify(taskRepositoryMock).deleteById(task.getId());
    }
}