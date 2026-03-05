//package support.context;
//
//import com.yumitoy.admin.tasks.support.TaskCallback;
//import com.yumitoy.admin.tasks.support.config.TaskConfig;
//import com.yumitoy.admin.tasks.support.scheduler.Scheduler;
//import com.yumitoy.admin.tasks.support.task.Task;
//
//import java.time.LocalDateTime;
//import java.util.Map;
//
///**
// * 默认的通用TaskContext
// */
//public class DefaultTaskContext extends AbstractTaskContext<Object>{
//    protected DefaultTaskContext(Task<Object> task, TaskConfig taskConfig, Scheduler scheduler, TaskCallback taskCallback, Map<String, Object> metaData, Object param, Class<Object> paramClass, String taskId, LocalDateTime createdTime) {
//        super(task, taskConfig, scheduler, taskCallback, metaData, param, paramClass, taskId, createdTime);
//    }
//
//}
