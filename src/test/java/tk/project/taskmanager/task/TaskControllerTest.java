package tk.project.taskmanager.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import tk.project.taskmanager.AbstractBaseIntegrationTest;
import tk.project.taskmanager.exceptions.ApiError;
import tk.project.taskmanager.exceptions.TaskNotFoundException;
import tk.project.taskmanager.exceptions.UserNotFoundException;
import tk.project.taskmanager.kafka.KafkaProducer;
import tk.project.taskmanager.task.dto.create.CreateTaskRequest;
import tk.project.taskmanager.task.dto.find.FindTaskResponse;
import tk.project.taskmanager.task.dto.update.UpdateTaskRequest;
import tk.project.taskmanager.task.dto.update.UpdateTaskResponse;
import tk.project.taskmanager.task.model.Task;
import tk.project.taskmanager.task.model.TaskStatus;
import tk.project.taskmanager.user.User;
import tk.project.taskmanager.user.UserRepository;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TaskControllerTest extends AbstractBaseIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;
    @MockBean
    private KafkaProducer kafkaProducer;

    @AfterEach
    void clearDatabase() {
        taskRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @SneakyThrows
    @DisplayName("Create task successfully")
    void createTask() {
        User user = new User();
        user.setName("name");
        user.setEmail("email@mail");
        user = userRepository.save(user);

        CreateTaskRequest taskRequest = new CreateTaskRequest();
        taskRequest.setTitle("title");
        taskRequest.setDescription("description");
        taskRequest.setUserId(user.getId());

        String result = mockMvc.perform(post("/tasks")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertNotNull(result);
    }

    @Test
    @SneakyThrows
    @DisplayName("Failed to create task if user was not found")
    void createTaskFailedWithNotFoundUserException() {
        CreateTaskRequest taskRequest = new CreateTaskRequest();
        taskRequest.setTitle("title");
        taskRequest.setDescription("description");
        taskRequest.setUserId(UUID.randomUUID());

        String result = mockMvc.perform(post("/tasks")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ApiError apiError = objectMapper.readValue(result, ApiError.class);

        assertEquals(UserNotFoundException.class.getSimpleName(), apiError.getExceptionName());
    }

    @Test
    @SneakyThrows
    @DisplayName("Find all tasks successfully")
    void findAllTasks() {
        User user = new User();
        user.setName("name");
        user.setEmail("email@mail");
        user = userRepository.save(user);

        Task task = new Task();
        task.setTitle("title");
        task.setDescription("description");
        task.setUser(user);
        task.setStatus(TaskStatus.CREATED);
        task = taskRepository.save(task);

        FindTaskResponse taskResponse = new FindTaskResponse();
        taskResponse.setId(task.getId());
        taskResponse.setTitle(task.getTitle());
        taskResponse.setDescription(task.getDescription());
        taskResponse.setUserId(task.getUser().getId());
        taskResponse.setStatus(task.getStatus());
        List<FindTaskResponse> expectedList = List.of(taskResponse);

        String result = mockMvc.perform(get("/tasks")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expectedList), result);
    }

    @Test
    @SneakyThrows
    @DisplayName("Find task by id successfully")
    void findTaskById() {
        User user = new User();
        user.setName("name");
        user.setEmail("email@mail");
        user = userRepository.save(user);

        Task task = new Task();
        task.setTitle("title");
        task.setDescription("description");
        task.setUser(user);
        task.setStatus(TaskStatus.CREATED);
        task = taskRepository.save(task);

        FindTaskResponse expectedTaskResponse = new FindTaskResponse();
        expectedTaskResponse.setId(task.getId());
        expectedTaskResponse.setTitle(task.getTitle());
        expectedTaskResponse.setDescription(task.getDescription());
        expectedTaskResponse.setUserId(task.getUser().getId());
        expectedTaskResponse.setStatus(task.getStatus());

        String result = mockMvc.perform(get(String.format("/tasks/%s", task.getId()))
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expectedTaskResponse), result);
    }

    @Test
    @SneakyThrows
    @DisplayName("Task was not found by id")
    void findTaskByIdFailed() {
        String result = mockMvc.perform(get(String.format("/tasks/%s", UUID.randomUUID()))
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ApiError apiError = objectMapper.readValue(result, ApiError.class);

        assertEquals(TaskNotFoundException.class.getSimpleName(), apiError.getExceptionName());
    }

    @Test
    @SneakyThrows
    @DisplayName("Update task successfully")
    void updateTaskById() {
        User user = new User();
        user.setName("name");
        user.setEmail("email@mail");
        user = userRepository.save(user);

        Task task = new Task();
        task.setTitle("title");
        task.setDescription("description");
        task.setUser(user);
        task.setStatus(TaskStatus.CREATED);
        task = taskRepository.save(task);

        UpdateTaskRequest updateTaskRequest = new UpdateTaskRequest();
        updateTaskRequest.setTitle("new title");
        updateTaskRequest.setDescription("new description");
        updateTaskRequest.setUserId(task.getUser().getId());
        updateTaskRequest.setStatus(TaskStatus.DONE);

        UpdateTaskResponse expectedTaskResponse = new UpdateTaskResponse();
        expectedTaskResponse.setId(task.getId());
        expectedTaskResponse.setTitle(updateTaskRequest.getTitle());
        expectedTaskResponse.setDescription(updateTaskRequest.getDescription());
        expectedTaskResponse.setUserId(updateTaskRequest.getUserId());
        expectedTaskResponse.setStatus(updateTaskRequest.getStatus());

        String result = mockMvc.perform(put(String.format("/tasks/%s", task.getId()))
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updateTaskRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expectedTaskResponse), result);
        verify(kafkaProducer).sendEvent(eq(kafkaProducer.getTaskNotificationsTopic()),
                eq(expectedTaskResponse.getId().toString()), any());
    }

    @Test
    @SneakyThrows
    @DisplayName("Failed to update task if user was not found")
    void updateTaskByIdFailedWithNotFoundUserException() {
        UpdateTaskRequest taskRequest = new UpdateTaskRequest();
        taskRequest.setTitle("title");
        taskRequest.setDescription("description");
        taskRequest.setUserId(UUID.randomUUID());
        taskRequest.setStatus(TaskStatus.CANCELLED);
        UUID taskId = UUID.randomUUID();

        String result = mockMvc.perform(put(String.format("/tasks/%s", taskId))
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ApiError apiError = objectMapper.readValue(result, ApiError.class);

        assertEquals(UserNotFoundException.class.getSimpleName(), apiError.getExceptionName());
        verify(kafkaProducer, never()).sendEvent(eq(kafkaProducer.getTaskNotificationsTopic()),
                eq(taskId.toString()), any());
    }

    @Test
    @SneakyThrows
    @DisplayName("Delete task successfully")
    void deleteTaskById() {
        User user = new User();
        user.setName("name");
        user.setEmail("email@mail");
        user = userRepository.save(user);

        Task task = new Task();
        task.setTitle("title");
        task.setDescription("description");
        task.setUser(user);
        task.setStatus(TaskStatus.CREATED);
        task = taskRepository.save(task);

        String result = mockMvc.perform(delete(String.format("/tasks/%s", task.getId()))
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertTrue(result.isBlank());
    }
}