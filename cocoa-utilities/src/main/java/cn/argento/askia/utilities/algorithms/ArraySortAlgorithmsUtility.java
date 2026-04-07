package cn.argento.askia.utilities.algorithms;

import cn.argento.askia.annotations.Utility;

import java.util.Comparator;
import java.util.Objects;

/**
 * 数组排序算法工具类.
 * <p>本工具类提供了10大排序算法的实现，包括：
 * <ul>
 *     <li>比较型排序(7种)：冒泡排序、快速排序、归并排序、堆排序、插入排序, 选择排序, 希尔排序</li>
 *     <li>非比较型排序(3种)：桶排序、计数排序、基数排序</li>
 * </ul>
 * <p>所有的排序都基于 {@link java.util.Comparator}接口 和 {@link Comparable} 接口进行对比.
 *
 * @author Askia
 */

@Utility(value = "数组排序算法工具类", author = "Askia")
public class ArraySortAlgorithmsUtility {

    public enum Order{
        ASC, DESC
    }

    // 冒泡排序
    public static <T extends Comparable<? super T>> void bubble(T[] target, int lo, int hi, Order orderBy){

    }

    // 插入排序
    public static <T extends Comparable<? super T>> void insertion(T[] target, int lo, int hi, Order orderBy){

    }

    // 选择排序
    public static <T extends Comparable<? super T>> void selection(T[] target, int lo, int hi, Order orderBy){

    }

    // 希尔排序
    public static <T extends Comparable<? super T>> void shell(T[] target, int lo, int hi, Order orderBy){

    }
    // 快速排序
    public static <T extends Comparable<? super T>> void quick(T[] target, int lo, int hi, Order orderBy){

    }
    // 归并排序
    public static <T extends Comparable<? super T>> void merge(T[] target, int lo, int hi, Order orderBy){

    }
    // 堆排序
    public static <T extends Comparable<? super T>> void heap(T[] target, int lo, int hi, Order orderBy){

    }

    // 非比较排序





    // 泛型数组需要协变时，可以考虑定义新的继承性泛型来实现数组
    // 需要Comparable[]数组, 规定一种实现了Comparable接口的泛型T

    /**
     * 判断一个数组是否已经排好序.
     *
     * @param target 待排序的目标, 数组原始类型必须是实现了 {@link Comparable} 接口
     * @param orderBy {@link Order#ASC}代表升序, {@link Order#DESC}代表降序
     * @param <T> 所有实现了 {@link Comparable} 接口的泛型类
     * @return 如果已排好序则返回 {@code true} 否则返回 {@code false}
     * @since 2026.4.7
     * @see ArraySortAlgorithmsUtility#isSorted(Object[], Comparator, Order)
     * @see ArraySortAlgorithmsUtility#isSorted(int[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(byte[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(char[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(long[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(float[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(short[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(double[], Order)
     */
    public static <T extends Comparable<? super T>> boolean isSorted(T[] target, Order orderBy){
        Objects.requireNonNull(target, "提供的数组不能为NULL");
        boolean sorted = true;
        for (int i = 0, j = i + 1; j < target.length; j++, i++) {
            if (orderBy == Order.ASC && target[i].compareTo(target[j]) > 0){
                // 升序, 发现前面的大于后面的, 则sorted = false
                sorted = false;
                break;
            }
            if (orderBy == Order.DESC && target[i].compareTo(target[j]) < 0){
                sorted = false;
                break;
            }
        }
        return sorted;
    }

    /**
     * 判断一个double数组是否已经排好序.
     *
     * @param target 待排序的double类型数组
     * @param orderBy {@link Order#ASC}代表升序, {@link Order#DESC}代表降序
     * @return 如果已排好序则返回 {@code true} 否则返回 {@code false}
     * @since 2026.4.7
     * @see ArraySortAlgorithmsUtility#isSorted(Comparable[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(Object[], Comparator, Order)
     * @see ArraySortAlgorithmsUtility#isSorted(byte[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(int[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(short[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(long[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(float[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(char[], Order)
     */
    public static boolean isSorted(double[] target, Order orderBy){
        Objects.requireNonNull(target, "提供的数组不能为NULL");
        boolean sorted = true;
        for (int i = 0, j = i + 1; j < target.length; j++, i++) {
            if (orderBy == Order.ASC && Double.compare(target[i], target[j]) > 0){
                // 升序, 发现前面的大于后面的, 则sorted = false
                sorted = false;
                break;
            }
            if (orderBy == Order.DESC && Double.compare(target[i], target[j]) < 0){
                sorted = false;
                break;
            }
        }
        return sorted;
    }

