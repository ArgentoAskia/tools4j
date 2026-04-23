package cn.argento.askia.cache.support;

import java.util.function.BinaryOperator;

/**
 * 缓存原子操作支持.
 *
 * <p>提供CompareAndSet、getAndSet、getAndDelete、</p>
 *
 * <p>需要提供原子操作支持的缓存可以实现此接口</p>
 */
public interface AtomicSupport<K, V> {
    boolean compareAndSet(K key, V expired, V update);
    V getAndDelete(K key);
    V getAndSet(K key, V update);
}
