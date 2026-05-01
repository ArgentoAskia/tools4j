package cn.argento.askia.supports.beans;

import cn.argento.askia.utilities.lang.AssertionUtility;

import java.lang.annotation.Annotation;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class BaseEnvironmentBean {

    private final Class<Annotation> solveAnnotation;

    private final Map<Class<?>, Object> annotationProcessorMap;

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

    /**
     * Builder专用构造器
     * @param solveAnnotation
     * @param annotationProcessorMap
     */
    BaseEnvironmentBean(Class<Annotation> solveAnnotation, Map<Class<?>, Object> annotationProcessorMap) {
        this.solveAnnotation = solveAnnotation;
        this.annotationProcessorMap = annotationProcessorMap;
    }

    public static BaseEnvironmentBean valueOf(Class<Annotation> solveAnnotation, Object... annotationProcessors){
        if (annotationProcessors == null){
            annotationProcessors = new Object[0];
        }
        return new BaseEnvironmentBean(solveAnnotation, annotationProcessors);
    }

    public static BaseEnvironmentBeanBuilder builder() {
        return new BaseEnvironmentBeanBuilder();
    }

    public static final class BaseEnvironmentBeanBuilder {
        private Class<Annotation> solveAnnotation;
        private Map<Class<?>, Object> annotationProcessorMap;

        private BaseEnvironmentBeanBuilder() {
            annotationProcessorMap = new HashMap<>();
        }

        public BaseEnvironmentBeanBuilder processAnnotation(Class<Annotation> solveAnnotation) {
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
