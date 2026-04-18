package cn.argento.askia.cache.support;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * 自动加载支持
 */
public interface LoadingSupport<K, V> {
    V get(K key, Function<? super K, ? extends V> loader);
    Map<K, V> getAll(Collection<? extends K> keys, Function<Set<? extends K>, Map<K, V>> bulkLoader);
}
