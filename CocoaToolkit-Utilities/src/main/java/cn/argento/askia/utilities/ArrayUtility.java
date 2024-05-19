package cn.argento.askia.utilities;


import java.lang.reflect.Array;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Askia's ArrayUtils.
 * 用于数组的工具类,主要包括对
 * {@link Array}、{@link Arrays}以及 {@code LeetCode} 数组一列相关算法的封装
 * 该工具类包括下面的操作:
 * <ol>
 *     <li>数组动态创建</li>
 *     <li>任意数组成员的随机访问和更换</li>
 *     <li>获取任意一个数组的长度</li>
 *     <li>数组扩容</li>
 *     <li>数组复制</li>
 *     <li>打印输出任意维度的数组</li>
 *     <li>数组转换成List、Set、Map</li>
 *     <li>统计一个数组的维度</li>
 * </ol>
 * <p>
 *
 * @author Askia
 * @version 1.2
 * @since 1.0
 */
@SuppressWarnings("SuspiciousSystemArraycopy")
public final class ArrayUtility {

    private final static String TARGET_OBJECT_NOT_ARRAY = "参数 arrayObj 不是一个数组";

    // 检查对象是不是一个数组
    private static void checkObjectIsArray(Object arrayObj){
        if (!isArray(arrayObj))
            throw new IllegalArgumentException(TARGET_OBJECT_NOT_ARRAY);
    }

    /**
     * 判断一个对象是否是数组.
     *
     * @param targetObj 任何对象
     * @return true代表该对象是一个数组,false代表不是
     * @since 1.0
     */
    public static boolean isArray(Object targetObj){
        return targetObj.getClass().isArray();
    }

    /**
     * 获取数组的维度.
     *
     * @param arrayObj 数组对象
     * @return 一个整数，代表数组的维度
     * @throws IllegalArgumentException 如果传递的参数不是一个数组时则抛出这个异常
     * @since 1.0.1
     */
    @SuppressWarnings("StatementWithEmptyBody")
    public static int getDimension(Object arrayObj){
        checkObjectIsArray(arrayObj);
        String name = arrayObj.getClass().getName();
        int i = 0;
        while (name.charAt(i++) == '[');
        return --i;
    }

    /**
     * 获取数组长度
     *
     * @param arrayObj 任何类型的数组
     * @return 数组的成员数
     * @throws IllegalArgumentException 如果参数不是一个数组则抛出此异常
     * @since 1.0.1
     */
    public static int getLength(Object arrayObj){
        checkObjectIsArray(arrayObj);
        return Array.getLength(arrayObj);
    }


    private static void checkPrimitiveAndBoxingType(){}


    /**
     * 获取数组成员.
     *
     * @param arrayObj 数组对象
     * @param index 下标
     * @return 数组的某个成员
     * @since 1.0.1
     */
    public static Object get(Object arrayObj, int index){
        checkObjectIsArray(arrayObj);
        return Array.get(arrayObj, index);
    }

    /**
     * 获取数组成员,并转为相应的类型, 该方法为了兼容继承关系而无须进行强制转换
     *
     * @param arrayObj 数组对象
     * @param index 下标
     * @param type 类型
     * @param <T> 要返回的成员的类型
     * @return 数组的某个成员
     * @see ArrayUtility#get(Object, int)
     * @since 1.0.5
     */
    public static <T> T get(Object arrayObj, int index, Class<T> type){
        Object o = get(arrayObj, index);
        return type.cast(o);
    }

    /**
     * 检查成员能否被放入到数组中，主要是一个类型匹配问题！
     * @param arrayObj
     * @param member
     */
    private static void checkObjectCanPutInArray(Object arrayObj, Object member){
        Class<?> aClass = member.getClass();
        Class<?> componentType = arrayObj.getClass().getComponentType();
        boolean assignableFrom = componentType.isAssignableFrom(aClass);
        if (!assignableFrom)
            throw new ClassCastException(
                    "不能将成员类型" + aClass.getSimpleName() + "放入到"
                            + componentType.getSimpleName() + "类型的数组中！");
    }

