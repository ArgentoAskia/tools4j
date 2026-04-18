package cn.argento.askia.cache;

import cn.argento.askia.cache.support.CollectionValueSupport;

import java.util.List;

public interface ListValueCache<K, V> extends Cache<K, List<V>>, CollectionValueSupport<K, V, List<V>> {

    @Override
    default int valueSize(K key){
        final List<V> vs = get(key);
        return vs != null? vs.size():-1;
    }

    @Override
    default boolean valueAdd(K key, V collectionValue){
        if (containsKey(key)){
            final List<V> vs = get(key);
            vs.add(collectionValue);
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    default V valueDelete(K key, int index){
        return null;
    }
}
