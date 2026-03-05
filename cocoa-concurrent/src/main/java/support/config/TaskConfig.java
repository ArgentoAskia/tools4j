package support.config;

import java.time.LocalDateTime;

public interface TaskConfig {
    String getTaskClassName();
    String getTaskDescription();
    LocalDateTime getScheduleTime();    // 获取任务运行时间
    boolean isRunImmediatelyWhenExpired();    // 一旦发现过期是否需要重启运行
    // 是否支持取消
    boolean isCancelable();

    // 获取任务标记ID，此ID由客户端代码生成，用于快速标识Task对象
    // 实现需要做到能通过此Id获取唯一标识任务
    String getTaskMarkId();
}
