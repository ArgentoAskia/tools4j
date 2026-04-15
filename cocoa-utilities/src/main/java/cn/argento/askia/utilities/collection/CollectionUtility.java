package cn.argento.askia.utilities.collection;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Java Collection Framework 等集合框架的通用工具类
 */
public class CollectionUtility {

    /**
     * 增强类型的{@link Map#toString()}.
     *
     * <p>原版本的{@link Map#toString()}在遇到数组的时候只会打印出数组的引用, 此版本会对{@link Map}的{@code value}进行判断, 如果发现他是数组的话则会直接使用{@link Arrays#deepToString(Object[])}进行格式化后再拼接输出</p>
     *
     * <p>调用者可以自行指定使用的分割器, 分割前缀和分割后缀, 方法底层使用{@link StringJoiner}进行字符串拼接来完成此方法的功能</p>
     *
     * @param map map结构
     * @param delimiter 分割器文本
     * @param begin 分割前缀
     * @param end 分割后缀
     * @return Map结构内部的Key, value内容组成的字符串
     * @see CollectionUtility#toString(Map)
     */
    public static String toString(Map<?, ?> map, String delimiter, String begin, String end){
        final String notNullDelimiter = Objects.requireNonNull(delimiter);
        final String notNullBegin = Objects.requireNonNull(begin);
        final String notNullEnd = Objects.requireNonNull(end);
        StringJoiner stringJoiner = new StringJoiner(notNullDelimiter, notNullBegin, notNullEnd);
        final Set<? extends Map.Entry<?, ?>> entries = map.entrySet();
        for (Map.Entry<?, ?> entry : entries){
            final Object key = entry.getKey();
            final Object value = entry.getValue();
            String keyValue = key.toString() + "=";
            // 因为所有的引用类型都可以调用toString()维度数组不行，所以我们单独处理数组即可！
            if (value.getClass().isArray()){
                final Object[] all = ArrayUtility.getAll(value);
                final String s = Arrays.deepToString(all);
                keyValue += s;
            }
            else{
                // 集合，map，list等是toString()方法仍然会打印出元素内容，因此可以不管
                keyValue += value.toString();
            }
            stringJoiner.add(keyValue);
        }
        return stringJoiner.toString();
    }
    /**
     * 增强类型的{@link Map#toString()}.
     * <p>原版本的{@link Map#toString()}在遇到数组的时候只会打印出数组的引用, 此版本会对{@link Map}的{@code value}进行判断, 如果发现他是数组的话则会直接使用{@link Arrays#toString(Object[])}进行格式化后再拼接输出</p>
     *
     * <p>此方法默认使用<code>&#123; &#125;</code>和{@code ,}来进行分割, 此方法是{@link CollectionUtility#toString(Map, String, String, String)}的默认版本</p>
     *
     * @param map 任何类型的map
     * @return Map结构内部的Key, value内容组成的字符串
     */
    public static String toString(Map<?, ?> map){
        return toString(map, ", ", "{ ", " }");
    }

    /**
     * 增强类型的{@link Collection#toString()}.
     *
     * <p>原版本的{@link Collection#toString()}在遇到数组的时候只会打印出数组的引用, 此版本会对值进行判断, 如果发现他是数组的话则会直接使用{@link Arrays#deepToString(Object[])}进行格式化后再拼接输出</p>
     *
     * <p>调用者可以自行指定使用的分割器, 分割前缀和分割后缀, 方法底层使用{@link StringJoiner}进行字符串拼接来完成此方法的功能</p>
     *
     * @param collectionTypeObj 任何集合类型, 比如{@link List}、{@link Set}、{@link Queue}以及他们的子接口和子实现
     * @param delimiter 分割器
     * @param begin 分割前缀
     * @param end 分割后缀
     * @param <C> 任何实现了{@link Collection}的接口或者实现类
     * @return {@code Collection}内部元素toString结果
     * @since 2026.4.14
     * @see CollectionUtility#toString(Map)
     * @see CollectionUtility#toString(Map, String, String, String)
     * @see CollectionUtility#toString(Collection)
     */
    public static <C extends Collection<?>> String toString(C collectionTypeObj, String delimiter, String begin, String end){
        final String notNullBegin = Objects.requireNonNull(begin);
        final String notNullEnd = Objects.requireNonNull(end);
        final String notNullDelimiter = Objects.requireNonNull(delimiter);
        StringJoiner joiner = new StringJoiner(notNullDelimiter, notNullBegin, notNullEnd);
        for (Object element : collectionTypeObj){
            final Class<?> elementClass = element.getClass();
            // 如果是数组, 则我们尝试使用数组进行输出
            if (elementClass.isArray()){
                final Object[] all = ArrayUtility.getAll(element);
                final String s = Arrays.deepToString(all);
                joiner.add(s);
            }
            else{
                joiner.add(element.toString());
            }
        }
        return joiner.toString();
    }

