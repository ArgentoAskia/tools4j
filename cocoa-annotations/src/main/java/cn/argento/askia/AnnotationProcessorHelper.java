package cn.argento.askia;

import cn.argento.askia.annotations.*;
import cn.argento.askia.annotations.phase.*;
import cn.argento.askia.context.AnnotationProcessorContext;
import cn.argento.askia.context.BeanNotFoundException;
import cn.argento.askia.context.MoreThenOneBeanException;
import cn.argento.askia.context.MutableAnnotationProcessorContext;
import cn.argento.askia.langs.TypeReference;
import cn.argento.askia.supports.LifeCyclePhase;
import cn.argento.askia.utilities.annotation.AnnotationUtility;
import cn.argento.askia.utilities.collection.ArrayUtility;
import cn.argento.askia.utilities.lang.StringUtility;
import cn.argento.askia.utilities.reflect.ReflectUtility;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

/**
 * 注解处理器辅助工具类
 */
@ApiStatus.Internal
class AnnotationProcessorHelper {

    /**
     * 生成注解处理器元数据
     * @param annotationProcessor
     * @param <T>
     * @return
     */
    @SuppressWarnings("all")
    static <T> AnnotationProcessingEnvironmentBean<T> introspectAnnotationProcessor(T annotationProcessor){
        AnnotationProcessingEnvironmentBean<T> annotationProcessingEnvironmentBean = new AnnotationProcessingEnvironmentBean<>();
        final Class<?> annotationProcessorClass = annotationProcessor.getClass();
        annotationProcessingEnvironmentBean.setAnnotationProcessorClass((Class<T>) annotationProcessorClass);
        if (AnnotationUtility.isDeclaredAnnotationPresent(annotationProcessorClass, AnnotationProcessor.class)) {
            final AnnotationProcessor declaredAnnotation = AnnotationUtility.getDeclaredAnnotation(annotationProcessorClass, AnnotationProcessor.class);
            annotationProcessingEnvironmentBean.setAnnotationProcessor(declaredAnnotation);
        }

        // 方法设置
        final Set<Method> methods =  ReflectUtility.getAllMethods(annotationProcessorClass);
        for (Method m : methods){
            Annotation[] declaredAnnotations = AnnotationUtility.getDeclaredAnnotations(m);
            final Annotation[] phaseAnnotations = LifeCyclePhase.filterPhase(declaredAnnotations);
            for (Annotation a : phaseAnnotations){
                add2Map(annotationProcessingEnvironmentBean, a, m);
            }
        }
        return annotationProcessingEnvironmentBean;
    }
    static void add2Map(AnnotationProcessingEnvironmentBean<?> bean, Annotation annotation, Method m){
        if (annotation.annotationType() == AnnotationProcessingPhase.class){
            // 添加到AnnotationProcessingPhase
            bean.addMethodAnnotationProcessingPhase(m, (AnnotationProcessingPhase) annotation);
        }
        else if (annotation.annotationType() == CheckPhase.class){
            bean.addMethodCheckPhase(m, (CheckPhase) annotation);
        }
        else if (annotation.annotationType() == CompletionPhase.class){
            bean.addMethodCompletionPhase(m, (CompletionPhase) annotation);
        }
        else if (annotation.annotationType() == MainProcessingPhase.class){
            bean.addMethodMainProcessingPhase(m, (MainProcessingPhase) annotation);
        }
        else if (annotation.annotationType() == PostProcessingPhase.class){
            bean.addMethodPostProcessingPhase(m, (PostProcessingPhase) annotation);
        }
        else if (annotation.annotationType() == ScanPhase.class){
            bean.addMethodScanPhase(m, (ScanPhase) annotation);
        }
        throw new UnsupportedOperationException("暂不支持解析该阶段" + annotation.annotationType());
    }

    /**
     * 将 {@link AnnotationProcessorContext} 转为 {@link MutableAnnotationProcessorContext}
     * @param context 实现了 {@link MutableAnnotationProcessorContext} 的子实现
     * @return {@link MutableAnnotationProcessorContext} 对象, 转换失败将返回 {@code null}
     */
    static MutableAnnotationProcessorContext getMutableContext(AnnotationProcessorContext context){
        if (context instanceof MutableAnnotationProcessorContext){
            return (MutableAnnotationProcessorContext) context;
        }
        return null;
    }

