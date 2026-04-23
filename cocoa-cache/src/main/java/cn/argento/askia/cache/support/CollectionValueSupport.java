package cn.argento.askia.cache.support;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 集合类型值支持.
 * <p>此接口支持您创建一些集合类型的缓存值, 比如：Map&lt;String, List&lt;V&gt;&gt;中的List&lt;V&gt;&gt;</p>
 * <p>此集合对标的是{@link Collection}, 部分方法复制此接口类</p>
 * @param <K> Key类型
 * @param <V> 集合组件类型
 * @param <C> 集合类型
 */
public interface CollectionValueSupport<K, V, C extends Collection<V>> {
    // 集合结构的增删改查
    // 添加
    boolean addValue(K key, V collectionValue);
    boolean addValue(K key, int index, V collectionValue);
    // 删除
    V deleteValue(K key, int index);
    boolean deleteValue(K key, V element);
    void clearList(K key);
    // 获取
    V get(K key, int index);
    V getOrDefault(K key, int index, V defaultValue);

    // 修改
    V set(K key, int index, V element);

    // 批量操作
    int addAll(K key, Collection<? extends V> coll);
    int deleteAll(K key, Collection<? extends V> coll);
    C deleteAll(K key, int... index);
    int retainAll(K key, Collection<? extends V> coll);

    int addIf(K key, Collection<? extends V> collection, Predicate<V> predicate);
    int deleteIf(K key, Collection<? extends V> coll, Predicate<V> predicate);

    // 集合状态查询
    boolean contains(K key, V element);
    boolean containsAll(K key, C coll);
    long sizeOf(K key);

    // 复合操作
    // 若不存在则创建列表，然后追加元素
    boolean addIfAbsent(K key, V element);
    boolean addIfAbsent(K key, V element, Supplier<C> cSupplier);
    int mergedWith(K key, C other, Function<Set<V>, C> mergeListFun);
}