    /**
     * 增强类型的{@link Collection#toString()}.
     *
     * <p>原版本的{@link Collection#toString()}在遇到数组的时候只会打印出数组的引用, 此版本会对值进行判断, 如果发现他是数组的话则会直接使用{@link Arrays#deepToString(Object[])}进行格式化后再拼接输出</p>
     *
     * <p>此方法在面对{@link Set}集合时, 使用<code>( )</code>来进行输出, 而其他如{@link List}、{@link Queue}等, 则使用<code>{ }</code>进行输出</p>
     *
     * <p>此方法为{@link CollectionUtility#toString(Collection, String, String, String)}的默认版本</p>
     *
     * @param collectionTypeObj 任何集合类型, 比如{@link List}、{@link Set}、{@link Queue}以及他们的子接口和子实现
     * @param <C> 任何实现了{@link Collection}的接口或者实现类
     * @return {@code Collection}内部元素toString结果
     */
    public static <C extends Collection<?>> String toString(C collectionTypeObj){
        String prefix = "[";
        String suffix = "]";
        if (collectionTypeObj instanceof Set<?>){
            prefix = "(";
            suffix = ")";
        }
        return toString(collectionTypeObj, ", ", prefix, suffix);
    }

    public static <C extends Collection<?>> String toBeautifulString(C collectionTypeObj, boolean withSize, boolean withType){
        final int size = collectionTypeObj.size();
        final String collectionTypeClassName = collectionTypeObj.getClass().getName();
        String prefix = collectionTypeClassName + " = [" + System.lineSeparator() + "\t\t\t";
        String delimiter = "," + System.lineSeparator() + "\t\t\t";
        if (!withType){
            prefix = "[" + System.lineSeparator() + "\t\t\t";
        }
        String suffer = System.lineSeparator() + "], size = " + size;
        if (!withSize){
            suffer = System.lineSeparator() + "]";
        }
        final String notNullBegin = Objects.requireNonNull(prefix);
        final String notNullEnd = Objects.requireNonNull(suffer);
        final String notNullDelimiter = Objects.requireNonNull(delimiter);
        StringJoiner joiner = new StringJoiner(notNullDelimiter, notNullBegin, notNullEnd);
        for (Object element : collectionTypeObj){
            final Class<?> elementClass = element.getClass();
            // 如果是数组, 则我们尝试使用数组进行输出
            if (elementClass.isArray()){
                final String s = ArrayUtility.toBeautifulString(element);
                joiner.add(s);
            }
            else{
                joiner.add(element.toString());
            }
        }
        return joiner.toString();
    }
    public static <C extends Collection<?>> String toBeautifulString(C collectionTypeObj){
        return toBeautifulString(collectionTypeObj, false, false);
    }


    // 实现将List进行转换

    /**
     * 实现将一种类型的List转为另外一种类型的List
     *
     * @param list 列表
     * @param function 转换函数
     * @param <V1> 类型1
     * @param <V2> 类型2
     * @return 另外一种类型的List
     * @since 2026.1.19
     */
    public static <V1, V2> List<V2> exchangeList(List<V1> list, Function<V1, V2> function){
        if (list == null){
            return null;
        }
        if (list.size() == 0){
            return new ArrayList<>();
        }
        return list.parallelStream()
                .map(function)
                .collect(Collectors.toList());
    }

    //  实现将Map的类型转换

