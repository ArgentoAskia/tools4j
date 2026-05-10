package cn.argento.askia.context;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResolveAnnotationCache {
    private Map<Class<?>, Class<? extends Annotation>[]> resolveCacheMap;

    public ResolveAnnotationCache(){
        resolveCacheMap = new HashMap<>();
    }

    public Map<Class<?>, Class<? extends Annotation>[]> getResolveCacheMap() {
        return resolveCacheMap;
    }


    public Class<? extends Annotation>[] get(Class<?> tClass){
        return resolveCacheMap.get(tClass);
    }

    public void put(Class<?> tClass, Class<? extends Annotation>[] resolveAnnotations){
        resolveCacheMap.put(tClass, resolveAnnotations);
    }


    public int size(){
        return resolveCacheMap.size();
    }
}
