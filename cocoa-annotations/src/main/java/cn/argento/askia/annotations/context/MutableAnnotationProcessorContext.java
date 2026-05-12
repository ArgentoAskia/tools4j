package cn.argento.askia.annotations.context;

import cn.argento.askia.langs.TypeReference;
import cn.argento.askia.annotations.support.LifeCyclePhase;

import java.lang.annotation.Annotation;

/**
 * 框架内部可修改接口
 */
public interface MutableAnnotationProcessorContext extends AnnotationProcessorContext{
    void setPhaseReturnValue(LifeCyclePhase phase, Object value);
    void setResolveCache(Class<?> annotationProcessClass, Class<? extends Annotation>[] resolveClasses);
    Class<? extends Annotation>[] getResolveCache(Class<?> annotationProcessClass);
    void registerBean(String name, Object bean);
    <T> void registerGenericBean(String name, Object bean, TypeReference<T> typeReference);
    TypeReference<?> getTypeReference(Object bean);
    void close();
}