    /**
     * 设置数组成员.
     *
     * @param arrayObj 数组对象
     * @param index 下标
     * @return 上一个这个位置的成员，如果这个位置上一次没有成员，则返回 {@code null}
     * @since 1.0.1
     */
    public static Object set(Object arrayObj, int index, Object member){
        checkObjectIsArray(arrayObj);
        checkObjectCanPutInArray(arrayObj, member);
        Object o = get(arrayObj, index);
        Array.set(arrayObj, index, member);
        return o;
    }

    /**
     * 创建一个一维或多维数组.
     *
     * @param componentType 数组的原始类型
     * @param dimensions 数组的每个维度的成员长度
     * @return
     * @since 1.0
     */
    public static Object newArray(Class<?> componentType, int ...dimensions){
        return Array.newInstance(componentType, dimensions);
    }

    /**
     *
     * @param componentType
     * @param length
     * @param <T>
     * @return
     * @deprecated 这个方法创建出来的数组可能并不是开发者想要的形式！并且可能会抛出莫名其妙的异常！
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    protected static <T> T[] newArray2(Class<? extends T> componentType, int length){
        return (T[]) Array.newInstance(componentType, length);
    }

    /**
     * 创建一个一维数组并指定长度.
     *
     * @param componentType 数组的原始类型
     * @param length 数组的长度
     * @return
     */
    public static Object newArray(Class<?> componentType, int length){
        return Array.newInstance(componentType, length);
    }

    /**
     * 创建一个对象矩阵(二维数组).
     *
     * @param objectType
     * @param m
     * @param n
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T[][] newObjectMatrix(Class<T> objectType, int m, int n){
        return (T[][]) Array.newInstance(objectType, m, n);
    }

    /**
     * 创建一个矩阵.
     * @param m
     * @param n
     * @return
     */
    public static Integer[][] newMatrix(int m, int n){
        return newObjectMatrix(Integer.class, m, n);
    }


    /**
     * 数组扩容
     *
     * @param arrayObj
     * @param expandSize
     * @return
     * @since 1.0.5
     */
    public static Object expand(Object arrayObj, int expandSize) {
        checkObjectIsArray(arrayObj);
        return expandSelf(arrayObj, expandSize);
    }

    /**
     * 1.5倍扩容.
     * @param arrayObj
     * @return
     */
    public static Object expand(Object arrayObj){
        checkObjectIsArray(arrayObj);
        final int expandSize = getLength(arrayObj) >>> 1;
        return expandSelf(arrayObj, expandSize);
    }

    @SuppressWarnings("SuspiciousSystemArraycopy")
    private static Object expandSelf(Object arrayObj, int expandSize){
        int length = getLength(arrayObj);
        Object newArray = newArray(arrayObj.getClass(), expandSize + length);
        System.arraycopy(arrayObj, 0, newArray, 0, length);
        return newArray;
    }

    private static void checkIndexNotOutOfBounds(Object arrayObj, int index){
        if (index < -1){
            throw new IllegalArgumentException("下标不能是小于-1的负数！");
        }
        if (index >= getLength(arrayObj)){
            throw new IndexOutOfBoundsException("数组越界了,数组有" + getLength(arrayObj) + "个成员，但是传入的下标是：" + index);
        }
    }

    /**
     * 复制数组。
     * @param arrayObj 待复制的数组
     * @return 返回一个新的数组引用,其大小和内容与 {@code arrayObj} 一模一样
     * @throws IllegalArgumentException {@code arrayObj} 参数不是数组的时候抛出该异常
     */
    public static Object copy(Object arrayObj){
        checkObjectIsArray(arrayObj);
        Object newArray = newArray(arrayObj.getClass().getComponentType(), getLength(arrayObj));
        System.arraycopy(arrayObj, 0, newArray, 0, getLength(arrayObj));
        return newArray;
    }

    public static Object copyOfLength(Object arrayObj, int beginIndex, int length){
        return copyOfLength(arrayObj, beginIndex, length, 0 , 0);
    }

    public static Object copyOfLength(Object arrayObj, int beginIndex, int length, int newArrayLength){
        return copyOfLength(arrayObj, beginIndex, length, newArrayLength, 0);
    }