    /**
     * 将一种类型的Map转为另外一种类型的Map，比如将Map&lt;Integer, String&gt;转为Map&lt;String, Object&gt;.
     *
     * @param map 原始Map
     * @param keyFunction key转换函数
     * @param valueFunction value转换函数
     * @param <K1> key type 1
     * @param <V1> key type 2
     * @param <K2> value type 1
     * @param <V2> value type 2
     * @return 转换后的Map(新Map, 不影响原始Map), 如果原始Map为null，则返回null，否则返回转换后的Map
     * @see CollectionUtility#exchangeMapKey(Map, Function)
     * @see CollectionUtility#reverseMap(Map, boolean)
     * @since 1.0
     */
    public static <K1, V1, K2, V2> Map<K2, V2>exchangeMap(Map<K1, V1> map, Function<K1, K2> keyFunction,
                                                          Function<V1, V2> valueFunction){
        // null checked
        if (map == null){
            return null;
        }
        Map<K2, V2> retMap = new HashMap<>();
        final Set<Map.Entry<K1, V1>> entries = map.entrySet();
        for (Map.Entry<K1, V1> entry : entries){
            final K2 k2Key = keyFunction.apply(entry.getKey());
            final V2 k2Value = valueFunction.apply(entry.getValue());
            retMap.put(k2Key, k2Value);
        }
        return retMap;
    }

    // 实现将map的key类型进行转换

    /**
     * 将Map的Key进行转换另外一种类型的Key, 比如将Map&lt;Integer, String&gt;转为Map&lt;String, String&gt;.
     *
     * @param map 原始Map
     * @param keyExchangeFunction key转换函数
     * @param <K1> key type 1
     * @param <K2> key type 2
     * @param <V> value类型
     * @return 转换后的Map(新Map, 不影响原始Map), 如果原始Map为null，则返回null，否则返回转换后的Map
     * @see CollectionUtility#exchangeMap(Map, Function, Function)
     * @see CollectionUtility#reverseMap(Map, boolean)
     * @since 1.0
     */
    public static <K1, K2, V> Map<K2, V> exchangeMapKey(Map<K1, V> map, Function<K1, K2> keyExchangeFunction){
        if (map == null){
            return null;
        }
        Map<K2, V> retMap = new HashMap<>();
        final Set<K1> k1s = map.keySet();
        for (K1 key1 : k1s){
            final V o = map.get(key1);
            retMap.put(keyExchangeFunction.apply(key1), o);
        }
        return retMap;
    }

    // 将map的key和value反转

    /**
     * 将Map的value和Key反转, 比如原始Map&lt;Integer, String&gt;转为Map&lt;String, Integer&gt;.
     *
     * <p>方法提供了一个参数override用来指定当遇到相同的value时直接覆盖还是保留最初的版本，比如在原始Map中, 存在两个键值对：
     * <ol>
     *     <li> 1 ==> "hello" </li>
     *     <li> 2 ==> "hello" </li>
     * </ol>
     * 则此时反转Map时就会出现冲突, 即：
     * <ol>
     *     <li> "hello" ==> 1 </li>
     *     <li> "hello" ==> 2 </li>
     * </ol>，override参数用来指定是否无限保留后者还是前者
     *
     * @param map 原始Map
     * @param override 指定当遇到相同的value的时候如何处理, 是覆盖还是保留原样.
     * @param <K> key
     * @param <V> value
     * @return 反转完成的Map, 新map, 不影响原始Map
     * @since 1.0
     * @see CollectionUtility#exchangeMap(Map, Function, Function)
     * @see CollectionUtility#exchangeMapKey(Map, Function)
     */
    public static <K, V> Map<V, K> reverseMap(Map<K, V> map, boolean override){
        if (map == null){
            return null;
        }
        else{
            Map<V, K> retMap = new HashMap<>();
            final Set<Map.Entry<K, V>> entries = map.entrySet();
            for (Map.Entry<K, V> entry : entries){
                final K key = entry.getKey();
                final V value = entry.getValue();
                if (override){
                    retMap.put(value, key);
                }
                else{
                    retMap.putIfAbsent(value, key);
                }
            }
            return retMap;
        }
    }

    /**
     * 获取两个List的交集(包含重复的选项).
     * <p>此方法能够获取两个List的交集，注意由于是{@linkplain List List结构}, 所以得到的交集可能有重复元素.
     * <p>此方法基于list的浅拷贝来进行比较的, 所以不会影响原始的数组
     * <p>例如：
     * <blockquote style="background-color:rgb(232,232,232)"><pre>
     *     List<String> l1 = List.of("Apple", "banana", "red", "pen", "red");
     *     List<String> l2 = List.of("Apple2", "banana2", "red", "pen", "red");
     *     List<String> result = intersection(l1, l2);
     *     // 结果：["red", "pen", "red"]
     * </pre></blockquote>
     * @param list List结构
     * @param list2 List结构
     * @param <T> 类型
     * @return 交集，如果没有交集则返回空List
     * @see CollectionUtility#intersectionOriginal(List, List)
     * @since 1.0
     */
    public static <T> List<T> intersection(List<T> list, List<T> list2){
        // List浅拷贝
        List<T> shallowCloneList1 = new ArrayList<>(list);
        List<T> shallowCloneList2 = new ArrayList<>(list2);
        return intersectionOriginal(shallowCloneList1, shallowCloneList2);
    }

