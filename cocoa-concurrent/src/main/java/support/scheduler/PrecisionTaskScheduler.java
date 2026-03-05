//package support.scheduler;
//
//import com.yumitoy.admin.tasks.support.TaskCallback;
//import com.yumitoy.admin.tasks.support.TaskStatus;
//import com.yumitoy.admin.tasks.support.config.TaskConfig;
//import com.yumitoy.admin.tasks.support.context.DefaultTaskContext;
//import com.yumitoy.admin.tasks.support.context.DefaultTaskContextProxy;
//import com.yumitoy.admin.tasks.support.context.TaskContext;
//import com.yumitoy.admin.tasks.support.task.Task;
//import com.yumitoy.utilities.DateTimeUtility;
//import com.yumitoy.utilities.KeyUtility;
//
//import java.time.LocalDateTime;
//import java.time.temporal.ChronoUnit;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.*;
//import java.util.concurrent.atomic.AtomicInteger;
//
//public class PrecisionTaskScheduler implements com.yumitoy.admin.tasks.support.scheduler.Scheduler {
//
//    private final ScheduledExecutorService scheduler;       // 任务调度器
//    private final ExecutorService executor;                 // 任务执行器
//    private final Map<String, String> idMaps;               // markId绑定taskId
//    private final Map<String, Task<?>> registeredTasks;     // 注册的任务
//    private final Map<String, ScheduledFuture<?>> scheduledFutures;     // 任务注册结果存储(是否注册成功)
//    private final Map<String, Future<?>> executeFutures;                // 任务执行结果存储(用于取消执行)
//    private final PriorityBlockingQueue<Task<?>> taskQueue;           // 任务队列
//    private final AtomicInteger taskCounter;                        // 总任务数
//    // 失败队列
//    // 过期队列
//
//
//
//    private static final String SCHEDULER_NAME = "TaskScheduler-Thread";
//    private static final Integer SCHEDULER_CORE_POOL_SIZE = 2;
//    public static final Integer CPU_CORES = Runtime.getRuntime().availableProcessors();
//    private static final Integer MAX_TASK_LIMITED = 1500;
//    private static final String EXECUTOR_NAME = "TaskExecutor-Thread";
//    private static final Long KEEP_ALIVE_TIME = 60L;
//    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
//    private static final RejectedExecutionHandler DEFAULT_POLICY_HANDLER = new ThreadPoolExecutor.CallerRunsPolicy();
//
//    private static final Integer DEFAULT_QUEUE_LARGE = 20;
//
//
//    public PrecisionTaskScheduler(){
//        scheduler = new ScheduledThreadPoolExecutor(SCHEDULER_CORE_POOL_SIZE, r -> {
//            Thread thread = new Thread(r, SCHEDULER_NAME);
//            thread.setDaemon(true);     // 守护线程的方式进行工作
//            return thread;
//        });
//        executor = new ThreadPoolExecutor(
//                CPU_CORES * 2,
//                CPU_CORES * 8,
//                KEEP_ALIVE_TIME,
//                KEEP_ALIVE_TIME_UNIT,
//                new ArrayBlockingQueue<>(MAX_TASK_LIMITED),
//                r -> {
//                    Thread t = new Thread(r, EXECUTOR_NAME);
//                    t.setDaemon(false);
//                    return t;
//                },
//                DEFAULT_POLICY_HANDLER   // 如果失败了则阻塞让调用方直接调用
//        );
//        idMaps = new ConcurrentHashMap<>();
//        registeredTasks = new ConcurrentHashMap<>();
//        scheduledFutures = new ConcurrentHashMap<>();
//        executeFutures = new ConcurrentHashMap<>();
//        taskQueue = new PriorityBlockingQueue<>(20, (o1, o2) -> {
//            TaskContext<?> context1 = o1.getContext();
//            TaskContext<?> context2 = o2.getContext();
//            LocalDateTime scheduleTime1 = context1.getTaskConfig().getScheduleTime();
//            LocalDateTime scheduleTime2 = context2.getTaskConfig().getScheduleTime();
//            return scheduleTime1.compareTo(scheduleTime2);
//        });
//        taskCounter = new AtomicInteger(0);
//        startQueueMonitor();
//    }
//
//    // 任务超时时间
//    private static final long TIME_DELAY_LIMITED = 30;
//    // 如果系统的时间更新了, 需要调用此来调整队列
//    // 同时如果队列的任务执行完成了，我们需要将其出队，防止任务堆积
//    // 同步问题
//    private void startQueueMonitor(){
//        scheduler.scheduleAtFixedRate(() -> {
//            try {
//                // 锁住队列
//                synchronized (taskQueue){
//                    LocalDateTime now = LocalDateTime.now();
//                    Iterator<Task<?>> iterator = taskQueue.iterator();
//                    while(iterator.hasNext()) {
//                        Task<?> next = iterator.next();
//                        TaskContext<?> context = next.getContext();
//                        if (context.isCompleted()){
//                            // 任务完成了，直接移除
//                            iterator.remove();
//                            continue;
//                        }
//                        TaskConfig config = next.getConfig();
//                        String taskId = context.getTaskId();
//                        // 获取任务运行时间
//                        LocalDateTime scheduleTime = config.getScheduleTime();
//                        // 如果任务时间已超时30分钟以上，很有可能是因为系统时间调整问题导致任务堆积（可能由于系统时间调整）
//                        if (DateTimeUtility.between(scheduleTime, now, ChronoUnit.MINUTES) > TIME_DELAY_LIMITED) {
//                            // 此时我们获取任务
//                            ScheduledFuture<?> scheduledFuture = scheduledFutures.get(taskId);
//                            // 必须要两个状态都满足时才清除
//                            TaskStatus taskStatus = context.getTaskStatus();
//                            // 任务存在, 且没有完成没有取消，且context中显示此任务为Pending状态
//                            if (scheduledFuture != null && !scheduledFuture.isCancelled() && !scheduledFuture.isDone() &&
//                                    taskStatus == TaskStatus.PENDING) {
//                                // 则此处我们需要查看一下任务是否配置了过期运行
//                                // 标记此任务为过期
//                                TaskCallback callback = context.getCallback();
//                                DefaultTaskContextProxy contextProxy = getContextProxy(context);
//                                contextProxy.setTaskStatus(TaskStatus.EXPIRED);
//                                contextProxy.setTaskStatusTime(now);
//                                //  触发过期回调
//                                callback.onExpired(taskId, scheduleTime, now, config, context);
//                                if (config.isRunImmediatelyWhenExpired()) {
//                                    // 重新提交最近时间运行任务
//                                    Future<?> submit = executor.submit(() -> {
//                                        executeTask0(next, contextProxy, context, callback);
//                                    });
//                                    executeFutures.put(taskId, submit);
//                                }
//
//                            }
//                            // 移除此过期任务
//                            iterator.remove();
//                        }
//                        // 最先开始的那个都超过30分钟了则肯定没啥好说的，
//                        else {
//                            break;
//                        }
//                    }
//                }
//            }
//            catch (Exception e){
//                e.printStackTrace();
//            }
//        }, 1, 3, TimeUnit.MINUTES);
//    }
//
//    private String createTaskUID(){
//        return KeyUtility.newUUID(false);
//    }
//    @Override
//    public String scheduleTask(Task<?> task, TaskCallback callback) {
//        // 1. 创建一个内部使用的TaskID并绑定
//        String taskUID = createTaskUID();
//        doIdBinging(taskUID, task.getConfig());
//
//        // 2. 更新TaskContext上下文
//        TaskContext<?> context = task.getContext();
//        DefaultTaskContextProxy contextProxy = getContextProxy(context);
//        contextProxy.setCreatedTime(LocalDateTime.now());
//        contextProxy.setFailureCount(0);
//        contextProxy.setScheduler(this);
//        contextProxy.setTaskConfig(task.getConfig());
//        contextProxy.setTaskId(taskUID);
//        contextProxy.setTaskStatus(TaskStatus.PENDING);
//        contextProxy.setTaskStatusTime(LocalDateTime.now());
//
//        // 3. 注册任务
//        registeredTasks.put(taskUID, task);
//        taskQueue.offer(task);
//        taskCounter.getAndIncrement();
//        // 写入数据库
//
//        // 4. 计算延迟并调度执行
//        scheduleTaskInternal(task, contextProxy);
//
//        // 5. 返回taskUID
//        return taskUID;
//    }
//
//    // 默认委托类
//    private DefaultTaskContextProxy getContextProxy(TaskContext<?> context){
//        if ((context instanceof DefaultTaskContext)){
//            DefaultTaskContext defaultTaskContext = (DefaultTaskContext) context;
//            DefaultTaskContextProxy.proxy(defaultTaskContext);
//        }
//        throw new UnsupportedOperationException("未支持的委托");
//    }
//    private void doIdBinging(String taskUID, TaskConfig taskConfig){
//        String taskMarkId = taskConfig.getTaskMarkId();
//        idMaps.put(taskMarkId, taskUID);
//    }
//    // 调度任务
//    private void scheduleTaskInternal(Task<?> task, DefaultTaskContextProxy contextProxy){
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime scheduledTime = task.getConfig().getScheduleTime();
//        // 如果发现提供的运行时间已经在当前时间前面了，则立即执行任务
//        if (scheduledTime.isBefore(now)){
//            executeTask(task, contextProxy);
//            return;
//        }
//        // 否则我们计算延迟
//        long delay = ChronoUnit.MILLIS.between(now, scheduledTime);
//        ScheduledFuture<?> future = scheduler.schedule(() -> {
//            // 提交并执行任务
//            executeTask(task, contextProxy);
//        }, delay, TimeUnit.MILLISECONDS);
//
//        // 缓存注册结果(用于实现取消注册)
//        TaskContext<?> context = task.getContext();
//        String taskId = context.getTaskId();
//        scheduledFutures.put(taskId, future);
//    }
//    // 执行任务
//    private void executeTask(Task<?> task, DefaultTaskContextProxy contextProxy){
//        TaskContext<?> context = task.getContext();
//        String taskId = context.getTaskId();
//        Future<?> submit = executor.submit(() -> {
//            TaskCallback callback = context.getCallback();
//            // 如果任务已经取消
//            // 判别运行状态设置运行状态
//            synchronized (taskQueue){
//                lockModifyStatus(TaskStatus.RUNNING, LocalDateTime.now(), contextProxy, context, taskId, callback);
//                executeTask0(task, contextProxy, context, callback);
//            }
//        });
//        executeFutures.put(taskId, submit);
//    }
//    private void executeTask0(Task<?> task, DefaultTaskContextProxy contextProxy, TaskContext<?> context, TaskCallback callback){
//        // 真正执行任务
//        try {
//            Object result = task.execute(context);
//            // 设置状态
//            contextProxy.setTaskStatus(TaskStatus.COMPLETED);
//            contextProxy.setTaskStatusTime(LocalDateTime.now());
//            if (callback != null) {
//                callback.onSuccess(context.getTaskId(), result, context);
//            }
//        }
//        catch (Exception e) {
//            // 添加错误次数
//            contextProxy.addFailureCount();
//            contextProxy.setTaskStatus(TaskStatus.FAILURE);
//            if (callback != null) {
//                callback.onFailure(context.getTaskId(), e, context);
//            }
//            else {
//                // 默认异常处理
//                e.printStackTrace();
//            }
//        }
//    }
//    // 锁住更新状态
//    private void lockModifyStatus(TaskStatus taskStatus, LocalDateTime statusTime, DefaultTaskContextProxy contextProxy, TaskContext<?> context, String taskId, TaskCallback callback){
//        // 最后一次检测是否取消
//        if (contextProxy.isCancelled()){
//            if (callback != null) {
//                callback.onCancel(taskId, context);
//            }
//        }
//        else{
//            // 设置状态之后，此时就不会有同步问题
//            contextProxy.setTaskStatus(taskStatus);
//            contextProxy.setTaskStatusTime(statusTime);
//        }
//    }
//
//    /**
//     *
//     * @param taskId
//     * @return 如果取消成功，则返回1，如果任务本身不能被取消，或者发送取消指令时任务刚好在运行阶段结束，导致无法取消，则返回0，如果任务不存在则返回-1
//     */
//    @Override
//    public int cancelTask(String taskId) {
//        ScheduledFuture<?> scheduledFuture = scheduledFutures.get(taskId);
//        if (scheduledFuture != null && !scheduledFuture.isDone()){
//            synchronized (taskQueue){
//                Task<?> task = registeredTasks.get(taskId);
//                // 判断是否是就绪状态【只有就绪状态可以取消，运行状态一般不推荐取消】
//                // 获取任务处于过期状态，则我们也可以取消此任务的执行
//                if (task.getContext().isPending() || task.getContext().isExpired()){
//                    if (scheduledFuture.cancel(true)){
//                        if (task.getConfig().isCancelable()) {
//                            DefaultTaskContextProxy contextProxy = getContextProxy(task.getContext());
//                            contextProxy.setTaskStatus(TaskStatus.CANCELLED);
//                            contextProxy.setTaskStatusTime(LocalDateTime.now());
//                            return 1;
//                        }
//                    }
//                }
//                return 0;
//            }
//        }
//        return -1;
//    }
//
//    @Override
//    public List<Task<?>> getPendingTasks() {
//        return new ArrayList<>(taskQueue);
//    }
//
//
//    @Override
//    public int getTotalTasksCount() {
//        return taskCounter.get();
//    }
//
//
//    @Override
//    public TaskStatus getTaskStatus(String taskId) {
//        return null;
//    }
//
//    @Override
//    public void shutdown() {
//
//    }
//
//    @Override
//    public String findUID(String markId) {
//        return idMaps.get(markId);
//    }
//}
