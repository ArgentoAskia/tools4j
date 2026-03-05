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
//public final class DefaultTaskContextBuilder {
//    private Task<Object> task;
//    private TaskConfig taskConfig;
//    private Scheduler scheduler;
//    private TaskCallback taskCallback;
//    private Map<String, Object> metaData;
//    private String taskId;
//    private LocalDateTime createdTime;
//    private Object param;
//    private Class<Object> paramClass;
//
//    // proxy
//
//    private DefaultTaskContextBuilder() {
//        paramClass = Object.class;
//    }
//
//    public static DefaultTaskContextBuilder builder() {
//        return new DefaultTaskContextBuilder();
//    }
//    public DefaultTaskContextBuilder bindTask(Task<Object> task){
//        this.task = task;
//        return this;
//    }
//    public DefaultTaskContextBuilder bindParam(Object param){
//        this.param = param;
//        return this;
//    }
//
//    public DefaultTaskContextBuilder withTaskConfig(TaskConfig taskConfig) {
//        this.taskConfig = taskConfig;
//        return this;
//    }
//
//    public DefaultTaskContextBuilder withScheduler(Scheduler scheduler) {
//        this.scheduler = scheduler;
//        return this;
//    }
//
//    public DefaultTaskContextBuilder withTaskCallback(TaskCallback taskCallback) {
//        this.taskCallback = taskCallback;
//        return this;
//    }
//
//    public DefaultTaskContextBuilder withMetaData(Map<String, Object> metaData) {
//        this.metaData = metaData;
//        return this;
//    }
//
//    public DefaultTaskContextBuilder withTaskId(String taskId) {
//        this.taskId = taskId;
//        return this;
//    }
//
//    public DefaultTaskContextBuilder withCreatedTime(LocalDateTime createdTime) {
//        this.createdTime = createdTime;
//        return this;
//    }
//
//    public DefaultTaskContext build() {
//        return new DefaultTaskContext(task, taskConfig, scheduler, taskCallback, metaData, param, paramClass, taskId, createdTime);
//    }
//}
