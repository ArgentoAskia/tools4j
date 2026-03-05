package support.config;

import java.time.LocalDateTime;

public abstract class AbstractRetryableTaskConfig extends AbstractTaskConfig implements RetryableTaskConfig{

    private final int maxRetryTime;
    private final boolean reportOnce;
    private final boolean backRetry;
    private final long backRetryOffset;

    protected AbstractRetryableTaskConfig(String taskClassName, String taskDescription, String taskMarkId, LocalDateTime scheduleTime, boolean runImmediatelyWhenExpired, boolean cancelable, int maxRetryTime, boolean reportOnce, boolean backRetry, long backRetryOffset) {
        super(taskClassName, taskDescription, taskMarkId, scheduleTime, runImmediatelyWhenExpired, cancelable);
        this.maxRetryTime = maxRetryTime;
        this.reportOnce = reportOnce;
        this.backRetry = backRetry;
        this.backRetryOffset = backRetryOffset;
    }

    public int getMaxRetryTime() {
        return maxRetryTime;
    }

    public boolean isReportOnce() {
        return reportOnce;
    }

    @Override
    public boolean isBackRetry() {
        return backRetry;
    }

    public long getBackRetryOffset() {
        return backRetryOffset;
    }
}
