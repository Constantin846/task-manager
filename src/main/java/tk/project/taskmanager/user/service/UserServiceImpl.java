package tk.project.taskmanager.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.project.taskmanager.aspect.annotation.LogCoverMethod;
import tk.project.taskmanager.aspect.annotation.MeasureExecutionTime;
import tk.project.taskmanager.user.User;
import tk.project.taskmanager.user.UserRepository;
import tk.project.taskmanager.user.dto.CreateUserDto;
import tk.project.taskmanager.user.dto.mapper.UserDtoMapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDtoMapper mapper;
    private final UserRepository userRepository;

    @Transactional
    @Override
    @LogCoverMethod
    @MeasureExecutionTime
    public UUID createUser(CreateUserDto userDto) {
        User user = mapper.toUser(userDto);
        user = userRepository.save(user);
        return user.getId();
    }
}
