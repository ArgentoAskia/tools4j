package cn.argento.askia.cache;

import java.io.Closeable;
import java.util.*;
import java.util.function.*;

/**
 * 缓存顶层接口.
 * <p>此接口是所有缓存适配实现的顶层接口, 我们希望为市面上的所有缓存组件和软件提供一个通用的Api, 因此设计了此接口</p>
 * <p>此接口只包含基础的增删改查缓存操作, 更多子功能可以参见该顶层接口的子接口</p>
 * <p>此接口仅考虑key的泛型, 子实现如果需要具体的类型区别请自行覆盖返回值</p>
 * <p>此接口基于{@link Map}接口而来</p>
 */
public interface Cache<K, V> {
    // 增
    V put(K key, V value);

    // 删
    void clear();
    V delete(K key);
    default boolean delete(K key, V value){
        if (containsKey(key)){
            if (get(key).equals(value)){
                delete(key);
                return true;
            }
        }
        return false;
    }

    // 改
    default V replace(K key, V newValue){
        final V oldValue = get(key);
        put(key, newValue);
        return oldValue;
    }
    default boolean replace(K key, V oldValue, V newValue) {
        Object curValue = get(key);
        if (!Objects.equals(curValue, oldValue) ||
                (curValue == null && !containsKey(key))) {
            return false;
        }
        put(key, newValue);
        return true;
    }

    // 查(获取)
    V get(K key);
    default V get(Object objKey, Function<Object, K> exchangeFn){
        final K apply = exchangeFn.apply(objKey);
        return get(apply);
    }
    default V getOrDefault(K key, V defaultValue) {
        if (containsKey(key)){
            return get(key);
        }
        else{
            return defaultValue;
        }
    }
    boolean containsKey(K key);
    default boolean containsKey(Object key, Function<Object, K> exchangeFn){
        final K apply = exchangeFn.apply(key);
        return containsKey(apply);
    }
    long size();
    default boolean isEmpty(){
        return size() == 0;
    }

    // --------------- 批量操作 ---------------
    default Map<K, V> getAll(Collection<? extends K> keys, Supplier<Map<K, V>> mapSupplier){
        if (keys == null){
            return null;
        }
        if (keys.size() == 0){
            return new HashMap<>();
        }
        Map<K, V> retMap = mapSupplier.get();
        for (K key : keys){
            final V v = get(key);
            retMap.put(key, v);
        }
        return retMap;
    }
    default Map<K, V> getAll(Collection<? extends K> keys){
        return getAll(keys, HashMap::new);
    }
    default int putAll(Map<? extends K, ? extends V> map){
        if (map == null){
            throw new IllegalArgumentException("map not null");
        }
        int count = 0;
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()){
            final K key = entry.getKey();
            final V value = entry.getValue();
            put(key, value);
            count++;
        }
        return count;
    }
    default int deleteAll(Collection<? extends K> keys){
        if (keys == null || keys.size() == 0){
            return 0;
        }
        int count = 0;
        for (K key : keys){
            delete(key);
            count++;
        }
        return count;
    }
    // --------------- 批量操作 ---------------

    // --------------- 生命周期 ---------------
    void close();  // 释放资源（如 Redis 连接池）

    // --------------- 原子操作 ---------------
    default V putIfAbsent(K key, V value) {
        if (containsKey(key)){
            return get(key);
        }
        else{
            put(key, value);
            return value;
        }
    }
    default V compute(K key,
                      BiFunction<? super K, ? super V, ? extends V> remappingFunction){
        return null;
    }
    default V computeIfAbsent(K key,
                              Function<? super K, ? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        V v;
        if ((v = get(key)) == null) {
            V newValue;
            if ((newValue = mappingFunction.apply(key)) != null) {
                put(key, newValue);
                return newValue;
            }
        }

        return v;
    }
    default V computeIfPresent(K key,
                               BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        V oldValue;
        if ((oldValue = get(key)) != null) {
            V newValue = remappingFunction.apply(key, oldValue);
            if (newValue != null) {
                put(key, newValue);
                return newValue;
            } else {
                delete(key);
                return null;
            }
        } else {
            return null;
        }
    }

    default V merge(K key, V value,
                    BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(value);
        V oldValue = get(key);
        V newValue = (oldValue == null) ? value :
                remappingFunction.apply(oldValue, value);
        if(newValue == null) {
            delete(key);
        } else {
            put(key, newValue);
        }
        return newValue;
    }
    // --------------- 原子操作 ---------------
}