    /**
     * 判断一个float数组是否已经排好序.
     *
     * @param target 待排序的float类型数组
     * @param orderBy {@link Order#ASC}代表升序, {@link Order#DESC}代表降序
     * @return 如果已排好序则返回 {@code true} 否则返回 {@code false}
     * @since 2026.4.7
     * @see ArraySortAlgorithmsUtility#isSorted(Comparable[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(Object[], Comparator, Order)
     * @see ArraySortAlgorithmsUtility#isSorted(byte[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(int[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(short[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(long[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(double[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(char[], Order)
     */
    public static boolean isSorted(float[] target, Order orderBy){
        Objects.requireNonNull(target, "提供的数组不能为NULL");
        boolean sorted = true;
        for (int i = 0, j = i + 1; j < target.length; j++, i++) {
            if (orderBy == Order.ASC && Float.compare(target[i], target[j]) > 0){
                // 升序, 发现前面的大于后面的, 则sorted = false
                sorted = false;
                break;
            }
            if (orderBy == Order.DESC && Float.compare(target[i], target[j]) < 0){
                sorted = false;
                break;
            }
        }
        return sorted;
    }

    /**
     * 判断一个float数组是否已经排好序.
     *
     * @param target 待排序的float类型数组
     * @param orderBy {@link Order#ASC}代表升序, {@link Order#DESC}代表降序
     * @return 如果已排好序则返回 {@code true} 否则返回 {@code false}
     * @since 2026.4.7
     * @see ArraySortAlgorithmsUtility#isSorted(Comparable[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(Object[], Comparator, Order)
     * @see ArraySortAlgorithmsUtility#isSorted(byte[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(int[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(short[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(long[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(double[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(char[], Order)
     */
    public static boolean isSorted(int[] target, Order orderBy){
        Objects.requireNonNull(target, "提供的数组不能为NULL");
        boolean sorted = true;
        for (int i = 0, j = i + 1; j < target.length; j++, i++) {
            if (orderBy == Order.ASC && target[i] > target[j]){
                // 升序, 发现前面的大于后面的, 则sorted = false
                sorted = false;
                break;
            }
            if (orderBy == Order.DESC && target[i] < target[j]){
                sorted = false;
                break;
            }
        }
        return sorted;
    }

    /**
     * 判断一个byte数组是否已经排好序.
     *
     * @param target 待排序的byte类型数组
     * @param orderBy {@link Order#ASC}代表升序, {@link Order#DESC}代表降序
     * @return 如果已排好序则返回 {@code true} 否则返回 {@code false}
     * @since 2026.4.7
     * @see ArraySortAlgorithmsUtility#isSorted(Comparable[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(Object[], Comparator, Order)
     * @see ArraySortAlgorithmsUtility#isSorted(float[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(int[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(short[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(long[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(double[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(char[], Order)
     */
    public static boolean isSorted(byte[] target, Order orderBy){
        Objects.requireNonNull(target, "提供的数组不能为NULL");
        boolean sorted = true;
        for (int i = 0, j = i + 1; j < target.length; j++, i++) {
            if (orderBy == Order.ASC && target[i] > target[j]){
                // 升序, 发现前面的大于后面的, 则sorted = false
                sorted = false;
                break;
            }
            if (orderBy == Order.DESC && target[i] < target[j]){
                sorted = false;
                break;
            }
        }
        return sorted;
    }

