//package support;
//
//import com.yumitoy.admin.tasks.support.config.TaskConfig;
//import com.yumitoy.admin.tasks.support.context.TaskContext;
//
//import java.time.LocalDateTime;
//
//public interface TaskCallback{
//    void onSuccess(String taskId, Object result, TaskContext<?> context);
//    void onFailure(String taskId, Exception e, TaskContext<?> context);
//    void onCancel(String taskId, TaskContext<?> context);
//    void onExpired(String taskId, LocalDateTime scheduleTime, LocalDateTime findItExpiredTime,
//                   TaskConfig taskConfig, TaskContext<?> context);
//}