    /**
     * 获取两个List的交集(包含重复的选项).
     * <p>此方法能够获取两个List的交集，注意由于是{@linkplain List List结构}, 所以得到的交集可能有重复元素.
     * <p>此方法基于原始数组进行比较, 因此返回的结果会更改list参数
     * <p>例如：
     * <blockquote style="background-color:rgb(232,232,232)"><pre>
     *     List<String> l1 = List.of("Apple", "banana", "red", "pen", "red");
     *     List<String> l2 = List.of("Apple2", "banana2", "red", "pen", "red");
     *     List<String> result = intersectionOriginal(l1, l2);
     *     // 结果：["red", "pen", "red"]
     * </pre></blockquote>
     *
     * @param list List结构
     * @param list2 List结构
     * @param <T> 类型
     * @return 交集，如果没有交集则返回空List
     * @see CollectionUtility#intersection(List, List)
     * @since 1.0
     */
    public static <T> List<T> intersectionOriginal(List<T> list, List<T> list2){
        list.retainAll(list2);
        return list;
    }

    /**
     * 判断两个集合是否是包含关系.
     * <p>比如
     * @param list List结构
     * @param list2
     * @param <T>
     * @return
     */
    public static <T> boolean isInclusionOriginal(List<T> list, List<T> list2){
        if (list.size() < list2.size()){
            return !list.retainAll(list2);
        }
        else{
            return !list2.retainAll(list);
        }
    }
    public static <T> boolean isInclusion(List<T> list, List<T> list2){
        // List浅拷贝
        List<T> shallowCloneList1 = new ArrayList<>(list);
        List<T> shallowCloneList2 = new ArrayList<>(list2);
        return isInclusionOriginal(shallowCloneList1, shallowCloneList2);
    }

    // 判断两个集合是否拥有交集
    public static <T> boolean hasIntersection(List<T> list, List<T> list2){
        final List<T> intersection = intersection(list, list2);
        return intersection.size() > 0;
    }

    /**
     * {@link List}结构的{@link List#equals(Object)}方法增强实现。
     *
     * <p>在原版本中，{@link List#equals(Object)}方法由其抽象实现{@link AbstractList}来进行实现。抽象实现的比较方法是通过获取
     * 两个{@link ListIterator}的形式来实现，也就是说这不仅仅需要比较元素的{@linkplain Object#equals(Object) equals()}, 还要比较
     * 顺序是否相同！如果你有两个{@code List}虽然他们的顺序不一致但是内部的元素实际上是相同的时候，也会返回{@code false}.
     *
     * <p>此方法消除了这种副作用。方法先会判断两个List元素个数是否相同, 然后再调用equals()判断, 如果还是false, 则会尝试无视顺序来进行判断。
     *
     * @param list list1
     * @param list2 list2
     * @param <T> 元素类型
     * @return 如果两个List相等(内容程度), 则返回true, 否则返回false
     */
    public static <T> boolean equals(List<T> list, List<T> list2){
        // 数量不相同所有false
        if (list.size() != list2.size()){
            return false;
        }
        else if (list.equals(list2)){
            // 如果原版equal()相同则肯定相同
            return true;
        }
        else{
            // 经过一次并集之后如果没有改变则一定相同
            Set<T> compareSet = new HashSet<>(list);
            return !compareSet.addAll(list2);
        }
    }

    /**
     * 合并多个List为一个
     *
     * @param listCollection
     * @param <T>
     * @return
     */
    public static <T> List<T> combine(Collection<List<T>> listCollection){
        return listCollection.parallelStream()      // Stream<List<T>>
                .flatMap(List::stream)  // 把每个 List<T> 展平成 Stream<T>
                .collect(Collectors.toList());
    }
    @SafeVarargs
    public static <T> List<T> combine(List<T>... lists){
        if (lists == null){
            return null;
        }
        else if (lists.length == 0){
            return new ArrayList<>();
        }
        else{
            return combine(Arrays.asList(lists));
        }
    }


