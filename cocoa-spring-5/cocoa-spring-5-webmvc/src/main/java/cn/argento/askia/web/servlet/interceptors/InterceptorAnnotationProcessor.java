package cn.argento.askia.web.servlet.interceptors;

import cn.argento.askia.exceptions.runtime.reflect.ReflectiveOperationRuntimeException;
import cn.argento.askia.utilities.annotation.AnnotationUtility;
import cn.argento.askia.utilities.classes.ClassUtility;
import cn.argento.askia.utilities.collection.ArrayUtility;
import cn.argento.askia.utilities.collection.CollectionUtility;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Intercepter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * {@code @Interceptor} 的注解处理器
 *
 * 客户端代码调用其中的 {@code process} 方法进行处理注解
 *
 * @author Askia
 * @since 2026.4.27
 * @see #process(Class, InterceptorRegistry)
 * @see #process(InterceptorRegistry, HandlerInterceptor...)
 * @see #process(Class, InterceptorRegistry, ApplicationContext)
 */
@Slf4j
public class InterceptorAnnotationProcessor {

    // 该方法能实现获取一个类上的所有@Interceptor, 如果不存在则
    private static Interceptor[] solveWithClass(Class<?> clazz) {
        if (AnnotationUtility.isDeclaredAnnotationPresent(clazz, Interceptor.class)) {
            return AnnotationUtility.getRepeatableAnnotations(clazz, Interceptor.class);
        }

        return new Interceptor[0];
    }

    private static Interceptor solveWithConstructor(Constructor<?> constructor) {
        if (AnnotationUtility.isDeclaredAnnotationPresent(constructor, Interceptor.class)) {
            return AnnotationUtility.getDeclaredAnnotation(constructor, Interceptor.class);
        }
        return null;
    }

    // 判断一个类是否实现了HandlerInterceptor接口(只有实现了这个接口的类才是拦截器)
    public static boolean isInterceptor(Class<?> clazz) {
        return HandlerInterceptor.class.isAssignableFrom(clazz);
    }

    // 1.扫描包及类(必须指定classpath路径下的包和位置, 包名, 类名过滤, 缓存机制)
    //   - 扫描器
    //   - 扫描策略(策略模式)
    // 2.必要的检查(是否标记注解等)
    // 3.注解处理(注解融合、特殊注解，顺序问题等)
    // 4. 核心处理过程(结合上下文进行处理)
    // 5. 后置处理阶段(处理上下文，返回值等等)
    // 6. 完成处理阶段(日志留档)

