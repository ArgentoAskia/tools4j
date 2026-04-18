package cn.argento.askia.cache.support;

import java.util.Collection;

// 集合类型的值支持
public interface CollectionValueSupport<K, V, C extends Collection<V>> {
    // 集合结构的增删改查
    int valueSize(K key);
    boolean valueAdd(K key, V collectionValue);
    V valueDelete(K key, int index);

}
