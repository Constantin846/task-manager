package tk.project.taskmanager.user.service;

import tk.project.taskmanager.user.dto.CreateUserDto;

import java.util.UUID;

public interface UserService {
    UUID createUser(CreateUserDto userDto);
}