    /**
     * 注解处理函数.
     *
     * <p>适用于 {@link HandlerInterceptor} 的类上标记了 {@link Interceptor} 然后提供默认构造器的注册. </p>
     * <p>可和 {@link #process(Class, InterceptorRegistry, ApplicationContext)} 混用但是需要注意 {@link #process(Class, InterceptorRegistry, ApplicationContext)} 的条件</p>
     * @param baseClass 扫描基类
     * @param registry 注册器
     */
    public static void process(Class<?> baseClass, InterceptorRegistry registry){
        if (log.isDebugEnabled()) {
            log.debug(".......... --------------------------------- 拦截器(基准类扫描方式)注册报告 ---------------------------------");
        } else if (log.isInfoEnabled()) {
            log.info(".......... --------------------------------- 拦截器(基准类扫描方式)注册报告 ---------------------------------");
        }
        try {
            Class<?>[] classes = ClassUtility.scanCurrentDirectoryClasses(baseClass, false);
            Map<Class<?>, List<Interceptor>> interceptorMap = new HashMap<>();
            if (classes == null){
                classes = new Class[0];
            }
            if (classes.length == 0) {
                if (log.isDebugEnabled()) {
                    log.warn(".......... 【❌】没有找到相应的注解拦截器对象，不进行此类拦截器的注册");
                    log.debug(".......... -----------------------------------------------------------------------------");

                } else if (log.isInfoEnabled()) {
                    log.info(".......... 已注册【0】个拦截器！");
                    log.info(".......... -----------------------------------------------------------------------------");
                }
                return;
            }
            Class<?> traverse = null;
            for (Class<?> cl : classes) {
                if (log.isDebugEnabled()) {
                    log.debug(".......... ==> {}, 是否实现了{} = {}", cl.getSimpleName(), HandlerInterceptor.class.getSimpleName(), HandlerInterceptor.class.isAssignableFrom(cl) ? "✅ 解析" : "❌ 跳过");
                }
                // 是否实现了HandlerInterceptor接口
                traverse = isInterceptor(cl) ? cl : null;
                if (traverse == null) {
                    continue;
                }
                // 判断是否有@Interceptor注解
                final Interceptor[] interceptors = solveWithClass(cl);
                if (interceptors.length == 0) {
                    if (log.isDebugEnabled()) {
                        log.debug(".......... ==> 没有在{}上发现任何@{}注解, 跳过!", cl.getSimpleName(), Interceptor.class.getSimpleName());
                    }
                    continue;
                }
                try{
                    // 判断有没有默认构造器
                    cl.getConstructor();
                }
                catch (NoSuchMethodException e){
                    if (log.isDebugEnabled()){
                        log.debug(".......... ==> 没有在{}上发现默认构造器, 跳过!", cl.getSimpleName());
                    }
                    continue;
                }

                // 添加到map中
                if (log.isDebugEnabled()) {
                    log.debug(".......... ==> {}上发现@{} = {}!", cl.getSimpleName(), Interceptor.class.getSimpleName(), ArrayUtility.toBeautifulString(interceptors));
                }
                interceptorMap.put(cl, new ArrayList<>(Arrays.asList(interceptors)));
            }
            if (log.isInfoEnabled()) {
                log.info(".......... 发现{}个拦截器对象 = {}", interceptorMap.keySet().size(), ArrayUtility.toBeautifulString(interceptorMap.keySet().toArray()));
            }
            // 实际处理的地方
            int count = 0;
            for (Map.Entry<Class<?>, List<Interceptor>> entry : interceptorMap.entrySet()) {
                final Class<?> key = entry.getKey();
                final List<Interceptor> value = entry.getValue();
                try{
                    HandlerInterceptor o = (HandlerInterceptor) key.newInstance();
                    for (Interceptor itc : value) {
                        // 添加的Mapping
                        List<String> addMappings = new ArrayList<>();
                        Collections.addAll(addMappings, itc.value());
                        Collections.addAll(addMappings, itc.addPathPatterns());
                        final String[] excludePathPatterns = itc.excludePathPatterns();
                        final Class<? extends Supplier<? extends PathMatcher>> pathMatcherSupplierClass = itc.pathMatcherSupplierClass();
                        final InterceptorRegistration order = registry.addInterceptor(o)
                                .addPathPatterns(addMappings)
                                .excludePathPatterns(excludePathPatterns)
                                .order(itc.order());
                        count++;
                        if (pathMatcherSupplierClass != DefaultPathMatcherSupplier.class) {
                            // 不是默认的提供者，我们需要处理
                            try {
                                final Supplier<? extends PathMatcher> supplier = pathMatcherSupplierClass.newInstance();
                                final PathMatcher pathMatcher = supplier.get();
                                order.pathMatcher(pathMatcher);
                            } catch (Exception e) {
                                // 如果无法初始化Supplier, 则我们不添加
                            }
                        }
                        if (log.isInfoEnabled()) {
                            log.info(".......... 已注册{} , addMapping = {} , excludeMapping = {}, order = {} 【√】", key, addMappings, Arrays.toString(itc.excludePathPatterns()), itc.order());
                        }
                    }
                } catch (IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                    if (log.isErrorEnabled()) {
                        log.error(".......... 注册{}失败, 无法创建此拦截器的对象, 请提供一个默认构造器 【❌】", key.getSimpleName());
                    }
                }
            }
            log.info(".......... 已注册【{}】个拦截器！", count);
            if (log.isDebugEnabled()) {
                log.debug(".......... -----------------------------------------------------------------------------");
            } else if (log.isInfoEnabled()) {
                log.info(".......... -----------------------------------------------------------------------------");
            }
        } catch (IOException e) {
            e.printStackTrace();
            // 没有这类注解
            if (log.isDebugEnabled()) {
                log.warn(".......... 【❌】无法扫描拦截器, 在扫描拦截器类是发生异常, 错误方法 = scanCurrentDirectoryClasses", e);
                log.debug(".......... -----------------------------------------------------------------------------");

            } else if (log.isInfoEnabled()) {
                log.info(".......... 【❌】因扫描时发生错误, 放弃此方式的拦截器扫描");
                log.info(".......... -----------------------------------------------------------------------------");
            }
        }
    }

