package cn.argento.askia.cache.impl.jdk;

import cn.argento.askia.cache.ListValueCache;

import java.util.List;

public class HashListMapCache<K, V> extends AbstractHashMapCacheDelegation<K, List<V>> implements ListValueCache<K, V> {
    private final HashMapCache<K, List<V>> listHashMapCache;

    public HashListMapCache(){
        listHashMapCache = new HashMapCache<>();
    }
    public HashListMapCache(int initialCapacity, float loadFactor){
        listHashMapCache = new HashMapCache<>(initialCapacity, loadFactor);
    }
    public HashListMapCache(int initialCapacity){
        listHashMapCache = new HashMapCache<>(initialCapacity);
    }

    @Override
    protected HashMapCache<K, List<V>> delegate() {
        return listHashMapCache;
    }
}
