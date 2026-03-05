//package support.context;
//
//import com.yumitoy.admin.tasks.support.TaskCallback;
//import com.yumitoy.admin.tasks.support.TaskStatus;
//import com.yumitoy.admin.tasks.support.config.TaskConfig;
//import com.yumitoy.admin.tasks.support.scheduler.Scheduler;
//import com.yumitoy.admin.tasks.support.task.Task;
//import org.jetbrains.annotations.Nullable;
//
//import java.time.LocalDateTime;
//import java.util.Map;
//
///**
// * DefaultTaskContext的代理，增加修改支持
// */
//public class DefaultTaskContextProxy extends AbstractTaskContext<Object>{
//
//    private DefaultTaskContextProxy(DefaultTaskContext taskContext){
//        super(taskContext);
//        this.taskContextProxy = taskContext;
//    }
//
//    private volatile DefaultTaskContext taskContextProxy;
//    public static DefaultTaskContextProxy proxy(DefaultTaskContext taskContext){
//        return new DefaultTaskContextProxy(taskContext);
//    }
//    @Override
//    public void setFailureCount(int failureCount) {
//        taskContextProxy.setFailureCount(failureCount);
//    }
//
//    @Override
//    public void setTaskStatus(TaskStatus taskStatus) {
//        taskContextProxy.setTaskStatus(taskStatus);
//    }
//
//    @Override
//    public void setTaskStatusTime(LocalDateTime statusTime) {
//        taskContextProxy.setTaskStatusTime(statusTime);
//    }
//
//    @Override
//    public void addFailureCount() {
//        taskContextProxy.addFailureCount();
//    }
//
//    @Override
//    public String serialize(@Nullable Class<Object> objectClass) {
//        return taskContextProxy.serialize(objectClass);
//    }
//
//    @Override
//    public Object deserialize(String string) {
//        return taskContextProxy.deserialize(string);
//    }
//
//    @Override
//    public void setTask(Task<Object> task) {
//        taskContextProxy.setTask(task);
//    }
//
//    @Override
//    public void setTaskConfig(TaskConfig taskConfig) {
//        taskContextProxy.setTaskConfig(taskConfig);
//    }
//
//    @Override
//    public void setScheduler(Scheduler scheduler) {
//        taskContextProxy.setScheduler(scheduler);
//    }
//
//    @Override
//    public void setTaskCallback(TaskCallback taskCallback) {
//        taskContextProxy.setTaskCallback(taskCallback);
//    }
//
//    @Override
//    public void setMetaData(Map<String, Object> metaData) {
//        taskContextProxy.setMetaData(metaData);
//    }
//
//    @Override
//    public void setParam(Object param) {
//        taskContextProxy.setParam(param);
//    }
//
//    @Override
//    public void setParamClass(Class<Object> paramClass) {
//        taskContextProxy.setParamClass(paramClass);
//    }
//
//    @Override
//    public void setTaskId(String taskId) {
//        taskContextProxy.setTaskId(taskId);
//    }
//
//    @Override
//    public void setCreatedTime(LocalDateTime createdTime) {
//        taskContextProxy.setCreatedTime(createdTime);
//    }
//
//    @Override
//    public Scheduler getScheduler() {
//        return taskContextProxy.getScheduler();
//    }
//
//    @Override
//    public TaskConfig getTaskConfig() {
//        return taskContextProxy.getTaskConfig();
//    }
//
//    @Override
//    public Task<Object> getTask() {
//        return taskContextProxy.getTask();
//    }
//
//    @Override
//    public TaskCallback getCallback() {
//        return taskContextProxy.getCallback();
//    }
//
//    @Override
//    public Map<String, Object> getMetaData() {
//        return taskContextProxy.getMetaData();
//    }
//
//    @Override
//    public Object getContextParams() {
//        return taskContextProxy.getContextParams();
//    }
//
//    @Override
//    public Class<Object> getContextParamsClass() {
//        return taskContextProxy.getContextParamsClass();
//    }
//
//    @Override
//    public String getTaskId() {
//        return taskContextProxy.getTaskId();
//    }
//
//    @Override
//    public LocalDateTime getCreatedTime() {
//        return taskContextProxy.getCreatedTime();
//    }
//
//    @Override
//    public int getFailureCount() {
//        return taskContextProxy.getFailureCount();
//    }
//
//    @Override
//    public TaskStatus getTaskStatus() {
//        return taskContextProxy.getTaskStatus();
//    }
//
//    @Override
//    public LocalDateTime getTaskStatusTime() {
//        return taskContextProxy.getTaskStatusTime();
//    }
//}
