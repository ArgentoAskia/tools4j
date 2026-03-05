package support.config;

import java.time.LocalDateTime;

public final class DefaultTaskConfigBuilder {
    private String taskClassName;
    private String taskDescription;
    private String taskMarkId;
    private LocalDateTime scheduleTime;
    private boolean runImmediatelyWhenExpired;
    private boolean cancelable;

    private DefaultTaskConfigBuilder() {
    }

    public static DefaultTaskConfigBuilder builder() {
        return new DefaultTaskConfigBuilder();
    }

    public DefaultTaskConfigBuilder taskClassName(String taskClassName) {
        this.taskClassName = taskClassName;
        return this;
    }

    public DefaultTaskConfigBuilder taskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
        return this;
    }

    public DefaultTaskConfigBuilder withTaskMarkId(String taskMarkId) {
        this.taskMarkId = taskMarkId;
        return this;
    }

    public DefaultTaskConfigBuilder withScheduleTime(LocalDateTime scheduleTime) {
        this.scheduleTime = scheduleTime;
        return this;
    }

    public DefaultTaskConfigBuilder withRunImmediatelyWhenExpired(boolean runImmediatelyWhenExpired) {
        this.runImmediatelyWhenExpired = runImmediatelyWhenExpired;
        return this;
    }

    public DefaultTaskConfigBuilder withCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    public DefaultTaskConfig build() {
        return new DefaultTaskConfig(taskClassName, taskDescription, taskMarkId, scheduleTime, runImmediatelyWhenExpired, cancelable);
    }
}