    /**
     * 判断一个short数组是否已经排好序.
     *
     * @param target 待排序的short类型数组
     * @param orderBy {@link Order#ASC}代表升序, {@link Order#DESC}代表降序
     * @return 如果已排好序则返回 {@code true} 否则返回 {@code false}
     * @since 2026.4.7
     * @see ArraySortAlgorithmsUtility#isSorted(Comparable[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(Object[], Comparator, Order)
     * @see ArraySortAlgorithmsUtility#isSorted(float[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(int[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(byte[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(long[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(double[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(char[], Order)
     */
    public static boolean isSorted(short[] target, Order orderBy){
        Objects.requireNonNull(target, "提供的数组不能为NULL");
        boolean sorted = true;
        for (int i = 0, j = i + 1; j < target.length; j++, i++) {
            if (orderBy == Order.ASC && target[i] > target[j]){
                // 升序, 发现前面的大于后面的, 则sorted = false
                sorted = false;
                break;
            }
            if (orderBy == Order.DESC && target[i] < target[j]){
                sorted = false;
                break;
            }
        }
        return sorted;
    }

    /**
     * 判断一个long数组是否已经排好序.
     *
     * @param target 待排序的long类型数组
     * @param orderBy {@link Order#ASC}代表升序, {@link Order#DESC}代表降序
     * @return 如果已排好序则返回 {@code true} 否则返回 {@code false}
     * @since 2026.4.7
     * @see ArraySortAlgorithmsUtility#isSorted(Comparable[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(Object[], Comparator, Order)
     * @see ArraySortAlgorithmsUtility#isSorted(float[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(int[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(byte[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(short[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(double[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(char[], Order)
     */
    public static boolean isSorted(long[] target, Order orderBy){
        Objects.requireNonNull(target, "提供的数组不能为NULL");
        boolean sorted = true;
        for (int i = 0, j = i + 1; j < target.length; j++, i++) {
            if (orderBy == Order.ASC && target[i] > target[j]){
                // 升序, 发现前面的大于后面的, 则sorted = false
                sorted = false;
                break;
            }
            if (orderBy == Order.DESC && target[i] < target[j]){
                sorted = false;
                break;
            }
        }
        return sorted;
    }

    /**
     * 判断一个char数组是否已经排好序.
     *
     * @param target 待排序的char类型数组
     * @param orderBy {@link Order#ASC}代表升序, {@link Order#DESC}代表降序
     * @return 如果已排好序则返回 {@code true} 否则返回 {@code false}
     * @since 2026.4.7
     * @see ArraySortAlgorithmsUtility#isSorted(Comparable[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(Object[], Comparator, Order)
     * @see ArraySortAlgorithmsUtility#isSorted(float[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(int[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(byte[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(short[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(double[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(long[], Order)
     */
    public static boolean isSorted(char[] target, Order orderBy){
        Objects.requireNonNull(target, "提供的数组不能为NULL");
        boolean sorted = true;
        for (int i = 0, j = i + 1; j < target.length; j++, i++) {
            if (orderBy == Order.ASC && target[i] > target[j]){
                // 升序, 发现前面的大于后面的, 则sorted = false
                sorted = false;
                break;
            }
            if (orderBy == Order.DESC && target[i] < target[j]){
                sorted = false;
                break;
            }
        }
        return sorted;
    }

    /**
     * 判断一个数组是否已经排好序.
     *
     * <p>此方法提供给那些没有实现了{@link Comparable} 接口的类, 因此需要提供一个 {@link Comparator} 比较器来进行比较</p>
     *
     * @param target 待排序的目标, 数组原始类型不限制
     * @param comparator 比较器对象
     * @param orderBy {@link Order#ASC}代表升序, {@link Order#DESC}代表降序
     * @param <T> 任何类型
     * @return 如果已排好序则返回 {@code true} 否则返回 {@code false}
     * @since 2026.4.7
     * @see ArraySortAlgorithmsUtility#isSorted(Comparable[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(int[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(byte[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(char[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(long[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(float[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(short[], Order)
     * @see ArraySortAlgorithmsUtility#isSorted(double[], Order)
     */
    public static <T> boolean isSorted(T[] target, Comparator<T> comparator, Order orderBy){
        Objects.requireNonNull(target, "提供的数组不能为NULL");
        boolean sorted = true;
        for (int i = 0, j = i + 1; j < target.length; j++, i++) {
            if (orderBy == Order.ASC && comparator.compare(target[i], target[j]) > 0){
                // 升序, 发现前面的大于后面的, 则sorted = false
                sorted = false;
                break;
            }
            if (orderBy == Order.DESC && comparator.compare(target[i], target[j]) < 0){
                sorted = false;
                break;
            }
        }
        return sorted;
    }
}
