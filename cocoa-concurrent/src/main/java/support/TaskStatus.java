package support;

public enum TaskStatus {
    // 初始状态
    PENDING,
    RETRY_PENDING,
    // 运行状态
    RUNNING,
    // 终止状态
    COMPLETED,
    CANCELLED,
    FAILURE,
    EXPIRED;    // 如果任务超时之后一直没有执行时，且不需要执行，则需要标记此状态
}
