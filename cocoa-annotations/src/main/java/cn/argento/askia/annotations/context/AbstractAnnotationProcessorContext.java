package cn.argento.askia.annotations.context;

import cn.argento.askia.annotations.support.LifeCyclePhase;
import cn.argento.askia.utilities.collection.CollectionUtility;
import cn.argento.askia.utilities.text.LoggerUtility;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractAnnotationProcessorContext implements AnnotationProcessorContext{

    // 注解处理器
    protected Map<Class<?>, Object> annotationProcessorList;

    // 返回值Map
    protected Map<LifeCyclePhase, Object> lifeCyclePhaseReturnObjectMap;

    // Bean容器
    protected Map<String, Object> nameMap;
    // 类型名称映射
    protected Map<Type, List<String>> typeIndex;

    protected AbstractAnnotationProcessorContext(){
        annotationProcessorList = new HashMap<>();
        lifeCyclePhaseReturnObjectMap = new ConcurrentHashMap<>();
        nameMap = new ConcurrentHashMap<>();
        typeIndex = new ConcurrentHashMap<>();
    }

    @Override
    public List<Object> getAnnotationProcessors() {
        return new ArrayList<>(annotationProcessorList.values());
    }

    @Override
    public <T> T getAnnotationProcessor(Class<T> annotationProcessorClass) {
        final Object annotationProcessor = annotationProcessorList.get(annotationProcessorClass);
        if (annotationProcessor == null){
            return null;
        }
        return annotationProcessorClass.cast(annotationProcessor);
    }

    @Override
    public Object getPhaseReturnValue(LifeCyclePhase phase) {
        return lifeCyclePhaseReturnObjectMap.get(phase);
    }

    @Override
    public <T> T getPhaseReturnValue(LifeCyclePhase phase, Class<T> tClass) {
        final Object o = lifeCyclePhaseReturnObjectMap.get(phase);
        if (o == null){
            return null;
        }
        return tClass.cast(o);
    }

    @Override
    @SuppressWarnings("all")
    public <T> T getBeanByType(Type tClass) throws MoreThenOneBeanException, BeanNotFoundException {
        final List<String> stringList = typeIndex.get(tClass);
        if (stringList == null || stringList.size() == 0){
            throw new BeanNotFoundException("bean type: " + tClass + ", not found");
        }
        else if (stringList.size() > 1){
            throw new MoreThenOneBeanException("found more than one bean in Container, please tell me which you want? "+ CollectionUtility.toString(stringList));
        }
        final String s = stringList.get(0);
        final Object o = nameMap.get(s);
        if (o == null){
            throw new BeanNotFoundException(LoggerUtility.log("bean name = {}, type = {} not found", s, tClass));
        }
        return (T) o;
    }

    @Override
    public <T> T getBeanByName(String name, Class<T> tClass) {
        final Object beanByName = getBeanByName(name);
        if (beanByName == null){
            return null;
        }
        return tClass.cast(beanByName);
    }

    @Override
    public Object getBeanByName(String name) {
        return nameMap.get(name);
    }

    @Override
    public long getBeanCount() {
        return nameMap.size();
    }

    @Override
    public String[] getAllBeanNames() {
        return nameMap.keySet().toArray(new String[0]);
    }


    protected void close(){
        annotationProcessorList.clear();
        lifeCyclePhaseReturnObjectMap.clear();
        nameMap.clear();
        typeIndex.clear();
    }
}
