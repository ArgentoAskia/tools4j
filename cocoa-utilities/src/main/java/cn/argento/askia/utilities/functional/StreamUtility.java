package cn.argento.askia.utilities.functional;

import cn.argento.askia.annotations.Utility;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 流式工具类，包含Map Reduce等步骤
 */
@Utility("Stream流工具类")
public class StreamUtility {



    private StreamUtility(){
        throw new IllegalAccessError("StreamUtility为工具类, 无法创建该类的对象");
    }

    // mapper-reducer Utilities
    public static <K, V, O, C extends Collection<V>> Map<K, V> simpleMapReduce(List<O> originalData,
                                                                        Function<O, K> keyExchanger,
                                                                        Function<K, C> mapperSupplier,
                                                                        BiFunction<O, C, C> mapper,
                                                                        BiFunction<C, Map<K, C>, V> reducer){
        // map阶段
        Map<K, C> mapResult = new HashMap<>();
        for (O data : originalData){
            final K key = keyExchanger.apply(data);
            final C collectionMap = mapResult.computeIfAbsent(key, mapperSupplier);
            final C apply = mapper.apply(data, collectionMap);
            mapResult.put(key, apply);
        }
        // reduce阶段
        Map<K, V> reduceResult = new HashMap<>();
        for (Map.Entry<K, C> map : mapResult.entrySet()){
            final V reduce = reducer.apply(map.getValue(), mapResult);
            reduceResult.put(map.getKey(), reduce);
        }
        return reduceResult;
    }

    public static <K, V, O extends Collection<?>> Map<K, V> simpleReduce(Map<K, O> mapResult, BiFunction<O, Map<K, O>, V> reducer){
        // reduce阶段
        Map<K, V> reduceResult = new HashMap<>();
        for (Map.Entry<K, O> map : mapResult.entrySet()){
            final V reduce = reducer.apply(map.getValue(), mapResult);
            reduceResult.put(map.getKey(), reduce);
        }
        return reduceResult;
    }
}
