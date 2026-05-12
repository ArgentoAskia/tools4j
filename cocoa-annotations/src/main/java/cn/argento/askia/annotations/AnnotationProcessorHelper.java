package cn.argento.askia.annotations;

import cn.argento.askia.annotations.annotation.AnnotationProcessor;
import cn.argento.askia.annotations.annotation.Param;
import cn.argento.askia.annotations.annotation.phase.*;
import cn.argento.askia.annotations.context.AnnotationProcessorContext;
import cn.argento.askia.annotations.context.BeanNotFoundException;
import cn.argento.askia.annotations.context.MoreThenOneBeanException;
import cn.argento.askia.annotations.context.MutableAnnotationProcessorContext;
import cn.argento.askia.langs.TypeReference;
import cn.argento.askia.annotations.support.LifeCyclePhase;
import cn.argento.askia.utilities.annotation.AnnotationUtility;
import cn.argento.askia.utilities.collection.ArrayUtility;
import cn.argento.askia.utilities.collection.CollectionUtility;
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
        else{
            throw new UnsupportedOperationException("暂不支持解析该阶段" + annotation.annotationType());
        }
    }

    /**
     * 执行一个注解处理器的特定阶段
     *
     * @param lifeCyclePhase2 执行阶段
     * @param bean 注解处理器环境对象
     * @param context 注解处理器上下文环境
     * @param annotationTargetClass 注解处理器处理的注解
     * @param <T> 注解处理器类型
     * @throws BeanNotFoundException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     */
    @SuppressWarnings("all")
    static <T> void firePhase(Class<? extends Annotation>[] resolveAnnotationTargetClasses, LifeCyclePhase lifeCyclePhase2, Object annotationProcessor, AnnotationProcessingEnvironmentBean<T> bean, MutableAnnotationProcessorContext context) throws BeanNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        // 获取所有标记了注解的方法
        final Map<Method, List<Annotation>> phaseMap = bean.getPhaseMap(lifeCyclePhase2);
        // 方法调用次序
        Map<Integer, List<Method>> orderMethodCallMap = new TreeMap<>();
        // 方法上标记的注解
        Map<Method, Set<Class<? extends Annotation>>> orderMethodResolveAnnotationSet = new HashMap<>();
        for (Map.Entry<Method, List<Annotation>> entry : phaseMap.entrySet()){
            // Scanning阶段注解
            final List<Annotation> value = entry.getValue();
            final Method key = entry.getKey();
            int order = -1;
            Set<Class<? extends Annotation>> resolveSet = new HashSet<>();
            for (Annotation annotation : value){
//                final ScanPhase scanPhase = LifeCyclePhase.SCANNING.asPhaseAnnotation(annotation);
                final boolean override = AnnotationUtility.getAnnotationAttributeValue(annotation, "override", boolean.class);;
                order = AnnotationUtility.getAnnotationAttributeValue(annotation, "order", int.class);
                final Class<? extends Annotation>[] resolveAnnotation = AnnotationUtility.getAnnotationAttributeValue(annotation, "value", Class[].class);
                if (override && resolveAnnotation.length > 0){
                    // 复写且标记了复写的注解，则我们需要使用复写阶段的注解覆盖掉resolveAnnotationTargetClasses
                    Collections.addAll(resolveSet, resolveAnnotation);
                }
                else if (resolveAnnotation.length > 0){
                    // override = false
                    Collections.addAll(resolveSet, resolveAnnotationTargetClasses);
                    Collections.addAll(resolveSet, resolveAnnotation);
                }
                else{
                    Collections.addAll(resolveSet, resolveAnnotationTargetClasses);
                }
            }
            // 添加此注解到容器
            orderMethodResolveAnnotationSet.put(key, resolveSet);
            // 添加此方法
            final List<Method> methods = orderMethodCallMap.computeIfAbsent(order, m -> new ArrayList<>());
            methods.add(key);
        }
        final Class<T> annotationProcessorClass = bean.getAnnotationProcessorClass();
        // 按照顺序进行调用
        for (Map.Entry<Integer, List<Method>> entry : orderMethodCallMap.entrySet()){
            final List<Method> value = entry.getValue();
            for (Method m : value){
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
                    // 特殊参数处理
                    // 如果是context本身, 则我们提供context
                    if (AnnotationProcessorContext.class.isAssignableFrom(parameter.getType())){
                        params[index++] = context;
                        continue;
                    }
                    // 注解集合
                    System.out.println(parameter.getParameterizedType().getTypeName());
                    if (Set.class == parameter.getType() && parameter.getParameterizedType().getTypeName().equals("java.util.Set<java.lang.Class<? extends java.lang.annotation.Annotation>>")){
                        // 集合参数
                        final Set<Class<? extends Annotation>> classes = orderMethodResolveAnnotationSet.get(m);
                        params[index++] = classes;
                        continue;
                    }
                    //
                    // 通过类型获取Bean
                    try {
                        Object beanByType = context.getBeanByType(parameter.getType());
                        // 查看泛型是否对得上
                        final Type parameterizedType = parameter.getParameterizedType();
                        if (beanByType != null && parameterizedType.getTypeName().contains("<")){
                            final TypeReference<?> typeReference = context.getTypeReference(beanByType);
                            if (typeReference != null){
                                final Type type = typeReference.getType();
                                if (TypeReference.typeMatch(type, parameterizedType, true)){
                                    params[index++] = beanByType;
                                    continue;
                                }
                            }
                            // 没有在容器中发现泛型信息, 检查是否是必须参数
                            else{
                                // 清空bean
                                beanByType = null;
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
                                if (lifeCyclePhase != LifeCyclePhase.NO){
                                    paramObj = context.getPhaseReturnValue(lifeCyclePhase);
                                }
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
                    // 设置上一个阶段
                    LifeCyclePhase.setLastPhase(lifeCyclePhase2);
                }
            }
        }
    }
    @SuppressWarnings("all")
    static <T> void firePhase(LifeCyclePhase lifeCyclePhase2, Object annotationProcess, AnnotationProcessingEnvironmentBean<T> bean, MutableAnnotationProcessorContext context, Class<? extends Annotation>... resolveAnnotationTargetClasses) throws IllegalAccessException, BeanNotFoundException, InvocationTargetException, NoSuchMethodException {

        Class<? extends Annotation>[] resolveCache = context.getResolveCache(bean.getAnnotationProcessorClass());
        if (resolveCache == null){
            // 拼接AnnotationProcessor处理器中value()和resolves()的值
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
            // 生成resolveCache并保存
            resolveCache = resolvesAnnotationMap.toArray(new Class[0]);
            context.setResolveCache(bean.getAnnotationProcessorClass(), resolveCache);
        }
        // 点燃阶段方法
        firePhase(resolveCache, lifeCyclePhase2, annotationProcess, bean, context);
    }
}
