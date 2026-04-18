package cn.argento.askia.cache.support;

import java.util.function.BinaryOperator;

/**
 * 缓存原子操作支持
 */
public interface AtomicSupport<K, V> {
    boolean compareAndSet(K key, V expired, V update);
    V getAndDelete(K key);
    V getAndSet(K key, V update);
}
