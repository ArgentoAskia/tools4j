package cn.argento.askia.utilities.algorithms;

import cn.argento.askia.annotations.Utility;
import cn.argento.askia.utilities.collection.ArrayUtility;
import cn.argento.askia.utilities.lang.AssertionUtility;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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

    public static void bubbleSort(Comparable<?>[] target, Order orderBy){

    }



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
