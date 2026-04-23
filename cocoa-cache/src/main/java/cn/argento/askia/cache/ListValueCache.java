package cn.argento.askia.cache;

import cn.argento.askia.cache.support.CollectionValueSupport;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface ListValueCache<K, V> extends Cache<K, List<V>>, CollectionValueSupport<K, V, List<V>> {

    // 大小
    @Override
    default long sizeOf(K key){
        final List<V> vs = get(key);
        return vs != null? vs.size():-1;
    }

    // 增删改查
    @Override
    default boolean addValue(K key, V collectionValue){
        if (containsKey(key)){
            final List<V> vs = get(key);
            return vs.add(collectionValue);
        }
        else{
            return false;
        }
    }
    // 删除
    @Override
    default V deleteValue(K key, int index){
        if (containsKey(key)){
            // 删除元素
            final List<V> list = get(key);
            return list.remove(index);
        }
        else{
            return null;
        }
    }

    @Override
    default boolean addValue(K key, int index, V collectionValue){
        if (containsKey(key)){
            final List<V> list = get(key);
            list.add(index, collectionValue);
            return true;
        }
        return false;
    }

    @Override
    default boolean deleteValue(K key, V element){
        if (containsKey(key)){
            final List<V> vs = get(key);
            return vs.remove(element);
        }
        return false;
    }

    @Override
    default void clearList(K key){
        if (containsKey(key)){
            final List<V> vs = get(key);
            vs.clear();
        }
    }

    @Override
    default V get(K key, int index){
        if (containsKey(key)){
            final List<V> vs = get(key);
            return vs.get(index);
        }
        return null;
    }

    @Override
    default V getOrDefault(K key, int index, V defaultValue){
        if (containsKey(key)){
            final List<V> vs = get(key);
            if (index >= vs.size() || index < 0){
                return defaultValue;
            }
            else{
                return vs.get(index);
            }
        }
        return null;
    }

    @Override
    default V set(K key, int index, V element){
        if (containsKey(key)){
            final List<V> vs = get(key);
            return vs.set(index, element);
        }
        return null;
    }

    @Override
    default int addAll(K key, Collection<? extends V> coll){
        if (containsKey(key)){
            final List<V> vs = get(key);
            return vs.addAll(coll)? 1 : 0;
        }
        return -1;
    }

    @Override
    default int deleteAll(K key, Collection<? extends V> coll){
        if (containsKey(key)){
            final List<V> vs = get(key);
            return vs.removeAll(coll)? 1 : 0;
        }
        return -1;
    }

    @Override
    default List<V> deleteAll(K key, int... index){
        if (containsKey(key)){
            final List<V> vs = get(key);
            // 初始容量设置在1.5倍
            List<V> list = new ArrayList<>(index.length + index.length >> 1) ;
            for (int i : index){
                final V remove = vs.remove(i);
                list.add(remove);
            }
            return list;
        }
        return null;
    }

    @Override
    default int retainAll(K key, Collection<? extends V> coll){
        if (containsKey(key)){
            final List<V> list = get(key);
            return list.retainAll(coll)? 1 : 0;
        }
        return -1;
    }

    @Override
    default boolean contains(K key, V element){
        if (containsKey(key)){
            final List<V> list = get(key);
            return list.contains(element);
        }
        else{
            return false;
        }
    }

    @Override
    default boolean addIfAbsent(K key, V element, Supplier<List<V>> cSupplier){
        List<V> list = null;
        if (!containsKey(key)){
            list = cSupplier.get();
            put(key, list);
        }
        else{
            list = get(key);
            // 当然也有可能是null value
            if (list == null){
                list = cSupplier.get();
                put(key, list);
            }
        }
        return list.add(element);
    }
    @Override
    default boolean addIfAbsent(K key, V element){
        return addIfAbsent(key, element, ArrayList::new);
    }

    @Override
    default int mergedWith(K key, List<V> other, Function<Set<V>, List<V>> mergeListFun){
        if (containsKey(key)){
            final List<V> list = get(key);
            HashSet<V> mergeSet = new HashSet<>(list);
            boolean result = mergeSet.addAll(other);
            final List<V> apply = mergeListFun.apply(mergeSet);
            put(key, apply);
            return result? 1 : 0;
        }
        return -1;
    }

    @Override
    default boolean containsAll(K key, List<V> coll){
        if (containsKey(key)){
            final List<V> list = get(key);
            return new HashSet<>(list).containsAll(coll);
        }
        return false;
    }

    @Override
    default int addIf(K key, Collection<? extends V> collection, Predicate<V> predicate){
        if (containsKey(key)){
            final List<V> list = get(key);
            int count = 0;
            for (V v : collection){
                if (predicate.test(v)) {
                    list.add(v);
                    count++;
                }
            }
            return count;
        }
        return -1;
    }
    @Override
    default int deleteIf(K key, Collection<? extends V> coll, Predicate<V> predicate){
        if (containsKey(key)){
            final List<V> list = get(key);
            int count = 0;
            for (V v : coll){
                if (predicate.test(v)){
                    list.remove(v);
                    count++;
                } ;
            }
            return count;
        }
        return -1;
    }
}
