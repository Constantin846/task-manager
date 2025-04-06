package tk.project.taskmanager.user.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tk.project.taskmanager.user.User;
import tk.project.taskmanager.user.dto.CreateUserDto;
import tk.project.taskmanager.user.dto.CreateUserRequest;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {
    UserDtoMapper MAPPER = Mappers.getMapper(UserDtoMapper.class);

    CreateUserDto toCreateUserDto(CreateUserRequest createUserRequest);

    User toUser(CreateUserDto createUserDto);
}
