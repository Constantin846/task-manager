package tk.project.taskmanager.user.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import tk.project.taskmanager.user.User;
import tk.project.taskmanager.user.UserRepository;
import tk.project.taskmanager.user.dto.CreateUserDto;
import tk.project.taskmanager.user.dto.mapper.UserDtoMapper;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Spy
    private UserDtoMapper mapper = UserDtoMapper.MAPPER;
    @Mock
    private UserRepository userRepositoryMock;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Create user successfully")
    void createUser() {
        UUID userId = UUID.randomUUID();
        User expectedUser = new User();
        expectedUser.setId(userId);

        when(userRepositoryMock.save(new User())).thenReturn(expectedUser);

        UUID actualUserId = userService.createUser(new CreateUserDto());

        assertEquals(expectedUser.getId(), actualUserId);
    }
}