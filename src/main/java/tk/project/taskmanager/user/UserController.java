package tk.project.taskmanager.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tk.project.taskmanager.aspect.annotation.LogStartMethod;
import tk.project.taskmanager.user.dto.CreateUserDto;
import tk.project.taskmanager.user.dto.CreateUserRequest;
import tk.project.taskmanager.user.dto.mapper.UserDtoMapper;
import tk.project.taskmanager.user.service.UserService;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserDtoMapper mapper;
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @LogStartMethod
    public Map<String, UUID> create(@Valid @RequestBody CreateUserRequest userRequest) {
        CreateUserDto userDto = mapper.toCreateUserDto(userRequest);
        UUID id = userService.createUser(userDto);
        return Map.of("id", id);
    }
}
