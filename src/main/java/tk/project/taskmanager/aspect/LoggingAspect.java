package tk.project.taskmanager.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Before("@annotation(tk.project.taskmanager.aspect.annotation.LogStartMethod)" +
            " || @annotation(tk.project.taskmanager.aspect.annotation.LogAroundMethod)")
    public void loggingBefore(JoinPoint joinPoint) {
        log.info("Start method {} with parameters: {}",
                joinPoint.getSignature().toShortString(), Arrays.toString(joinPoint.getArgs()));
    }

    @AfterThrowing(
            pointcut = "(@annotation(tk.project.taskmanager.aspect.annotation.LogException)" +
                    " || @annotation(tk.project.taskmanager.aspect.annotation.LogAroundMethod))",
            throwing = "exception"
    )
    public void loggingException(JoinPoint joinPoint, Exception exception) {
        log.warn("Exception was thrown by method {} with parameters: {}",
                joinPoint.getSignature().toShortString(), Arrays.toString(joinPoint.getArgs()));
        log.warn("Exception {} contains a message: {}", exception.getClass().getSimpleName(), exception.getMessage());
    }

    @AfterReturning(
            pointcut = "(@annotation(tk.project.taskmanager.aspect.annotation.LogCompleteMethod)" +
                    " || @annotation(tk.project.taskmanager.aspect.annotation.LogAroundMethod))",
            returning = "result"
    )
    public void loggingFinishMethod(JoinPoint joinPoint, Object result) {
        log.info("Method {} was completed successfully with the result: {}",
                joinPoint.getSignature().toShortString(), result);
    }

    /**
     * {@code @Order(Integer.MAX_VALUE - 2)}
     *  <p>The annotation states that this advice covers around all the others. For example @Transactional.</p>
     */
    @Order(Integer.MAX_VALUE - 2)
    @Around("@annotation(tk.project.taskmanager.aspect.annotation.MeasureExecutionTime)")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        log.info("Start measuring execution method {}", joinPoint.getSignature().toShortString());
        Object result;
        long startTime = System.currentTimeMillis();

        try {
            result = joinPoint.proceed();

        } catch (Throwable throwable) {
            log.info("Unable to measure execution time of method {} that threw exception {}",
                    joinPoint.getSignature().toShortString(), throwable.getClass().getSimpleName());
            log.info("Time since the start of the execution is {} millis", (System.currentTimeMillis() - startTime));
            throw throwable;
        }

        long endTime = System.currentTimeMillis();
        log.info("Method {} was completed successfully in {} millis",
                joinPoint.getSignature().toShortString(), (endTime - startTime));
        return result;
    }
}
