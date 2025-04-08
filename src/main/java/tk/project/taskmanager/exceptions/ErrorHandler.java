package tk.project.taskmanager.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice(basePackages = "tk.project.taskmanager")
public class ErrorHandler {
    private static final String DELIMITER = "; ";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handlerValidException(final MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getFieldErrors();
        Set<String> messages = new HashSet<>();

        for (FieldError fieldError : fieldErrors) {
            String message = fieldError != null ? fieldError.getDefaultMessage() : INTERNAL_SERVER_ERROR.toString();
            message = message != null ? message : INTERNAL_SERVER_ERROR.toString();
            messages.add(message);
        }
        String message = String.join(DELIMITER, messages);

        return createApiError(e, message);
    }

    @ExceptionHandler(EventHandlerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handlerEventHandlerNotFoundException(final EventHandlerNotFoundException e) {
        return createApiError(e);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handlerTaskNotFoundException(final TaskNotFoundException e) {
        return createApiError(e);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handlerUserNotFoundException(final UserNotFoundException e) {
        return createApiError(e);
    }

    @ExceptionHandler
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ApiError handlerThrowable(final Throwable e) {
        String message = INTERNAL_SERVER_ERROR.toString();
        log.warn(message, e);
        return createApiError(e, message);
    }

    private ApiError createApiError(final Throwable e) {
        return createApiError(e, e.getMessage());
    }

    private ApiError createApiError(final Throwable e, final String message) {
        return new ApiError(e.getClass().getSimpleName(), message,
                Instant.now(), e.getStackTrace()[0].getFileName());
    }
}