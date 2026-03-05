package support.config;

public interface RetryableTaskConfig extends TaskConfig{
    int getMaxRetryTimes();
    default boolean isRetryable(){
        return getMaxRetryTimes() > 0;
    }
    boolean isBackRetry();
    long getBackRetryTimeOffset();      // 时间偏移

    boolean reportOnce();
}
