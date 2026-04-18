package cn.argento.askia.cache.impl.jdk;

import cn.argento.askia.cache.Cache;
import cn.argento.askia.cache.impl.AbstractCacheImpl;

import java.util.HashMap;
import java.util.Map;

public class HashMapCache<K, V> extends AbstractCacheImpl implements Cache<K, V> {

    private transient HashMap<K, V> hashMap;
    private transient boolean isClose;
    private void initStatus(){
        isClose = false;
    }
    public HashMapCache(){
        hashMap = new HashMap<>();
        initStatus();
    }
    public HashMapCache(int initialCapacity, float loadFactor){
        hashMap = new HashMap<>(initialCapacity,loadFactor);
        initStatus();
    }
    public HashMapCache(int initialCapacity){
        hashMap = new HashMap<>(initialCapacity);
        initStatus();
    }
    public HashMapCache(Map<? extends K, ? extends V> m){
        initStatus();
        hashMap = new HashMap<>();
        putAll(m);
    }
    // todo 拷贝构造函数
//    protected HashMapCache(HashMapCache<? extends K, ? extends V> m){
//
//    }


//    public HashMapCache(AbstractCacheImpl abstractCache){
//
//    }

    // 解包adaptar
    private void checkStatus(){
        if (isClose){
            throw new IllegalStateException("缓存池已关闭, 所有缓存均已清空, 无法再对缓存进行任何操作");
        }
    }

    @Override
    public V put(K key, V value) {
        checkStatus();
        return hashMap.put(key, value);
    }

    @Override
    public void clear() {
        checkStatus();
        hashMap.clear();
    }

    @Override
    public V delete(K key) {
        checkStatus();
        return hashMap.remove(key);
    }

    @Override
    public V get(K key) {
        checkStatus();
        return hashMap.get(key);
    }

    @Override
    public boolean containsKey(K key) {
        checkStatus();
        return hashMap.containsKey(key);
    }

    @Override
    public long size() {
        checkStatus();
        return hashMap.size();
    }

    @Override
    public void close() {
        // 删除所有的成员
        if (hashMap.size() > 0){
            // 有成员
            clear();        // 清除所有成员
        }
        // 触发gc回收缓存
        hashMap = null;
    }
}
