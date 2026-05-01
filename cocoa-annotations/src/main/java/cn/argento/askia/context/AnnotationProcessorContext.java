package cn.argento.askia.context;

import cn.argento.askia.supports.LifeCyclePhase;

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

    Object getAnnotationProcessor(Class<?> annotationProcessorClass);

    Object getPhaseReturnValue(LifeCyclePhase phase);

    default <T> T getPhaseReturnValue(LifeCyclePhase phase, Class<T> tClass){
        final Object phaseReturnValue = getPhaseReturnValue(phase);
        return tClass.cast(phaseReturnValue);
    }

    <T> T getBeanByType(Class<T> tClass) throws MoreThenOneBeanException;

    /**
     * 获取继承关系中的大Bean
     * @param tClass 和getBeanByType的参数一样
     * @param <T>
     * @return
     * @throws MoreThenOneBeanException
     */
    <T> T getBeanByInheritType(Class<T> tClass) throws MoreThenOneBeanException;

    <T> T getBeanByName(String name, Class<T> tClass);

    Object getBeanByName(String name);

    long getBeanCount();

    String[] getAllBeanNames();
}
