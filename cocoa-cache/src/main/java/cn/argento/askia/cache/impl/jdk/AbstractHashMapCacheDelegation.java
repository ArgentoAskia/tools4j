package cn.argento.askia.cache.impl.jdk;

import cn.argento.askia.cache.Cache;

// HashMap实现内部装饰器
public abstract class AbstractHashMapCacheDelegation<K, V> implements Cache<K, V>{

    protected abstract HashMapCache<K, V> delegate();

    @Override
    public V put(K key, V value) {
        return delegate().put(key, value);
    }

    @Override
    public void clear() {
        delegate().clear();
    }

    @Override
    public V delete(K key) {
        return delegate().delete(key);
    }

    @Override
    public V get(K key) {
        return delegate().get(key);
    }

    @Override
    public boolean containsKey(K key) {
        return delegate().containsKey(key);
    }

    @Override
    public long size() {
        return delegate().size();
    }

    @Override
    public void close() {
        delegate().close();
    }
}