    public static Object copyOfLength(Object arrayObj, int beginIndex, int length, int newArrayLength, int newArrayCopyBeginIndex){
        checkObjectIsArray(arrayObj);
        if (beginIndex < 0)                                             beginIndex = 0;
        if (length <= 0)                                                length = getLength(arrayObj);
        if (newArrayLength <= 0)                                        newArrayLength = length;
        if (newArrayCopyBeginIndex <= -1)                               newArrayCopyBeginIndex = 0;
        if ((newArrayLength - newArrayCopyBeginIndex) < length)         newArrayLength = newArrayCopyBeginIndex + length;
        Object newArray = newArray(arrayObj.getClass().getComponentType(), newArrayLength);
        System.arraycopy(arrayObj, beginIndex, newArray, newArrayCopyBeginIndex, length);
        return newArray;
    }

    public static Object copyOfRange(Object arrayObj){
        return ArrayUtility.copyOfRange(arrayObj, 0);
    }
    public static Object copyOfRange(Object arrayObj, int beginIndex){
        return ArrayUtility.copyOfRange(arrayObj, beginIndex, END_OF_ARRAY);
    }
    public static final int END_OF_ARRAY = -1;
    public static Object copyOfRange(Object arrayObj, int beginIndex, int endIndex){
        return ArrayUtility.copyOfRange(arrayObj, beginIndex, endIndex, false);
    }

    public static Object copyOfRange(Object arrayObj, int beginIndex, int endIndex, boolean includeEndIndex){
        checkObjectIsArray(arrayObj);
        // 检查两个下标是否
        checkIndexNotOutOfBounds(arrayObj, beginIndex);
        checkIndexNotOutOfBounds(arrayObj, endIndex);
        if (endIndex == END_OF_ARRAY){
            endIndex = getLength(arrayObj);
            includeEndIndex = false;
        }
        return copyOfRange(arrayObj, beginIndex, endIndex, includeEndIndex, -1, 0);
    }

    /**
     *
     * @param arrayObj
     * @param beginIndex
     * @param endIndex
     * @param includeEndIndex
     * @param newArrayLength
     * @param newArrayCopyBeginIndex
     * @return
     */
    public static Object copyOfRange(Object arrayObj, int beginIndex, int endIndex, boolean includeEndIndex,
                                     int newArrayLength, int newArrayCopyBeginIndex){
        // 下标小于0,则让其等于0
        if (newArrayCopyBeginIndex < 0){
            newArrayCopyBeginIndex = 0;
        }
        if (newArrayLength < -1){
            newArrayLength = -1;
        }
        int copyLength = endIndex - beginIndex;
        if (includeEndIndex) copyLength++;
        //  检查新数组长度变量
        if (newArrayLength == -1 || newArrayLength == 0){
            newArrayLength = newArrayCopyBeginIndex + copyLength;
        }
        // 当newArrayLength非0,或非-1，且小于开始新数组复制位置
        if (newArrayLength <= newArrayCopyBeginIndex){
            newArrayLength = newArrayCopyBeginIndex + copyLength;
        }
        // 剩余空间不够数组复制！
        if ((newArrayLength - newArrayCopyBeginIndex) < copyLength){
            newArrayLength = newArrayLength + (copyLength - (newArrayLength - newArrayCopyBeginIndex));
        }
        Object newArray = newArray(arrayObj.getClass().getComponentType(), newArrayLength);
        System.arraycopy(arrayObj, beginIndex, newArray, newArrayCopyBeginIndex, copyLength);
        return newArray;
    }


    @Deprecated
    public static <T> T[] expand(T[] arrayObj, int expandSize){
        return (T[]) expandSelf(arrayObj, expandSize);
    }


    public static final String TO_STRING_TEMPLATE_PRIMITIVE_ARRAY = "[[ {{arrayMember}}, {{arrayMember}} ]]";
    public static final String TO_STRING_TEMPLATE_REFERENCE_ARRAY =
            "[[\n" +
            "    {{arrayMember}},\n" +
            "    {{arrayMember}}\n" +
            "]]";
    public static final String TO_STRING_TEMPLATE_TWO_DIMENSIONAL_ARRAY = TO_STRING_TEMPLATE_REFERENCE_ARRAY;
    public static final String TO_STRING_TEMPLATE_ARRAY_TYPE = "{{arrayType}}";
    public static final String TO_STRING_TEMPLATE_ARRAY_LENGTH = "{{arrayLength}}";
    public static final int DEFAULT_ADD_INDENTATION = 4;

