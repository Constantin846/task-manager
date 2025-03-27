package tk.project.taskmanager.task.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import tk.project.taskmanager.task.Task;
import tk.project.taskmanager.task.dto.create.CreateTaskDto;
import tk.project.taskmanager.task.dto.create.CreateTaskRequest;
import tk.project.taskmanager.task.dto.find.FindTaskDto;
import tk.project.taskmanager.task.dto.find.FindTaskResponse;
import tk.project.taskmanager.task.dto.update.UpdateTaskDto;
import tk.project.taskmanager.task.dto.update.UpdateTaskRequest;
import tk.project.taskmanager.task.dto.update.UpdateTaskResponse;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskDtoMapper {
    TaskDtoMapper MAPPER = Mappers.getMapper(TaskDtoMapper.class);

    CreateTaskDto toCreateTaskDto(CreateTaskRequest taskRequest);

    Task toTask(CreateTaskDto taskDto);

    @Mapping(target = "userId", source = "task.user.id" )
    FindTaskDto toFindTaskDto(Task task);

    FindTaskResponse toFindTaskResponse(FindTaskDto taskDto);

    default List<FindTaskResponse> toFindTaskResponse(Collection<FindTaskDto> tasksDto) {
        return tasksDto.stream()
                .map(this::toFindTaskResponse)
                .toList();
    }

    UpdateTaskDto toUpdateTaskDto(UpdateTaskRequest taskRequest);

    Task toTask(UpdateTaskDto taskDto);

    @Mapping(target = "userId", source = "task.user.id" )
    UpdateTaskDto toUpdateTaskDto(Task task);

    UpdateTaskResponse toUpdateTaskResponse(UpdateTaskDto taskDto);
}
