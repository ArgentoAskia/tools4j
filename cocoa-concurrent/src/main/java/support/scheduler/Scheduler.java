//package support.scheduler;
//
//import com.yumitoy.admin.tasks.support.task.Task;
//import com.yumitoy.admin.tasks.support.TaskCallback;
//import com.yumitoy.admin.tasks.support.TaskStatus;
//
//import java.util.List;
//
//// 调度器
//public interface Scheduler {
//
//    // 调度
//    String scheduleTask(Task<?> task, TaskCallback callback);
//
//    int cancelTask(String taskId);
//
//    List<Task<?>> getPendingTasks();
//
//    int getTotalTasksCount();
//
//    TaskStatus getTaskStatus(String taskId);
//
//    void shutdown();
//
//    String findUID(String markId);
//
//}
//