    /**
     * 打印一个数组
     * @param arrayObj
     * @return
     */
    public static String toString(Object arrayObj){
        String template = "ArrayType：" + TO_STRING_TEMPLATE_ARRAY_TYPE + "\n"
                + "ArrayLength：" + TO_STRING_TEMPLATE_ARRAY_LENGTH + "\n"
                + TO_STRING_TEMPLATE_REFERENCE_ARRAY;
        return toString(arrayObj, template);
    }
    public static String toString(Object arrayObj, String template){
        checkObjectIsArray(arrayObj);
        if (template == null){
            throw new NullPointerException("模板不能为null!");
        }
        return toString(arrayObj, template, 0);
    }

    /**
     * 定义模板：
     *      arrayType：代表数组的类型
     *      arrayLength：代表数组长度变量
     *      arrayMember：代表成员展位字符
     *      [[]]：代表数组域
     *      {{}}：代表引用变量
     *
     * @param arrayObj 数组对象
     * @param template 模板
     * @return
     */
    private static String toString(Object arrayObj, String template, int indentation){
        template = template.toLowerCase();
        if (template.contains("{{arraytype}}")){
            String simpleName = arrayObj.getClass().getComponentType().getSimpleName();
            template = template.replace("{{arraytype}}", simpleName);
        }
        if (template.contains("{{arraylength}}")){
            int length = getLength(arrayObj);
            template = template.replace("{{arraylength}}", String.valueOf(length));
        }
        template = parseTemplate(getLength(arrayObj), template, indentation);
        // 替换掉实现体！
        for (int i = 0; i < getLength(arrayObj); i++) {
            Object o = get(arrayObj, i);
            if (o.getClass().isArray()){
                // 如果一个一维数组是基本类型
                if (o.getClass().getComponentType().isPrimitive()){
                    template = template.replace("{{arraymember" + i + "}}", toString(o, TO_STRING_TEMPLATE_PRIMITIVE_ARRAY, indentation + DEFAULT_ADD_INDENTATION));
                }else{
                    template = template.replace("{{arraymember" + i + "}}", toString(o, TO_STRING_TEMPLATE_TWO_DIMENSIONAL_ARRAY, indentation + DEFAULT_ADD_INDENTATION));
                }
            }
            template = template.replace("{{arraymember" + i + "}}", o.toString());
        }
        return template;
    }

    // 创建三个缩进字符！
    private static String parseTemplate(int length, String template, int indentation){
        StringBuilder indentationStringBuilder = new StringBuilder();
        for (int i = 0; i < indentation; i++) {
            indentationStringBuilder.append(" ");
        }
        String indentationStr = indentationStringBuilder.toString();

        // 定位到数组域的位置
        int arrayLeftSymbol = template.indexOf("[[");
        int arrayRightSymbol = template.lastIndexOf("]]");
        // 截取的部分去掉数组域的最左括号和最右括号
        String templateString = template.substring(arrayLeftSymbol + 1, arrayRightSymbol + 1);
        int i = templateString.indexOf("}}") + 1;
        int j = templateString.lastIndexOf("{{");
        int i1 = templateString.indexOf("{{");
        int j1 = templateString.lastIndexOf("}}") + 2;
        // 证明没有分割符号
        if (templateString.substring(i, i + 2).equalsIgnoreCase("}{")){
            StringBuilder stringBuilder = new StringBuilder();
            for (int j0 = 0; j0 < length; j0++){
                stringBuilder.append("{{arraymember").append(j0).append("}}");
            }
            String s = stringBuilder.toString().toString();
            templateString = templateString.replace("{{arraymember}}{{arraymember}}", s);
        }
        String split = templateString.substring(i + 1, j);
        StringBuilder stringBuilder = new StringBuilder();
        for (int k = 0; k < length; k++) {
            if (k == length - 1){
                stringBuilder.append("{{arraymember").append(k).append("}}");
            }else{
                stringBuilder.append("{{arraymember").append(k).append("}}").append(split);
            }
        }
        String expression = templateString.substring(i1, j1);
        templateString = templateString.replace(expression, stringBuilder.toString());
        String memberFields = template.substring(arrayLeftSymbol, arrayRightSymbol + 2);
        String replace = template.replace(memberFields, templateString);
        if (indentation > 0 && replace.contains("\n"))
            replace = replace.replace("\n", "\n" + indentationStr);
        System.out.println(replace);
        return replace;
    }