    /**
     * 注解处理函数.
     * <p>适用于 {@link HandlerInterceptor} 的构造器上标记了 {@link Interceptor} 并携带参数的注册. </p>
     * <p>可和 {@link #process(Class, InterceptorRegistry)} 混用但是需要注意 {@link #process(Class, InterceptorRegistry)} 的条件</p>
     * @param baseClass 扫描基类
     * @param registry 注册器
     * @param context IoC容器上下文
     */
    public static void process(Class<?> baseClass, InterceptorRegistry registry, ApplicationContext context) {
        if (log.isDebugEnabled()) {
            log.debug(".......... --------------------------------- 拦截器(基准类扫描方式 + 参数构造器)注册报告 ---------------------------------");
        } else if (log.isInfoEnabled()) {
            log.info(".......... --------------------------------- 拦截器(基准类扫描方式 + 参数构造器)注册报告 ---------------------------------");
        }
        try {
            Class<?>[] classes = ClassUtility.scanCurrentDirectoryClasses(baseClass, false);
            Map<Constructor<?>, Interceptor> interceptorMap = new HashMap<>();
            if (classes == null){
                classes = new Class[0];
            }
            if (classes.length == 0) {
                if (log.isDebugEnabled()) {
                    log.warn(".......... 【❌】没有找到相应的注解拦截器对象，不进行此类拦截器的注册");
                    log.debug(".......... -----------------------------------------------------------------------------");

                } else if (log.isInfoEnabled()) {
                    log.info(".......... 已注册【0】个拦截器！");
                    log.info(".......... -----------------------------------------------------------------------------");
                }
                return;
            }
            Class<?> traverse = null;
            for (Class<?> cl : classes) {
                if (log.isDebugEnabled()) {
                    log.debug(".......... ==> {}, 是否实现了{} = {}", cl.getSimpleName(), HandlerInterceptor.class.getSimpleName(), HandlerInterceptor.class.isAssignableFrom(cl) ? "✅ 解析" : "❌ 跳过");
                }
                // 是否实现了HandlerInterceptor接口
                traverse = isInterceptor(cl) ? cl : null;
                if (traverse == null) {
                    continue;
                }
                // 寻找标记了@Interceptor的构造器
                Interceptor interceptor = null;
                Constructor<?> find = null;
                final Constructor<?>[] constructors = cl.getConstructors();
                for (Constructor<?> constructor : constructors){
                    interceptor = solveWithConstructor(constructor);
                    // 找到一个就直接退出
                    if (interceptor != null){
                        find = constructor;
                        break;
                    }
                }
                if (interceptor == null){
                    // 找不到标记了的
                    if (log.isDebugEnabled()){
                        log.debug(".......... ==> 没有在{}上发现标记了@Interceptor注解, 跳过!", cl.getSimpleName());
                    }
                    continue;
                }

                // 添加到map中
                if (log.isDebugEnabled()) {
                    log.debug(".......... ==> {}上发现@{} = {}!", find, Interceptor.class.getSimpleName(), interceptor);
                }
                interceptorMap.put(find, interceptor);
            }
            if (log.isInfoEnabled()) {
                log.info(".......... 发现{}个拦截器对象 = {}", interceptorMap.keySet().size(), ArrayUtility.toBeautifulString(interceptorMap.keySet().toArray()));
            }
            // 实际处理的地方
            int count = 0;
            for (Map.Entry<Constructor<?>, Interceptor> entry : interceptorMap.entrySet()) {
                final Constructor<?> key = entry.getKey();
                final Interceptor value = entry.getValue();
                try{
                    final Class<?>[] parameterTypes = key.getParameterTypes();
                    Object[] params = new Object[parameterTypes.length];
                    for (int i = 0; i < params.length; i++) {
                        final Object bean = context.getBean(parameterTypes[i]);
                        params[i] = bean;
                    }
                    final HandlerInterceptor o = (HandlerInterceptor) key.newInstance(params);
                    // 添加的Mapping
                    List<String> addMappings = new ArrayList<>();
                    Collections.addAll(addMappings, value.value());
                    Collections.addAll(addMappings, value.addPathPatterns());
                    final String[] excludePathPatterns = value.excludePathPatterns();
                    final Class<? extends Supplier<? extends PathMatcher>> pathMatcherSupplierClass = value.pathMatcherSupplierClass();
                    final InterceptorRegistration order = registry.addInterceptor(o)
                            .addPathPatterns(addMappings)
                            .excludePathPatterns(excludePathPatterns)
                            .order(value.order());
                    count++;
                    if (pathMatcherSupplierClass != DefaultPathMatcherSupplier.class) {
                        // 不是默认的提供者，我们需要处理
                        try {
                            final Supplier<? extends PathMatcher> supplier = pathMatcherSupplierClass.newInstance();
                            final PathMatcher pathMatcher = supplier.get();
                            order.pathMatcher(pathMatcher);
                        } catch (Exception e) {
                            // 如果无法初始化Supplier, 则我们不添加
                        }
                    }
                    if (log.isInfoEnabled()) {
                        log.info(".......... 已注册{} , addMapping = {} , excludeMapping = {}, order = {} 【√】", key, addMappings, Arrays.toString(value.excludePathPatterns()), value.order());
                    }
                } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                    e.printStackTrace();
                    if (log.isErrorEnabled()) {
                        log.error(".......... 注册{}失败, 无法创建此拦截器的对象, 请提供构造器{}的参数 【❌】", key.getDeclaringClass(), key);
                    }
                }
            }
            log.info(".......... 已注册【{}】个拦截器！", count);
            if (log.isDebugEnabled()) {
                log.debug(".......... -----------------------------------------------------------------------------");
            } else if (log.isInfoEnabled()) {
                log.info(".......... -----------------------------------------------------------------------------");
            }
        } catch (IOException e) {
            e.printStackTrace();
            // 没有这类注解
            if (log.isDebugEnabled()) {
                log.warn(".......... 【❌】无法扫描拦截器, 在扫描拦截器类是发生异常, 错误方法 = scanCurrentDirectoryClasses", e);
                log.debug(".......... -----------------------------------------------------------------------------");

            } else if (log.isInfoEnabled()) {
                log.info(".......... 【❌】因扫描时发生错误, 放弃此方式的拦截器扫描");
                log.info(".......... -----------------------------------------------------------------------------");
            }
        }
    }

    /**
     * 注解处理函数.
     *
     * <p>适用于 {@link HandlerInterceptor} 的子类被标记了 @{@link org.springframework.stereotype.Component} 并且在 {@code WebMvcConfigurer}中被 {@code @Autowired} 进来的处理方式</p>
     *
     * @param registry     注册器
     * @param interceptors {@link HandlerInterceptor}对象
     */
    public static void process(InterceptorRegistry registry, HandlerInterceptor... interceptors) {
        if (log.isDebugEnabled()) {
            log.debug(".......... --------------------------------- 拦截器(IoC容器对象)注册报告 ---------------------------------");
        } else if (log.isInfoEnabled()) {
            log.info(".......... --------------------------------- 拦截器(IoC容器对象)注册报告 ---------------------------------");
        }
        // 如果没有拦截器
        if (interceptors == null || interceptors.length == 0) {
            // 没有这类注解
            if (log.isDebugEnabled()) {
                log.warn(".......... 【❌】没有找到相应的注解拦截器对象，不进行此类拦截器的注册");
                log.debug(".......... -----------------------------------------------------------------------------");

            } else if (log.isInfoEnabled()) {
                log.info(".......... 已注册【0】个拦截器！");
                log.info(".......... -----------------------------------------------------------------------------");
            }
            return;
        }
        // 打印对象, 并进行注解获取处理
//        Map<Class<? extends HandlerInterceptor>, Map<ElementType, List<Interceptor>>> interceptorMap = new HashMap<>();
        Map<HandlerInterceptor, List<Interceptor>> interceptorMap = new HashMap<>();
        // 已经创建好的对象我们只需要获取类上的标记就好了!
        for (HandlerInterceptor handlerInterceptor : interceptors) {
            final Class<? extends HandlerInterceptor> handlerInterceptorClass = handlerInterceptor.getClass();
            final Interceptor[] interceptorsOnClass = solveWithClass(handlerInterceptorClass);
            if (log.isDebugEnabled()) {
                log.debug(".......... 拦截器对象 = {}, 类型 = {}, @Interceptor = {}", handlerInterceptor, handlerInterceptorClass, ArrayUtility.toBeautifulString(interceptorsOnClass));
            }
            interceptorMap.put(handlerInterceptor, new ArrayList<>(Arrays.asList(interceptorsOnClass)));
        }
        if (log.isDebugEnabled()) {
            log.debug("..........");
        }
        int count = 0;
        for (Map.Entry<HandlerInterceptor, List<Interceptor>> entry : interceptorMap.entrySet()) {
            final HandlerInterceptor key = entry.getKey();
            final List<Interceptor> interceptorList = entry.getValue();
            for (Interceptor itc : interceptorList) {
                // 添加的Mapping
                List<String> addMappings = new ArrayList<>();
                Collections.addAll(addMappings, itc.value());
                Collections.addAll(addMappings, itc.addPathPatterns());
                final String[] excludePathPatterns = itc.excludePathPatterns();
                final Class<? extends Supplier<? extends PathMatcher>> pathMatcherSupplierClass = itc.pathMatcherSupplierClass();
                final InterceptorRegistration order = registry.addInterceptor(key)
                        .addPathPatterns(addMappings)
                        .excludePathPatterns(excludePathPatterns)
                        .order(itc.order());
                count++;
                if (pathMatcherSupplierClass != DefaultPathMatcherSupplier.class) {
                    // 不是默认的提供者，我们需要处理
                    try {
                        final Supplier<? extends PathMatcher> supplier = pathMatcherSupplierClass.newInstance();
                        final PathMatcher pathMatcher = supplier.get();
                        order.pathMatcher(pathMatcher);
                    } catch (Exception e) {
                        // 如果无法初始化Supplier, 则我们不添加
                    }
                }
                if (log.isInfoEnabled()) {
                    log.info(".......... 已注册{} , addMapping = {} , excludeMapping = {}, order = {} 【√】", key.getClass(), addMappings, Arrays.toString(itc.excludePathPatterns()), itc.order());
                }
            }
        }
        log.info(".......... 已注册【{}】个拦截器！", count);
        if (log.isDebugEnabled()) {
            log.debug(".......... -----------------------------------------------------------------------------");
        } else if (log.isInfoEnabled()) {
            log.info(".......... -----------------------------------------------------------------------------");
        }

    }

    private static HandlerInterceptor createInterceptInstance(ApplicationContext context, Class<?> handlerInterceptorClass) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (!HandlerInterceptor.class.isAssignableFrom(handlerInterceptorClass)) {
            // 不是HandlerInterceptor
            throw new IllegalAccessException("非HandlerInterceptor拦截器类");
        }
        final Constructor<?>[] constructors = handlerInterceptorClass.getConstructors();
        Constructor<?> constructor;
        if (constructors.length == 1) {
            // 只有一个构造器则必定是你了
            constructor = constructors[0];
        } else {
            // 寻找标记了@Autowire的！
            final Optional<Constructor<?>> first = Arrays.stream(constructors).parallel().filter(constructor2 -> constructor2.isAnnotationPresent(Autowired.class)).findFirst();
            constructor = first.orElseThrow(() -> new ReflectiveOperationRuntimeException("无法找到合适的构造器创建拦截器"));
        }
        // 组装参数
        final Parameter[] parameters = constructor.getParameters();
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            // 有限处理带@Qualifier的情况
            if (AnnotationUtility.isAnnotationPresent(parameters[i], Qualifier.class)) {
                // 如果标记了@Qualifier,  则我们按照名称来查询
                final Qualifier annotation = AnnotationUtility.getAnnotation(parameters[i], Qualifier.class);
                final String name = annotation.value();
                final Object bean = context.getBean(name);
                args[i] = bean;
            } else {
                // 按照类型来获取
                final Class<?> type = parameters[i].getType();
                final Object bean = context.getBean(type);
                args[i] = bean;
            }
        }
        // 保证构造器可以被使用
        if (!constructor.isAccessible()) {
            constructor.setAccessible(true);
        }
        final HandlerInterceptor o = (HandlerInterceptor) constructor.newInstance(args);
        return o;
    }

    // 合并所有属性，或者带@AlisaFor属性的注解
    private static Map<String, Object> combineAttributes(Interceptor interceptor) {
        Map<String, Object> attributes = new HashMap<>();
        final String[] value = interceptor.value();
        final int order = interceptor.order();
        final String[] excludePathPatterns = interceptor.excludePathPatterns();
        final String[] addPathPatterns = interceptor.addPathPatterns();
        // 其中addPathPatterns()和value()需要合并
        // fixme 是否有更加通用的合并方法？
        final Set<String> addPathPatternsFinal = ArrayUtility.toSet(addPathPatterns);
        addPathPatternsFinal.addAll(Arrays.asList(value));
        attributes.put("addPathPatterns", addPathPatternsFinal.toArray(new String[0]));
        attributes.put("order", order);
        attributes.put("excludePathPatterns", excludePathPatterns);
        return attributes;
    }
}
