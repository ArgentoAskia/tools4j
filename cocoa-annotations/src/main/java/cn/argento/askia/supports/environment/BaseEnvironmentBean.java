package cn.argento.askia.supports.environment;

import cn.argento.askia.utilities.lang.AssertionUtility;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * 基础环境信息支持
 */
public class BaseEnvironmentBean implements EnvironmentBean<BaseEnvironmentBean>{

    private Class<Annotation> solveAnnotation;

    private final Map<Class<?>, Object> annotationProcessorMap;

    /**
     * valueOf专用构造器
     * @param solveAnnotation
     * @param annotationProcessors
     */
    private BaseEnvironmentBean(Class<Annotation> solveAnnotation, Object[] annotationProcessors){
        AssertionUtility.requireArrayAtLeastOneMember(annotationProcessors);
        this.solveAnnotation = solveAnnotation;
        annotationProcessorMap = new HashMap<>();
        for (Object annotationProcessor : annotationProcessors){
            annotationProcessorMap.put(annotationProcessor.getClass(), annotationProcessor);
        }
    }

    public Class<Annotation> getSolveAnnotation() {
        return solveAnnotation;
    }

    public Map<Class<?>, Object> getAnnotationProcessorMap() {
        return annotationProcessorMap;
    }

    public <T> T getAnnotationProcessor(Class<T> tClass){
        final Object o = annotationProcessorMap.get(tClass);
        if (o != null){
            return tClass.cast(o);
        }
        else{
            return null;
        }
    }

    public static BaseEnvironmentBean valueOf(Class<Annotation> solveAnnotation, Object... annotationProcessors){
        if (annotationProcessors == null){
            annotationProcessors = new Object[0];
        }
        return new BaseEnvironmentBean(solveAnnotation, annotationProcessors);
    }

    /**
     * Builder专用构造器
     * @param solveAnnotation
     * @param annotationProcessorMap
     */
    BaseEnvironmentBean(Class<Annotation> solveAnnotation, Map<Class<?>, Object> annotationProcessorMap) {
        this.solveAnnotation = solveAnnotation;
        this.annotationProcessorMap = annotationProcessorMap;
    }



    public static BaseEnvironmentBeanBuilder builder() {
        return new BaseEnvironmentBeanBuilder();
    }

    @Override
    public BaseEnvironmentBean getBean(BaseEnvironmentBean environmentBean) {
        this.annotationProcessorMap.putAll(environmentBean.getAnnotationProcessorMap());
        this.solveAnnotation = environmentBean.solveAnnotation;
        return this;
    }

    public static class BaseEnvironmentBeanBuilder {
        protected Class<Annotation> solveAnnotation;
        protected final Map<Class<?>, Object> annotationProcessorMap;

        BaseEnvironmentBeanBuilder() {
            annotationProcessorMap = new HashMap<>();
        }

        public BaseEnvironmentBeanBuilder addProcessAnnotation(Class<Annotation> solveAnnotation) {
            this.solveAnnotation = solveAnnotation;
            return this;
        }

        public BaseEnvironmentBeanBuilder addAnnotationProcessorMap(Map<Class<?>, Object> annotationProcessorMap) {
            this.annotationProcessorMap.putAll(annotationProcessorMap);
            return this;
        }

        public BaseEnvironmentBeanBuilder addAnnotationProcessor(Object annotationProcessor){
            this.annotationProcessorMap.put(annotationProcessor.getClass(), annotationProcessor);
            return this;
        }

        public BaseEnvironmentBean build() {
            return new BaseEnvironmentBean(solveAnnotation, annotationProcessorMap);
        }
    }
}
