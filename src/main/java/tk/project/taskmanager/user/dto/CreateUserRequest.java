package tk.project.taskmanager.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserRequest {

    @NotBlank(message = "User name must be set")
    @Length(message = "User name length must be between 1 and 64 characters inclusive", min = 1, max = 64)
    String name;

    @NotBlank(message = "User email must be set")
    @Email(message = "User email has a wrong format")
    @Length(message = "User email length must be between 1 and 64 characters inclusive", min = 1, max = 64)
    String email;
}