    /**
     * 初始化上下文容器
     * @param context context
     */
    static void initContext(MutableAnnotationProcessorContext context, Object annotationProcessObj) throws InvocationTargetException, IllegalAccessException {
        final Class<?> annotationProcessObjClass = annotationProcessObj.getClass();
        final Set<Method> declaredMethods = ReflectUtility.getAllMethods(annotationProcessObjClass);
        Method initMethod = null;
        String beanName = null;
        for (Method m : declaredMethods){
            if (AnnotationUtility.isDeclaredAnnotationPresent(m, Initialization.class)) {
                // 标记了@Initialization
                initMethod = m;
                beanName = Initialization.class.getSimpleName();
                break;
            }
            if (AnnotationUtility.isDeclaredAnnotationPresent(m, PostConstruct.class)){
                // 标记了@PostConstruct
                initMethod = m;
                beanName = PostConstruct.class.getSimpleName();
                break;
            }
        }
        if (initMethod != null){
            // 允许访问私有方法和继承性方法
            if (!initMethod.isAccessible()){
                initMethod.setAccessible(true);
            }
            final Class<?>[] parameterTypes = initMethod.getParameterTypes();
            if (parameterTypes.length == 0){
                // 直接调用
                final Object invoke = initMethod.invoke(annotationProcessObj);
                context.registerBean(beanName, invoke);
                return;
            }
            if (parameterTypes.length == 1 && (AnnotationProcessorContext.class == parameterTypes[0] || MutableAnnotationProcessorContext.class == parameterTypes[0])){
                final Object invoke = initMethod.invoke(annotationProcessObj, context);
                context.registerBean(beanName, invoke);
                return;
            }
            // 两个以上的参数
            if (parameterTypes.length > 1){
                final Parameter[] parameters = initMethod.getParameters();
                Object[] args = new Object[parameters.length];
                int index = 0;
                for (Parameter p : parameters){
                    if (p.getType() == AnnotationProcessorContext.class || p.getType() == MutableAnnotationProcessorContext.class){
                        args[index++] = context;
                        continue;
                    }
                    String paramName = "";
                    if (p.isAnnotationPresent(Param.class)){
                        final Param declaredAnnotation = p.getDeclaredAnnotation(Param.class);
                        paramName = declaredAnnotation.value();
                    }
                    try{
                        final Object bean = AnnotationProcessorContextHelper.findBean(p.getType(), paramName, context);
                        args[index++] = bean;
                    }
                    catch (BeanNotFoundException e){
                        //  找不到填null
                        args[index++] = null;
                    }
                }
                final Object invoke = initMethod.invoke(annotationProcessObj, args);
                context.registerBean(beanName, invoke);
            }
        }
    }

    static void closeContext(MutableAnnotationProcessorContext context, Object annotationProcessObj) throws InvocationTargetException, IllegalAccessException {
        final Class<?> annotationProcessObjClass = annotationProcessObj.getClass();
        final Set<Method> declaredMethods = ReflectUtility.getAllMethods(annotationProcessObjClass);
        Method destroyMethod = null;
        String beanName = null;
        for (Method m : declaredMethods){
            if (AnnotationUtility.isDeclaredAnnotationPresent(m, Destroy.class)) {
                // 标记了@Initialization
                destroyMethod = m;
                beanName = Destroy.class.getSimpleName();
                break;
            }
            if (AnnotationUtility.isDeclaredAnnotationPresent(m, PreDestroy.class)){
                // 标记了@PostConstruct
                destroyMethod = m;
                beanName = PreDestroy.class.getSimpleName();
                break;
            }
        }
        if (destroyMethod != null){
            // 允许访问私有方法和继承性方法
            if (!destroyMethod.isAccessible()){
                destroyMethod.setAccessible(true);
            }
            final Class<?>[] parameterTypes = destroyMethod.getParameterTypes();
            if (parameterTypes.length == 0){
                // 直接调用
                final Object invoke = destroyMethod.invoke(annotationProcessObj);
                context.registerBean(beanName, invoke);
                return;
            }
            if (parameterTypes.length == 1 && (AnnotationProcessorContext.class == parameterTypes[0] || MutableAnnotationProcessorContext.class == parameterTypes[0])){
                final Object invoke = destroyMethod.invoke(annotationProcessObj, context);
                context.registerBean(beanName, invoke);
                return;
            }
            // 两个以上的参数
            if (parameterTypes.length > 1){
                final Parameter[] parameters = destroyMethod.getParameters();
                Object[] args = new Object[parameters.length];
                int index = 0;
                for (Parameter p : parameters){
                    if (p.getType() == AnnotationProcessorContext.class || p.getType() == MutableAnnotationProcessorContext.class){
                        args[index++] = context;
                        continue;
                    }
                    args[index++] = null;
                }
                final Object invoke = destroyMethod.invoke(annotationProcessObj, args);
                context.registerBean(beanName, invoke);
            }
        }
    }