    @Deprecated
    protected static <T extends Cloneable> List<T> deepCopy(List<T> list, Function<? super T, T> function){
        if (list == null){
            return null;
        }
        ArrayList<T> deepCopyList = new ArrayList<>();
        for (T t : list) {
            T apply = function.apply(t);
            deepCopyList.add(apply);
        }
        return deepCopyList;
    }

    /**
     * 将一个List转为Map
     *
     * @param list list列表
     * @param keyFunction 转为哪种key
     * @param valueFunction
     * @param <K>
     * @param <V>
     * @param <O>
     * @return
     */
    public static <K, V, O> Map<K, V> listToMap(List<O> list,
                                                Function<O, K> keyFunction,
                                                Function<O, V> valueFunction){
        if (list == null || list.size() == 0){
            return new HashMap<>();
        }
        else{
           return list.parallelStream().collect(Collectors.toMap(keyFunction, valueFunction));
        }
    }

    public static <K, V, O, C extends Collection<V>> Map<K, C> listToMultiValueMap(List<O> list,
                                                                                   Function<O, K> keyFunction,
                                                                                   Function<? super K, C> multiSup,
                                                                                   BiFunction<O, C, C> multiValueFunction){
        Map<K, C> map = new HashMap<>();
        for (O object : list){
            final K key = keyFunction.apply(object);
            final C vs = map.computeIfAbsent(key, multiSup);
            final C apply = multiValueFunction.apply(object, vs);
            map.put(key, apply);
        }
        return map;
    }

    public static <K, V, O, K3,  C extends Map<K3, V>> Map<K, C> listToRedisHashMap(List<O> list,
                                                                                    Function<O, K> keyFunction,
                                                                                    Function<? super K, C> multiSup,
                                                                                    BiFunction<O, C, C> multiValueFunction){
        Map<K, C> map = new HashMap<>();
        for (O object : list){
            final K key = keyFunction.apply(object);
            final C vs = map.computeIfAbsent(key, multiSup);
            final C apply = multiValueFunction.apply(object, vs);
            map.put(key, apply);
        }
        return map;
    }


    public static <K, V, T extends Collection<V>, O> Map<K, T> listToMap(List<O> list,
                                                                         Function<O, K> keyFunction,
                                                                         Function<O, V> valueFunction,
                                                                         Supplier<T> listFunction,
                                                                         BiFunction<V, T, T> combineFunction){
        Map<K, T> map = new HashMap<>();
        for (O object : list){
            // key
            final K key = keyFunction.apply(object);
            final V value = valueFunction.apply(object);
            final T orDefault = map.getOrDefault(key, listFunction.get());
            final T apply = combineFunction.apply(value, orDefault);
            map.put(key, apply);
        }
        return map;
    }


    /**
     * 获取集合 {@code a} 和集合 {@code b} 的对称差集, 即集合 {@code a} 和集合 {@code b} 中各自不包含的部分.
     *
     * <p><b>方法最后两个参数为参考类型参数, 可以提供null, 不会触发 {@linkplain NullPointerException NPE}, 此时将不会返回对称差集, 当仅需要统计对称差集个数时使用</b>
     * <p>方法返回对称差集的成员数
     *
     * <p>方法使用:
     * <blockquote style="background-color:rgb(232,232,232)"><pre>
     *
     *     Set&lt;Integer&gt; a = Set.of(1, 2, 3);
     *     Set&lt;Integer&gt; b = Set.of(2, 3, 4);
     *     Set&lt;Integer&gt; diffFromA = new HashSet();
     *     Set&lt;Integer&gt; diffFromB = new HashSet();
     *     // diffFromA = [1], diffFromB = [4]
     *     int diffCounts = CollectionUtility
     *           .symmetricDifference(a, b, diffFromA, diffFromB);  // 2
     *
     * </pre></blockquote>
     *
     *
     * @param a 集合A
     * @param b 集合B
     * @param diffFromA 集合A的差集, 参考型参数, 可以提供现有的集合, 或者新集合, 或者null
     * @param diffFromB 集合B的差集, 参考型参数, 可以提供现有的集合, 或者新集合, 或者null
     * @param <T> 类型参数
     * @return 对称差集的成员数
     */
    public static <T> int symmetricDifference(Set<T> a, Set<T> b,
                                                 Set<T> diffFromA,
                                                 Set<T> diffFromB){
        Objects.requireNonNull(a, "集合A不能为空");
        Objects.requireNonNull(b, "集合B不能为空");
        Objects.requireNonNull(b, "集合B不能为空");
        Set<T> shadowA = new HashSet<>(a);
        shadowA.removeAll(b);
        Set<T> shadowB = new HashSet<>(b);
        shadowB.removeAll(a);
        int differenceCounts = shadowA.size() + shadowB.size();
        if (diffFromA != null){
            diffFromA.addAll(shadowA);
        }
        if (diffFromB != null){
            diffFromB.addAll(shadowB);
        }
        return differenceCounts;
    }