    public static <S, T extends S> void toList(T[] arrayObj, List<S> list){
        list.addAll(Arrays.asList(arrayObj));
    }
    public static <S, T extends S> int toSet(T[] arrayObj, Set<S> set){
        int count = 0;
        for (T t :
                arrayObj) {
            boolean add = set.add(t);
            if (!add)
                count++;
        }
        return count;
    }

    /**
     * @param arrayObj
     * @param map
     * @param keys
     * @param <V>
     * @param <T>
     * @param <K>
     * @return
     * @since 1.1
     */
    @SafeVarargs
    public static <V, T extends V, K> int toMap(T[] arrayObj, Map<K, V>map, K... keys){
        int loop = Math.min(arrayObj.length, keys.length);
        for (int i = 0; i < loop; i++) {
            map.put(keys[i], arrayObj[i]);
        }
        return loop;
    }

    /**
     * for each 遍历
     * @param array
     * @param foreachConsumer
     * @param <T>
     */
    public static <T> void foreach(T[] array, Consumer<T> foreachConsumer){
        for (T member :
                array) {
            foreachConsumer.accept(member);
        }
    }

    public static <T, V> V[] computeForeach(T[] array, Class<V> vClass, Function<T, V> foreachFunc){
        final V[] vArray = newArray2(vClass, array.length);
        int index = 0;
        for (T member :
                array) {
            final V apply = foreachFunc.apply(member);
            vArray[index++] = apply;
        }
        return vArray;
    }

    /**
     * 数组的计算形式复制, 如将Method[] 中的所有成员的getName()方法的结果组成的新数组
     * @param source
     * @param computeFunction
     * @param t2Class
     * @param <T1>
     * @param <T2>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T1, T2> T2[] computeCopy(T1[] source, Function<T1, T2> computeFunction, Class<T2> t2Class){
        final T2[] t2Array = (T2[]) newArray(t2Class, source.length);
        int index = 0;
        for (T1 member :
                source) {
            final T2 applyResult = computeFunction.apply(member);
            t2Array[index++] = applyResult;
        }
        return t2Array;
    }

    /**
     * 数组是否包含某个成员
     * @param arrayObj 待在寻找成员的数组
     * @param target 要查找的成员
     * @return 如果在数组中找到该成员, 则返回 {@code true}, 否则返回{@code false}
     */
    public static boolean contain(Object arrayObj, Object target){
        checkObjectIsArray(arrayObj);
        final int length = getLength(arrayObj);
        for (int i = 0; i < length; i++) {
            if (get(arrayObj, i).equals(target)){
                return true;
            }
        }
        return false;
    }

    /**
     * 快速判断数组内是否包含某个成员！
     * <strong>该方法旨在当 {@linkplain #contain(Object, Object)} 方法在执行效率上无法满足时
     * (如待查找的数组规模非常大，或者运行效率超时)
     * ，使用，该方法会对数组进行排序！
     * </strong>
     * @param array
     * @param target
     * @param fastMethod
     * @param <T>
     * @return
     */
    public static <T> boolean fastContain(T[] array, T target, BiFunction<T[], T[], T> fastMethod){
        return false;
    }

    // 定位方法
    public static int indexOf(Object arrayObj, Object target){
        checkObjectIsArray(arrayObj);
        final int length = getLength(arrayObj);
        for (int i = 0; i < length; i++) {
            if (get(arrayObj, i).equals(target)){
                return i;
            }
        }
        return -1;
    }

    // 反转数组

    /**
     * reverse array using double point! this method has high performance！(<code>O(n)</code>)
     * @since 1.2
     * @param arrayObj array object
     */
    public static void reverse(Object arrayObj){
        checkObjectIsArray(arrayObj);
        int lo = 0;
        int hi = getLength(arrayObj) - 1;
        // this loop make sure traveling once and reverse all array members!
        while(hi > lo){
            Object swap = get(arrayObj, lo);
            set(arrayObj, lo, get(arrayObj, hi));
            set(arrayObj, hi, swap);
            hi--;
            lo++;
        }
    }

    // TODO: 2024/4/16  乱序数组方法
    // TODO: 2024/4/16 范围乱序，无限缩小乱序

    public static void permute(Object arrayObj){

    }

    public static void permute(Object arrayObj, int lo, int hi){

    }

    public static void unsorted(Object arrayObj){

    }
}