    /**
     * 执行一个注解处理器的特定阶段
     *
     * @param lifeCyclePhase2 执行阶段
     * @param bean 注解处理器环境对象
     * @param context 注解处理器上下文环境
     * @param phaseAnnotationsClass 注解处理器支持处理的注解
     * @param annotationTargetClass 要处理的注解
     * @param <T> 注解处理器类型
     * @throws BeanNotFoundException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     */
    @SuppressWarnings("all")
    static <T> void firePhase(LifeCyclePhase lifeCyclePhase2, AnnotationProcessingEnvironmentBean<T> bean, MutableAnnotationProcessorContext context,
                              Class<? extends Annotation>[] phaseAnnotationsClass, Class<Annotation> annotationTargetClass) throws BeanNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        // 阶段方法
        final Map<Method, List<Annotation>> phaseMap = bean.getPhaseMap(lifeCyclePhase2);
        Map<Integer, List<Method>> orderMethodCallMap = new TreeMap<>();

        for (Map.Entry<Method, List<Annotation>> entry : phaseMap.entrySet()){
            // Scanning阶段注解
            final List<Annotation> value = entry.getValue();
            final Method key = entry.getKey();
            boolean next = false;
            int order = -1;
            for (Annotation annotation : value){
//                final ScanPhase scanPhase = LifeCyclePhase.SCANNING.asPhaseAnnotation(annotation);
                final boolean override = AnnotationUtility.getAnnotationAttributeValue(annotation, "override", boolean.class);;
                order = AnnotationUtility.getAnnotationAttributeValue(annotation, "order", int.class);
                final Class<? extends Annotation>[] resolveAnnotation = AnnotationUtility.getAnnotationAttributeValue(annotation, "value", Class[].class);
                if (override){
                    // 复写，我们则按照scanPhase.value()进行匹配
                    if (ArrayUtility.contain(resolveAnnotation, annotationTargetClass)){
                        next = true;
                        break;
                    }
                }
                else if (resolveAnnotation.length > 0){
                    // 注解融合
                    // 复写
                    HashSet<Class<? extends Annotation>> set = new HashSet<>(Arrays.asList(phaseAnnotationsClass));
                    set.addAll(Arrays.asList(resolveAnnotation));
                    if (set.contains(annotationTargetClass)){
                        // 有交集, 调用
                        next = true;
                        break;
                    }
                }
                else{
                    // 使用phaseAnnotationsClass来判断
                    if (ArrayUtility.contain(phaseAnnotationsClass, annotationTargetClass)){
                        // 有交集, 调用
                        next = true;
                        break;
                    }
                }
            }
            if (next){
                // 添加此方法
                final List<Method> methods = orderMethodCallMap.computeIfAbsent(order, m -> new ArrayList<>());
                methods.add(key);
            }
        }

