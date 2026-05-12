package cn.argento.askia.annotations.context;

import cn.argento.askia.annotations.support.LifeCyclePhase;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 注解处理器全局上下文
 */
public interface AnnotationProcessorContext {

    /**
     * 获取处理此注解的所有注解处理器
     * @return
     */
    List<Object> getAnnotationProcessors();

    <T> T getAnnotationProcessor(Class<T> annotationProcessorClass);

    Object getPhaseReturnValue(LifeCyclePhase phase);

    default <T> T getPhaseReturnValue(LifeCyclePhase phase, Class<T> tClass){
        final Object phaseReturnValue = getPhaseReturnValue(phase);
        return tClass.cast(phaseReturnValue);
    }

    <T> T getBeanByType(Type tClass) throws MoreThenOneBeanException, BeanNotFoundException;

    <T> T getBeanByName(String name, Class<T> tClass);

    Object getBeanByName(String name);

    long getBeanCount();

    String[] getAllBeanNames();
}
