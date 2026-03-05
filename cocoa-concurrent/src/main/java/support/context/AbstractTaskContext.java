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
//import java.util.Objects;
//
//public abstract class AbstractTaskContext<T> implements TaskContext<T>{
//
//    private Task<T> task;
//    private TaskConfig taskConfig;
//    private Scheduler scheduler;
//    private TaskCallback taskCallback;
//    private Map<String, Object> metaData;
//    private T param;
//    private Class<T> paramClass;
//    private String taskId;
//    private LocalDateTime createdTime;
//
//    // 子代理类兼容的构造器
//    protected AbstractTaskContext(TaskContext<T> context){
//        Objects.requireNonNull(context, "代理类不能提供null");
//    }
//
//    protected AbstractTaskContext(Task<T> task, TaskConfig taskConfig, Scheduler scheduler, TaskCallback taskCallback,
//                                  Map<String, Object> metaData, T param, Class<T> paramClass, String taskId,
//                                  LocalDateTime createdTime) {
//        this.task = task;
//        this.taskConfig = taskConfig;
//        this.scheduler = scheduler;
//        this.taskCallback = taskCallback;
//        this.metaData = metaData;
//        this.param = param;
//        this.paramClass = paramClass;
//        this.taskId = taskId;
//        this.createdTime = createdTime;
//    }
//
//    protected void setTask(Task<T> task) {
//        this.task = task;
//    }
//
//    protected void setTaskConfig(TaskConfig taskConfig) {
//        this.taskConfig = taskConfig;
//    }
//
//    protected void setScheduler(Scheduler scheduler) {
//        this.scheduler = scheduler;
//    }
//
//    protected void setTaskCallback(TaskCallback taskCallback) {
//        this.taskCallback = taskCallback;
//    }
//
//    protected void setMetaData(Map<String, Object> metaData) {
//        this.metaData = metaData;
//    }
//
//    protected void setParam(T param) {
//        this.param = param;
//    }
//
//    protected void setParamClass(Class<T> paramClass) {
//        this.paramClass = paramClass;
//    }
//
//    protected void setTaskId(String taskId) {
//        this.taskId = taskId;
//    }
//
//    protected void setCreatedTime(LocalDateTime createdTime) {
//        this.createdTime = createdTime;
//    }
//
//    @Override
//    public Scheduler getScheduler() {
//        return scheduler;
//    }
//
//    @Override
//    public TaskConfig getTaskConfig() {
//        return taskConfig;
//    }
//
//    @Override
//    public Task<T> getTask() {
//        return task;
//    }
//
//    @Override
//    public TaskCallback getCallback() {
//        return taskCallback;
//    }
//
//    @Override
//    public Map<String, Object> getMetaData() {
//        return metaData;
//    }
//
//    @Override
//    public T getContextParams() {
//        return param;
//    }
//
//    @Override
//    public Class<T> getContextParamsClass() {
//        return paramClass;
//    }
//
//    @Override
//    public String getTaskId() {
//        return taskId;
//    }
//
//    @Override
//    public LocalDateTime getCreatedTime() {
//        return createdTime;
//    }
//
//    // 失败次数
//    private int failureCount;
//    private TaskStatus taskStatus;
//    private LocalDateTime taskStatusTime;
//
//    @Override
//    public int getFailureCount() {
//        return failureCount;
//    }
//
//    @Override
//    public TaskStatus getTaskStatus() {
//        return taskStatus;
//    }
//
//    @Override
//    public LocalDateTime getTaskStatusTime() {
//        return taskStatusTime;
//    }
//
//    protected void setFailureCount(int failureCount) {
//        this.failureCount = failureCount;
//    }
//    protected void setTaskStatus(TaskStatus taskStatus){
//        this.taskStatus = taskStatus;
//    }
//    protected void setTaskStatusTime(LocalDateTime statusTime){
//        this.taskStatusTime = statusTime;
//    }
//    protected void addFailureCount(){
//        failureCount++;
//    }
//
//    // 数据库辅助方法
//    // todo 默认我们生成Json形式即可
//    protected String serialize(@Nullable Class<T> tClass) {
//        return null;
//    }
//    protected T deserialize(String string) {
//        return null;
//    }
//}