        final Class<T> annotationProcessorClass = bean.getAnnotationProcessorClass();
        // 按照顺序进行调用
        for (Map.Entry<Integer, List<Method>> entry : orderMethodCallMap.entrySet()){
            final List<Method> value = entry.getValue();
            for (Method m : value){
                final Object annotationProcessor = context.getAnnotationProcessor(annotationProcessorClass);
                final Parameter[] parameterTypes = m.getParameters();
                Object[] params = new Object[parameterTypes.length];
                int index = 0;
                for (Parameter parameter : parameterTypes){
                    boolean must = false;
                    Param param = null;
                    if (AnnotationUtility.isDeclaredAnnotationPresent(parameter, Param.class)) {
                        param = parameter.getDeclaredAnnotation(Param.class);
                        must = param.must();
                    }
                    // 如果是context本身, 则我们提供context
                    if (AnnotationProcessorContext.class.isAssignableFrom(parameter.getType())){
                        params[index++] = context;
                        continue;
                    }
                    // 通过类型获取Bean
                    try {
                        Object beanByType = context.getBeanByType(parameter.getType());
                        if (beanByType == null){
                            beanByType = context.getBeanByInheritType(parameter.getType());
                        }
                        // 查看泛型是否对得上
                        final Type parameterizedType = parameter.getParameterizedType();
                        if (beanByType != null && parameterizedType.getTypeName().contains("<")){
                            final TypeReference<?> typeReference = context.getTypeReference(beanByType);
                            final Type type = typeReference.getType();
                            if (TypeReference.typeMatch(type, parameterizedType, true)){
                                params[index++] = beanByType;
                                continue;
                            }
                        }
                        if (must && beanByType == null){
                            throw new BeanNotFoundException("找不到类型为" + parameter.getType() + "的Bean, 开启按照名字查找");
                        }
                        params[index++] = beanByType;
                    }
                    catch (MoreThenOneBeanException | BeanNotFoundException e) {
                        // 获取是否标记@Param
                        if (param != null){
                            final String value1 = param.value();
                            final LifeCyclePhase lifeCyclePhase = param.phaseRet();
                            Object paramObj = null;
                            if (StringUtility.isBlank(value1)){
                                // 处理LifeCyclePhase
                                paramObj = context.getPhaseReturnValue(lifeCyclePhase);
                            }
                            else{
                                //  处理按名字查找
                                paramObj = context.getBeanByName(param.value());
                            }
                            if (paramObj == null && must){
                                // 必须要有但是找不到
                                throw new BeanNotFoundException("找不到名字为" + param.value() + "的Bean, 因此抛出异常", e);
                            }
                            params[index++] = paramObj;
                        }
                    }
                }
                final Object phaseResult = m.invoke(annotationProcessor, params);
                final Class<?> returnType = m.getReturnType();
                if (returnType != void.class && returnType != Void.class){
                    context.setPhaseReturnValue(lifeCyclePhase2, phaseResult);
                }
            }
        }
    }
    @SuppressWarnings("all")
    static <T> void firePhase(LifeCyclePhase lifeCyclePhase2, AnnotationProcessingEnvironmentBean<T> bean, MutableAnnotationProcessorContext context, Class<Annotation> annotationTargetClass) throws IllegalAccessException, BeanNotFoundException, InvocationTargetException, NoSuchMethodException {

        Class<? extends Annotation>[] resolveCache = context.getResolveCache(bean.getAnnotationProcessorClass());
        if (resolveCache == null){
            // 注解处理器处理哪些注解
            final AnnotationProcessor annotationProcessor = bean.getAnnotationProcessor();
            final Class<? extends Annotation>[] resolves = annotationProcessor.resolves();
            final Class<?>[] value = annotationProcessor.value();
            HashSet<Class<? extends Annotation>> resolvesAnnotationMap = new HashSet<>();
            Collections.addAll(resolvesAnnotationMap, resolves);
            for (Class<?> c : value){
                if (c.isAnnotation()){
                    Class<? extends Annotation> cl = (Class<? extends Annotation>) c;
                    resolvesAnnotationMap.add(cl);
                }
            }
            resolveCache = resolvesAnnotationMap.toArray(new Class[0]);
            context.setResolveCache(bean.getAnnotationProcessorClass(), resolveCache);
        }
        firePhase(lifeCyclePhase2, bean, context, resolveCache, annotationTargetClass);
    }

}
