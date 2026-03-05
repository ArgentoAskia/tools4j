//package support.context;
//
//import com.yumitoy.admin.tasks.support.TaskCallback;
//import com.yumitoy.admin.tasks.support.TaskStatus;
//import com.yumitoy.admin.tasks.support.config.TaskConfig;
//import com.yumitoy.admin.tasks.support.scheduler.Scheduler;
//import com.yumitoy.admin.tasks.support.task.Task;
//
//import java.time.LocalDateTime;
//import java.util.Map;
//
//public interface TaskContext<T> {
//
//    // 执行前上下文
//    // 获取绑定的调度器
//    Scheduler getScheduler();
//    // 获取绑定的Task配置
//    TaskConfig getTaskConfig();
//    // 获取绑定的Task
//    Task<T> getTask();
//    // taskCallback
//    TaskCallback getCallback();
//
//
//    // 执行中上下文
//    /**
//     * 额外的参数输入
//     * @return
//     */
//    Map<String, Object> getMetaData();
//
//    /**
//     * 获取扩展的参数
//     * @return
//     */
//    T getContextParams();
//
//    Class<T> getContextParamsClass();
//
//    // 执行后上下文
//
//    /**
//     * 获取内部TaskId, 此Id由框架维护Task对象生成的唯一Id，需要区别 {@link TaskConfig#getTaskMarkId()}
//     * @return
//     */
//    String getTaskId();
//    // 获取创建时间
//    LocalDateTime getCreatedTime();
//    // 执行失败时间
//    int getFailureCount();
//    // 获取执行状态
//    TaskStatus getTaskStatus();
//    LocalDateTime getTaskStatusTime();  // 获取当前状态执行时间
//
//    default boolean isCancelled(){
//        return getTaskStatus() == TaskStatus.CANCELLED;
//    }
//
//    default boolean isCompleted(){
//        return getTaskStatus() == TaskStatus.COMPLETED;
//    }
//
//    default boolean isExpired(){
//        return getTaskStatus() == TaskStatus.EXPIRED;
//    }
//    default boolean isPending(){
//        return getTaskStatus() == TaskStatus.PENDING;
//    }
//
//}
