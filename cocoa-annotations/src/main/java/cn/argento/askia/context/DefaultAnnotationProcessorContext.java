package cn.argento.askia.context;

import cn.argento.askia.langs.TypeReference;
import cn.argento.askia.supports.LifeCyclePhase;
import cn.argento.askia.utilities.lang.StringUtility;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DefaultAnnotationProcessorContext extends AbstractAnnotationProcessorContext
        implements MutableAnnotationProcessorContext{


    private ResolveAnnotationCache resolveCache;

    public DefaultAnnotationProcessorContext(){
        resolveCache = new ResolveAnnotationCache();
    }

    @Override
    public void setPhaseReturnValue(LifeCyclePhase phase, Object value) {
        lifeCyclePhaseReturnObjectMap.put(phase, value);
    }

    @Override
    public void setResolveCache(Class<?> annotationProcessClass, Class<? extends Annotation>[] resolveClasses) {
        resolveCache.put(annotationProcessClass, resolveClasses);
    }

    @Override
    public Class<? extends Annotation>[] getResolveCache(Class<?> annotationProcessClass) {
        return resolveCache.get(annotationProcessClass);
    }

    @SuppressWarnings("all")
    private String createDefaultUniqueName(Object bean){
        final Class<?> aClass = bean.getClass();
        final List<String> stringList = typeIndex.get(aClass);
        return aClass.getName() + "#" + stringList.size();
    }

//    private void indexBean(Class<?> clazz, String name) {
//        if (clazz == null || clazz == Object.class) return;
//        typeIndex.computeIfAbsent(clazz, k -> new CopyOnWriteArrayList<>()).add(name);
//        for (Class<?> iface : clazz.getInterfaces()) {
//            typeIndex.computeIfAbsent(iface, k -> new CopyOnWriteArrayList<>()).add(name);
//        }
//        indexBean(clazz.getSuperclass(), name);
//    }

    @Override
    public void registerBean(String name, Object bean) {
        if (StringUtility.isBlank(name)){
            // 空命名则使用类型
            nameMap.put(createDefaultUniqueName(bean), bean);
        }
        else{
            nameMap.put(name, bean);
        }
    }

    @Override
    public <T> void registerGenericBean(String name, Object bean, TypeReference<T> typeReference) {
        if (StringUtility.isBlank(name)){
            // 空命名则使用类型
            name = createDefaultUniqueName(bean);
        }
        nameMap.put(name, bean);

    }

    @Override
    public TypeReference<?> getTypeReference(Object bean) {
        return null;
    }

    @Override
    public void close() {

    }
}
