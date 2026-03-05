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

    private ArrayUtility(){}

    private final static String TARGET_OBJECT_NOT_ARRAY = "参数 arrayObj 不是一个数组";

    // 检查对象是不是一个数组
    private static void checkObjectIsArray(Object arrayObj){
        if (!isArray(arrayObj))
            throw new IllegalArgumentException(TARGET_OBJECT_NOT_ARRAY);
    }
    private static void checkObjectIsArray(Object arrayObj, String paramName){
        String message = TARGET_OBJECT_NOT_ARRAY.replace("arrayObj", paramName);
        if (!isArray(arrayObj)){
            throw new IllegalArgumentException(message);
        }
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

    /**
     * 将一个Object类型的数组全部包装为特定泛型类型的数组.
     *
     * @param arrayObj 数组Object
     * @param vClass 要转换成的类型
     * @param <V> 类型参数
     * @return 特定类型的数组结果
     */
    public static <V> V[] getAll(Object arrayObj, Class<V> vClass){
        final int length = getLength(arrayObj);
        V[] arrayResult = newArray2(vClass, length);
        for (int i = 0 ;i < length; i++){
            final Object o = get(arrayObj, i);
            if (vClass.isAssignableFrom(o.getClass())){
                final V cast = vClass.cast(o);
                arrayResult[i] = cast;
            }
        }
        return arrayResult;
    }

    /**
     * 提供将{@linkplain Object Object}类型的数据解包成{@linkplain Object[] Object[]}的能力.
     *
     * <p>方法会检查提供的参数 {@code arrayObj} 是否是一个数组, 如果不是则会抛出{@link IllegalArgumentException}
     * <p>方法基于{@link System#arraycopy(Object, int, Object, int, int)}方法来进行数组复制
     *
     * @param arrayObj arrayObj
     * @return Object[] 数组
     * @throws IllegalArgumentException 如果参数 {@code arrayObj} 不是是一个数组
     * @see ArrayUtility#getAll(Object, Class)
     */
    public static Object[] getAll(Object arrayObj){
        checkObjectIsArray(arrayObj);
        final int length = getLength(arrayObj);
        Object[] objects = new Object[length];
        System.arraycopy(arrayObj, 0, objects, 0, length);
        return objects;
    }

    /**
     * 包装int基本类型数组成其对应的包装器类型{@link Integer}数组.
     * @param array int类型数组
     * @return 包装器类型 {@link Integer} 数组
     */
    public static Integer[] wrapPrimitiveIntArray(int[] array){
        if (array == null){
            return null;
        }
        if (array.length == 0){
            return new Integer[0];
        }
        Integer[] box = new Integer[array.length];
        for (int i = 0; i < array.length; i++) {
            box[i] = array[i];
        }
        return box;
    }

    /**
     * 包装long基本类型数组成其对应的包装器类型{@link Long}数组.
     * @param array long类型数组
     * @return 包装器类型 {@link Long} 数组
     */
    public static Long[] wrapPrimitiveLongArray(long[] array){
        if (array == null){
            return null;
        }
        if (array.length == 0){
            return new Long[0];
        }
        Long[] box = new Long[array.length];
        for (int i = 0; i < array.length; i++) {
            box[i] = array[i];
        }
        return box;
    }

    /**
     * 包装short基本类型数组成其对应的包装器类型{@link Short}数组.
     * @param array short类型数组
     * @return 包装器类型 {@link Short} 数组
     */
    public static Short[] wrapPrimitiveShortArray(short[] array){
        if (array == null){
            return null;
        }
        if (array.length == 0){
            return new Short[0];
        }
        Short[] box = new Short[array.length];
        for (int i = 0; i < array.length; i++) {
            box[i] = array[i];
        }
        return box;
    }

    /**
     * 包装byte基本类型数组成其对应的包装器类型{@link Byte}数组.
     * @param array byte类型数组
     * @return 包装器类型 {@link Byte} 数组
     */
    public static Byte[] wrapPrimitiveByteArray(byte[] array){
        if (array == null){
            return null;
        }
        if (array.length == 0){
            return new Byte[0];
        }
        Byte[] box = new Byte[array.length];
        for (int i = 0; i < array.length; i++) {
            box[i] = array[i];
        }
        return box;
    }

    /**
     * 包装double基本类型数组成其对应的包装器类型{@link Double}数组.
     * @param array double类型数组
     * @return 包装器类型 {@link Double} 数组
     */
    public static Double[] wrapPrimitiveDoubleArray(double[] array){
        if (array == null){
            return null;
        }
        if (array.length == 0){
            return new Double[0];
        }
        Double[] box = new Double[array.length];
        for (int i = 0; i < array.length; i++) {
            box[i] = array[i];
        }
        return box;
    }

    /**
     * 包装float基本类型数组成其对应的包装器类型{@link Float}数组.
     * @param array float类型数组
     * @return 包装器类型 {@link Float} 数组
     */
    public static Float[] wrapPrimitiveFloatArray(float[] array){
        if (array == null){
            return null;
        }
        if (array.length == 0){
            return new Float[0];
        }
        Float[] box = new Float[array.length];
        for (int i = 0; i < array.length; i++) {
            box[i] = array[i];
        }
        return box;
    }

    /**
     * 解包包装器类型 {@link Integer} 数组到对应的基本类型 int
     * @param array Integer 包装器类型数组
     * @return 基本类型 int 数组
     */
    public static int[] unwrapBoxingIntegerArray(Integer[] array){
        if (array == null){
            return null;
        }
        if (array.length == 0){
            return new int[0];
        }
        int[] primitive = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            primitive[i] = array[i];
        }
        return primitive;
    }

    /**
     * 解包包装器类型 {@link Long} 数组到对应的基本类型 long
     * @param array Long 包装器类型数组
     * @return 基本类型 long 数组
     */
    public static long[] unwrapBoxingLongArray(Long[] array){
        if (array == null){
            return null;
        }
        if (array.length == 0){
            return new long[0];
        }
        long[] primitive = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            primitive[i] = array[i];
        }
        return primitive;
    }

    /**
     * 解包包装器类型 {@link Short} 数组到对应的基本类型 short
     * @param array Short 包装器类型数组
     * @return 基本类型 short 数组
     */
    public static short[] unwrapBoxingShortArray(Short[] array){
        if (array == null){
            return null;
        }
        if (array.length == 0){
            return new short[0];
        }
        short[] primitive = new short[array.length];
        for (int i = 0; i < array.length; i++) {
            primitive[i] = array[i];
        }
        return primitive;
    }

    /**
     * 解包包装器类型 {@link Byte} 数组到对应的基本类型 byte
     * @param array Byte 包装器类型数组
     * @return 基本类型 byte 数组
     */
    public static byte[] unwrapBoxingByteArray(Byte[] array){
        if (array == null){
            return null;
        }
        if (array.length == 0){
            return new byte[0];
        }
        byte[] primitive = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            primitive[i] = array[i];
        }
        return primitive;
    }

    /**
     * 解包包装器类型 {@link Double} 数组到对应的基本类型 double
     * @param array Double 包装器类型数组
     * @return 基本类型 double 数组
     */
    public static double[] unwrapBoxingByteArray(Double[] array){
        if (array == null){
            return null;
        }
        if (array.length == 0){
            return new double[0];
        }
        double[] primitive = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            primitive[i] = array[i];
        }
        return primitive;
    }

    /**
     * 解包包装器类型 {@link Float} 数组到对应的基本类型 float
     * @param array Float 包装器类型数组
     * @return 基本类型 float 数组
     */
    public static float[] unwrapBoxingByteArray(Float[] array){
        if (array == null){
            return null;
        }
        if (array.length == 0){
            return new float[0];
        }
        float[] primitive = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            primitive[i] = array[i];
        }
        return primitive;
    }



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
     * @param arrayObj 数组
     * @param member 成员
     * @throws ClassCastException 如果不能放入则抛出此ClassCastException异常
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
     * @return 新数组对象
     * @since 1.0
     */
    public static Object newArray(Class<?> componentType, int ...dimensions){
        return Array.newInstance(componentType, dimensions);
    }

    /**
     * 创建一个一维且指定类型的数组.
     * @param componentType 数组的原始类型
     * @param length 数组长度
     * @param <T> 数组类型
     * @return 特定类型的数组
     * @deprecated 这个方法创建出来的数组可能并不是开发者想要的形式！并且可能会抛出莫名其妙的异常！
     * @since 2026.2.9 此方法进内部API使用
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    public static <T> T[] newArray2(Class<? extends T> componentType, int length) {
        Objects.requireNonNull(componentType);
        if (length < 0 ){
            throw new IllegalArgumentException("长度是负数：" + length);
        }
        return (T[]) Array.newInstance(componentType, length);
    }

    /**
     * 创建一个一维数组并指定长度.
     *
     * @param componentType 数组的原始类型
     * @param length 数组的长度
     * @return {@link Object}类型的兼容数组对象
     */
    public static Object newArray(Class<?> componentType, int length){
        return Array.newInstance(componentType, length);
    }

    /**
     * 创建一个对象矩阵(二维数组).
     *
     * @param objectType 对象类型
     * @param m 二位数组长度
     * @param n 二维数组宽度
     * @param <T> 类型
     * @return 二维对象数组
     */
    @SuppressWarnings("unchecked")
    public static <T> T[][] newObjectMatrix(Class<T> objectType, int m, int n){
        return (T[][]) Array.newInstance(objectType, m, n);
    }

    /**
     * 创建一个矩阵.
     * @param m 二位数组长度
     * @param n 二维数组宽度
     * @return 整数矩阵
     */
    public static Integer[][] newMatrix(int m, int n){
        return newObjectMatrix(Integer.class, m, n);
    }


    /**
     * 数组扩容
     *
     * @param arrayObj 原始数组
     * @param expandSize 扩容大小
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
     * 增强版本 {@linkplain System#arraycopy(Object, int, Object, int, int) System.arraycopy}.
     * <p>增强版本提供了：
     * <ol>
     *     <li></li>
     * </ol>
     *
     * @param src
     * @param srcOffset
     * @param desc
     * @param descOffset
     * @param length
     * @apiNote 原版 {@link System#arraycopy(Object, int, Object, int, int)} 的复制有如下的注意点：<ol>
     *     <li>若src和dest参数指向同一数组对象，则执行的复制操作将按以下方式处理：首先将srcPos至srcPos+length-1位置的组件复制到长度为components的临时数组，随后将该临时数组的内容复制至目标数组的destPos至destPos+length-1位置</li>
     *     <li>若dest为null值，则抛出NullPointerException异常。</li>
     *     <li>若src为null值，则抛出NullPointerException异常且目标数组未被修改。</li>
     *     <li>若src或者dest参数所指对象并非数组时抛出ArrayStoreException</li>
     *     <li>src参数和dest参数所指数组的组件类型（ComponentType,即该数组的原始类型，比如int[]则是int）为不同的基本类型。抛出ArrayStoreException</li>
     *     <li>src是基本类型数组而desc是引用类型数组或者反过来，desc是基本类型数组，src是引用类型数组时，抛出ArrayStoreException</li>
     *     <li>srcPos或者destPos参数为负值，抛出IndexOutOfBoundsException</li>
     *     <li>当srcPos+length大于src.length（即源数组的长度）或者destPos+length大于dest.length，即目标数组的长度，抛出IndexOutOfBoundsException</li>
     *     <li>若源数组中从srcPos到srcPos+length-1位置的任何实际组件无法通过赋值转换转换为目标数组的组件类型，则会抛出ArrayStoreException异常。 此时，设k为小于length的最小非负整数，使得src[srcPos+k]无法转换为目标数组的组件类型；当异常发生时，源数组中从srcPos到srcPos+k-1位置的组件已全部复制到目标数组的destPos到destPos+k-1位置，且目标数组的其他位置未被修改。（由于已列出的限制条件，本段落实际上仅适用于两个数组的组件类型均为引用类型的情况。）</li>
     * </ol>
     */
    public void arrayCopySuperbly(Object src, int srcOffset, Object desc, int descOffset, int length){
        // 1. 首先先确认两个数据源是否是数组类型
        checkObjectIsArray(src, "src");
        checkObjectIsArray(desc, "desc");
        // 全程都要考虑负向索引
        int srcLength = getLength(src);
        int descLength = getLength(desc);
        int srcOffsetReal = srcOffset;
        int descOffsetReal = descOffset;
        // 如果索引是负的，则代表末端倒数第几个成员
        if (srcOffset < 0){
            srcOffsetReal = srcLength + srcOffset;
        }
        if (descOffset < 0){
            descOffsetReal = descLength + descOffset;
        }
        // 2. 考虑包装器装箱和解包
        try{
            if (LangUtility.isAble2BoxingOrUnboxing(src.getClass(), desc.getClass(), false)){
                // 此时src和desc要么一个包装器类，要么一个基本类型
                // 转二进制代码再转换成数值
            }
            return;
        }
        catch (Exception e){
            // 不是包装器类的成员Copy
        }

        // 3. 考虑兼容层，即小字节量基本类型转大字节量基本类型
        try{

            return;
        }
        catch (Exception e){

        }
        // 4. 最后委托System.arraycopy()
        System.arraycopy(src, srcOffsetReal, desc, descOffsetReal, length);
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

    /**
     *
     * @param arrayObj
     * @param beginIndex
     * @param length
     * @return
     */
    public static Object copyOfLength(Object arrayObj, int beginIndex, int length){
        return copyOfLength(arrayObj, beginIndex, length, 0 , 0);
    }

    /**
     *
     * @param arrayObj
     * @param beginIndex
     * @param length
     * @param newArrayLength
     * @return
     */
    public static Object copyOfLength(Object arrayObj, int beginIndex, int length, int newArrayLength){
        return copyOfLength(arrayObj, beginIndex, length, newArrayLength, 0);
    }

    /**
     *
     * @param arrayObj
     * @param beginIndex
     * @param length
     * @param newArrayLength
     * @param newArrayCopyBeginIndex
     * @return
     */
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

    /**
     * 判断是否为空数组
     * @param arrayObj
     * @return
     * @since 2024.11.17
     */
    public static boolean isEmpty(Object arrayObj){
        checkObjectIsArray(arrayObj);
        Objects.requireNonNull(arrayObj, "数组不能为null");
        return getLength(arrayObj) == 0;
    }

    /**
     * 判断数组不能为空！
     * @param arrayObj
     * @since 2024.11.17
     */
    private static void checkArrayNotEmpty(Object arrayObj){
        if (isEmpty(arrayObj)){
            throw new ArrayIndexOutOfBoundsException("数组为空");
        }
    }


    private static int LARGE_STR_GAPS = 35;

    /**
     * toDelimiterString(arrayObj, ",");
     * @param arrayObj
     * @return
     * @see #toDelimiterString(Object, String)
     * @since 2024.11.17
     */
    public static String toDelimiterString(Object arrayObj){
        return toDelimiterString(arrayObj, ",");
    }

    /**
     * 输出带分隔符格式的字符串，如 [ 1, 2 ,3 ], 该方法会判断输出对象的长度, 当长度大于{@code LARGE_STR_GAPS}的时候，将会采用
     * 一行一个的形式进行输出, 默认{@code LARGE_STR_GAPS}等于35
     *
     * @param arrayObj
     * @param delimiter
     * @return
     * @since 2024.11.17
     */
    public static String toDelimiterString(Object arrayObj, String delimiter){
        checkObjectIsArray(arrayObj);
        checkArrayNotEmpty(arrayObj);
        final int length = getLength(arrayObj);
        boolean largeStr = false;
        for (int i = 0; i < length; i++) {
            // 大字符，则需要换行输出
            if(get(arrayObj, i).toString().length() >= LARGE_STR_GAPS){
                largeStr = true;
                break;
            }
        }
        if (largeStr){
            // 一行一个数据
            return toDelimiterString(arrayObj, delimiter + System.lineSeparator() + "\t",
                    "[" + System.lineSeparator() + "\t" , System.lineSeparator() + "]");
        }
        else{
            // for example ==> [ 1, 2 ,3 ]
            return toDelimiterString(arrayObj, delimiter + " ", "[ ", " ]");
        }
    }

    /**
     * 输出带分隔符格式的字符串，如 [ 1, 2 ,3 ]
     * @param arrayObj
     * @param delimiter
     * @param prefix
     * @param suffix
     * @return
     * @since 2024.11.17
     */
    public static String toDelimiterString(Object arrayObj, String delimiter,
                                           String prefix, String suffix){
        checkObjectIsArray(arrayObj);
        checkArrayNotEmpty(arrayObj);
        StringJoiner delimiterString = new StringJoiner(delimiter, prefix, suffix);
        final int length = getLength(arrayObj);
        for (int i = 0; i < length; i++) {
            final Object o = get(arrayObj, i);
            delimiterString.add(o.toString());
        }
        return delimiterString.toString();
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

    public static <S, T extends S> Set<S> toSet(T[] arrayObj){
        Set<S> set = new HashSet<>(Arrays.asList(arrayObj));
        return set;
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
     * 使用并行流优化的Contain().
     * 如果您的数据量很大, 达到百万级别, 千万级别时, 使用此方法替换传统的Contain()时会更加节省时间.
     * 该方法在的处理在数据量处于百万和千万中间层时，效率最高，符合正态分布。
     * <b>其他情况使用此方法可能会导致效率下降, 出于稳定性考虑我们不建议经常使用此方法, 在低数据量的时候此方法的表现远远低于普通的contain(), 即便数据量达到百万级别时，部分情况下仍然会远低于普通Contain()方法，出现此情况的原因是并行流要拆分、合并、线程调度，启动成本 > 计算节省</b>
     * @param array
     * @param target
     * @param <T>
     * @return
     */
    public static <T> boolean fastContain(T[] array, Object target){
        return Arrays.stream(array).parallel().anyMatch(target::equals);
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

    @SuppressWarnings("unchecked")
    // 合并数组
    public static <A> A[] combine(A[] array1, A[] array2, boolean removeDuplicate){
        Collection<A> collection;
        if (removeDuplicate){
            collection = new HashSet<>();
        }
        else{
            collection = new ArrayList<>();
        }
        collection.addAll(Arrays.asList(array1));
        collection.addAll(Arrays.asList(array2));
        return collection.toArray((A[])ArrayUtility.newArray(array1.getClass().getComponentType(), 0));
    }

    /**
     * 创建一个包含特定成员的数组
     * @param aTypeObject 成员
     * @param <A> 任何泛型支持的类型
     * @return 直接调用此方法将返回空数据
     */
    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <A> A[] as(A... aTypeObject){
        if (aTypeObject.length == 0){
            return (A[]) newArray2(aTypeObject.getClass().getComponentType(), 0);
        }
        else{
            return as(aTypeObject.length, aTypeObject);
        }
    }

    @SuppressWarnings("unchecked")
    @SafeVarargs
    private static <A> A[] as(int arrayLength, A... aTypeObject){
        int originalArrayLength = aTypeObject.length;
        final Class<?> componentType = aTypeObject.getClass().getComponentType();
        int realArrayLength = Math.max(originalArrayLength, arrayLength);
        if (aTypeObject.length == 0){
            // 退化为创建数组
            return (A[]) newArray2(componentType, realArrayLength);
        }
        final A[] objects = (A[]) newArray2(componentType, realArrayLength);
        System.arraycopy(aTypeObject, 0, objects, 0, originalArrayLength);
        return objects;
    }


    /**
     * 严格的数组判等, 要求：顺序相同，元素相同
     * @param array1
     * @param array2
     * @return
     */
    public static boolean equalsStrictly(Object array1, Object array2){
        checkObjectIsArray(array1);
        checkObjectIsArray(array2);
        // 长度不同则肯定false
        if (getLength(array1) != getLength(array2)){
            return false;
        }
        final int length = getLength(array1);
        for (int i = 0; i < length; i++) {
            final Object objFromArray1 = get(array1, i);
            final Object objFromArray2 = get(array2, i);
            if (!Objects.equals(objFromArray1, objFromArray2)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 严格的数组判等.
     * <p>此方法要求数组的每一个成员都要完全匹配.例如, 对于下面两个数组时：<br>
     * <pre>[1, 2, 3, 4, 5]</pre>
     * <pre> ⬇  ⬇  ⬇  ⬇  ⬇ </pre>
     * <pre>[1, 2, 3, 4, 5]</pre>
     * <p>由于其每个成员之间都是相等的, 所以此方法会返回 {@code true} .而对于：<br>
     * <pre>[1, 2, 3, 5, 4]</pre>
     * <pre> ⬇  ⬇  ⬇  ×  × </pre>
     * <pre>[1, 2, 3, 4, 5]</pre>
     * <p>虽然在总体上来说，此数组中的所有元素是相同的, 但是由于其中的5和4顺序不同, 则不符合方法要求的【严格】判等, 所以方法返回 {@code false}
     * <p><b>此方法比较的两个数组不要求类型相同, 只要你愿意, 你完全可以使用两种不同的类型的数组进行比较,
     * 比如比较 {@linkplain java.net.InetAddress InetAddress} 和 {@linkplain java.net.SocketAddress SocketAddress},
     * 你只需要指定他们之间的比较规则即可</b>
     * <p><b>此方法的另外一个不足在于只能比较对象, 因此如果希望比较基本类型数组, 则只能使用他们的包装器类, 参考本工具类的{@link ArrayUtility#wrapPrimitiveByteArray(byte[])}等方法</b>
     * @param array1 数组1
     * @param array2 数组2
     * @param equalFunction 数组1和数组2的原始类型比较函数, 返回boolean类型, 返回true代表相同, false代表不相同
     * @param <T1> 类型1, 可以不和类型2相同
     * @param <T2> 类型2, 可以不和类型1相同
     * @return 如果两个数组相等则返回true, 否则返回false
     * @since 2025.12.18
     * @see ArrayUtility#equalsStrictly(Object, Object)
     */
    public static <T1, T2> boolean equalsStrictlyIgnoreTypeMatch(T1[] array1, T2[] array2, BiFunction<T1, T2, Boolean> equalFunction){
        if (array1.length != array2.length){
            return false;
        }
        for (int i = 0; i < array1.length; i++) {
            if (!equalFunction.apply(array1[i], array2[i])){
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        int[] ints = RandomUtility.randomIntArray(6000000);
        long l = System.nanoTime();
        boolean contain = ArrayUtility.contain(ints, 2);
        long l1 = System.nanoTime();
        System.out.println("contain = " + contain);
        System.out.println("l0 = " + l + ", l1 = " + l1 + ", l1 - l1 = " + (l1 - l));

        Integer[] integers = Arrays.stream(ints).boxed().toArray(Integer[]::new);

        long l2 = System.nanoTime();
        boolean contain2 = ArrayUtility.fastContain(integers, 2);
        long l3 = System.nanoTime();
        System.out.println("contain2 = " + contain2);
        System.out.println("l2 = " + l2 + ", l3 = " + l3 + ", l3 - l2 = " + (l3 - l2));


    }
}
