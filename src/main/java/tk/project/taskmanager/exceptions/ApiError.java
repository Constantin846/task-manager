package tk.project.taskmanager.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {

    private String exceptionName;

    private String message;

    private Instant time;

    private String className;
}
