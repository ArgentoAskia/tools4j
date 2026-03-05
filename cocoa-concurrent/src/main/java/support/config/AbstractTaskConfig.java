package support.config;

import java.time.LocalDateTime;

public abstract class AbstractTaskConfig implements TaskConfig {

    private final String taskClassName;
    private final String taskDescription;
    private final String taskMarkId;
    private final LocalDateTime scheduleTime;
    private final boolean runImmediatelyWhenExpired;
    private final boolean cancelable;

    protected AbstractTaskConfig(String taskClassName, String taskDescription, String taskMarkId, LocalDateTime scheduleTime, boolean runImmediatelyWhenExpired, boolean cancelable) {
        this.taskClassName = taskClassName;
        this.taskDescription = taskDescription;
        this.taskMarkId = taskMarkId;
        this.scheduleTime = scheduleTime;
        this.runImmediatelyWhenExpired = runImmediatelyWhenExpired;
        this.cancelable = cancelable;
    }

    @Override
    public String getTaskClassName() {
        return taskClassName;
    }

    @Override
    public String getTaskDescription() {
        return taskDescription;
    }

    @Override
    public String getTaskMarkId() {
        return taskMarkId;
    }

    @Override
    public LocalDateTime getScheduleTime() {
        return scheduleTime;
    }

    public boolean isRunImmediatelyWhenExpired() {
        return runImmediatelyWhenExpired;
    }

    @Override
    public boolean isCancelable() {
        return cancelable;
    }

}
