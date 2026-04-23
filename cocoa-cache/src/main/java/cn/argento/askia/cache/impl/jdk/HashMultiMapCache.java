package cn.argento.askia.cache.impl.jdk;

import cn.argento.askia.cache.support.MapValueSupport;

import java.util.Map;

public class HashMultiMapCache<K, K2, V> extends AbstractHashMapCacheDelegation<K, Map<K2, V>> implements MapValueSupport<K2, V> {
    private HashMapCache<K, Map<K2, V>> mapHashMapCache;

    public HashMultiMapCache(){
        mapHashMapCache = new HashMapCache<>();
    }

    public HashMultiMapCache(int initialCapacity, float loadFactor){
        mapHashMapCache = new HashMapCache<>(initialCapacity, loadFactor);
    }
    public HashMultiMapCache(int initialCapacity){
        mapHashMapCache = new HashMapCache<>(initialCapacity);
    }



    @Override
    protected HashMapCache<K, Map<K2, V>> delegate() {
        return mapHashMapCache;
    }
}