    /**
     * 获取集合 {@code a} 和集合 {@code b} 的对称差集, 即集合 {@code a} 和集合 {@code b} 中各自不包含的部分.
     *
     * <p>使用说明：
     * <blockquote style="background-color:rgb(232,232,232)"><pre>
     *
     *     Set&lt;Integer&gt; a = Set.of(1, 2, 3);
     *     Set&lt;Integer&gt; b = Set.of(2, 3, 4);
     *     Set diff = CollectionUtility
     *               .symmetricDifference(a, b);  // [1, 4]
     *
     * </pre></blockquote>
     * <hr>该方法实际上调用了{@link CollectionUtility#symmetricDifference(Set, Set, Set, Set)}, 等价于下面的代码：
     * <blockquote style="background-color:rgb(232,232,232)"><pre>
     *
     *     Set&lt;Integer&gt; a = Set.of(1, 2, 3);
     *     Set&lt;Integer&gt; b = Set.of(2, 3, 4);
     *
     *     // 各自不包含的部分（对称差集）
     *     Set&lt;Integer&gt; diff = new HashSet<>(a);
     *     diff.removeAll(b);          // a - b
     *     Set&lt;Integer&gt; diffB = new HashSet<>(b);
     *     diffB.removeAll(a);         // b - a
     *     diff.addAll(diffB);         // (a-b) ∪ (b-a)
     *
     *     System.out.println(diff);   // [1, 4]
     *
     * </pre></blockquote>
     *
     * @param a 集合A
     * @param b 集合B
     * @param <T> 类型参数
     * @return 对称差集总和
     * @see CollectionUtility#symmetricDifference(Set, Set, Set, Set)
     * @since 2025.10.10
     */
    public static <T> Set<T> symmetricDifference(Set<T> a, Set<T> b){
        Set<T> diff = new HashSet<>();
        symmetricDifference(a, b, diff, diff);
        return diff;
    }

    public static List<Map<String, Object>> mapToList(Map<?, ?> map,
                                                      String keyName,
                                                      String valueName){
        List<Map<String, Object>> list = new ArrayList<>();
        Set<?> keys = map.keySet();
        for (Object key : keys){
            Object o = map.get(key);
            Map<String, Object> secondMap = new HashMap<>();
            secondMap.put(keyName, key);
            secondMap.put(valueName, o);
            list.add(secondMap);
        }
        return list;
    }

    /**
     * 通用添加元素方法, 支持多重添加, 比如：add(list, "1", "2", "3");
     * @param collection 集合, 比如List, Set等
     * @param elements 确定的元素
     * @param <E> 元素类型
     * @param <T> 集合类型
     * @return 集合
     * @since 2025.12.29
     */
    @SafeVarargs
    public static <E, T extends Collection<E>> T add(T collection, E... elements){
        if (elements != null && elements.length != 0 && collection != null){
            collection.addAll(Arrays.asList(elements));
        }
        return collection;
    }

    public static void main(String[] args) {
        final Integer[][] integers = ArrayUtility.newMatrix(10, 10);
        final CharSequence[][] integers1 = ArrayUtility.newArrayInstance(String[].class, 20, new String[]{"123", "456", "789"}, new String[]{"222", "333", "444"});

        System.out.println(Arrays.deepToString(integers1));
        System.out.println(Arrays.deepToString(new java.lang.String[]{"123", "456", "789"}));
        List<Integer> list = new ArrayList<>();
        list.add(3);
        list.add(2);
        list.add(4);
        list.add(5);
        List<Integer> list2 = new ArrayList<>();
        list2.add(2);
        list2.add(3);
        list2.add(4);
        list2.add(5);
        System.out.println(toBeautifulString(list, false, false));
    }
}
