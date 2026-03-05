//package support.task;
//
//import com.yumitoy.admin.tasks.support.config.TaskConfig;
//import com.yumitoy.admin.tasks.support.context.TaskContext;
//
//public abstract class AbstractTask<T> implements Task<T>{
//    private TaskConfig taskConfig;
//    private volatile TaskContext<T> taskContext;
//
//
//    public AbstractTask(TaskConfig taskConfig, TaskContext<T> taskContext) {
//        this.taskConfig = taskConfig;
//        this.taskContext = taskContext;
//    }
//
//    public TaskConfig getTaskConfig() {
//        return taskConfig;
//    }
//
//    public TaskContext<T> getTaskContext() {
//        return taskContext;
//    }
//}
