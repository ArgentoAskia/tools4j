package support.config;

import java.time.LocalDateTime;

public class DefaultTaskConfig extends AbstractTaskConfig{
    public DefaultTaskConfig(String taskClassName, String taskDescription, String taskMarkId, LocalDateTime scheduleTime, boolean runImmediatelyWhenExpired, boolean cancelable) {
        super(taskClassName, taskDescription, taskMarkId, scheduleTime, runImmediatelyWhenExpired, cancelable);
    }
}
