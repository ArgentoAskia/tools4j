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

    private ArraySortAlgorithmsUtility(){
        throw new IllegalAccessError("ArraySortAlgorithmsUtility为工具类, 无法创建该类的对象");
    }

    /**
     * 顺序常量.
     *
     * <p>决定了排序算法使用什么顺序进行排序, 其中{@link Order#ASC}代表升序, {@link Order#DESC}代表降序</p>
     *
     * @author Admin
     * @since 2026.4.7
     */
    public enum Order{
        ASC, DESC
    }

    /**
     * 数组内交换函数.
     * <p>用于交换数组内两个下标位置的数据</p>
     *
     * @param target 数组变量, 数组引用
     * @param i 交换位置1
     * @param j 交换位置2
     * @param <T> 任何除了基本类型之外的数据类型
     */
    private static <T> void exchange(T[] target, int i, int j){
        T temp = target[i];
        target[i] = target[j];
        target[j] = temp;
    }

    /**
     * 数据有序性函数.
     * <p>判断两个数据是否已经按照特定的顺序排好序, 例如, 提供了数据{@code 2}和{@code 3}, 我们希望判断这两个数据的顺序是否已经按照{@code ASC}排序, 则此函数返回{@code true}</p>
     * <p>此函数为双向切分函数, 即结果只有两个, 要么已有序, 要么非有序, 此函数将相等的情况划分到非有序集中, 因此在实际数组比较时, 需要减少比较次数的时候可以考虑使用后一个数据来比较前一个数据的有序性, 这样会省略掉相等情况下的数据交换, 例如你有一个数组{@code [2, 3, 5, 7]}, 你希望判断有序性为{@code ASC}来决定是否需要进行成员交换, 此时调用{@code ordered(3, 2, ASC)}返回{@code true}时才进行交换会更优于调用{@code !ordered(2, 3, ASC)}, 因为后者会让相等时也进行交换</p>
     * @param t1 数据1
     * @param t2 数据2
     * @param order 顺序枚举量, 参考{@link Order}
     * @return 如果有序, 则返回{@code true}, 否则返回{@code false}
     * @param <T> 任何实现了{@link Comparable}接口的类
     * @see ArraySortAlgorithmsUtility#ordered(Object, Object, Order, Comparator)
     */
    private static <T extends Comparable<? super T>> boolean ordered(T t1, T t2, Order order){
        if (order == Order.ASC){
            // 升序则判断t1是否小于t2
            return t1.compareTo(t2) < 0;
        }
        else{
            // 降序则判断是否大于t2
            return t1.compareTo(t2) > 0;
        }
    }

    /**
     * 数据有序性函数(双向切分).
     * <p>判断两个数据是否已经按照特定的顺序排好序</p>
     * <p>此函数为双向切分函数, 即结果只有两个, 要么已有序, 要么非有序, 此函数将相等的情况划分到非有序集中, 因此在实际数组比较时, 需要减少比较次数的时候可以考虑使用后一个数据来比较前一个数据的有序性, 这样会省略掉相等情况下的数据交换, 例如你有一个数组{@code [2, 3, 5, 7]}, 你希望判断有序性为{@code ASC}来决定是否需要进行成员交换, 此时调用{@code ordered(3, 2, ASC)}返回{@code true}时才进行交换会更优于调用{@code !ordered(2, 3, ASC)}, 因为后者会让相等时也进行交换</p>
     * @param t1 数据1
     * @param t2 数据2
     * @param order 顺序枚举量, 参考{@link Order}
     * @param comparator 比较器
     * @param <T> 任何非基础类型, 不要求一定实现{@link Comparable}
     * @return 如果有序, 则返回{@code true}, 否则返回{@code false}
     * @author Admin
     * @since 2026.4.7
     * @see ArraySortAlgorithmsUtility#ordered(Comparable, Comparable, Order)
     */
    private static <T> boolean ordered(T t1, T t2, Order order, Comparator<? super T> comparator){
        if (order == Order.ASC){
            return comparator.compare(t1, t2) < 0;
        }
        else{
            return comparator.compare(t1, t2) > 0;
        }
    }

    /**
     * 数据有序性函数(三向切分).
     * <p>判断两个数据是否已经按照特定的顺序排好序</p>
     * <p>此函数为三向切分函数, 会单独考虑等于的情况, 当满足顺序时返回{@code 1}, 相等时返回{@code 0}, 不满足顺序时返回{@code -1}</p>
     * <p>此函数满足以下注意点：</p>
     * <ul>
     *     <li>{@link ArraySortAlgorithmsUtility#ordered(Comparable, Comparable, Order)} 返回{@code true}时此函数必返回{@code 1}</li>
     *     <li>{@link ArraySortAlgorithmsUtility#ordered(Comparable, Comparable, Order)} 返回{@code false}时此函数可能是{@code 0}或者{@code -1}</li>
     *     <li>{@link ArraySortAlgorithmsUtility#ordered(Comparable, Comparable, Order)} 的反向比较对此函数仍然有效</li>
     * </ul>
     * @param t1 数据1
     * @param t2 数据2
     * @param order 顺序枚举量, 参考{@link Order}
     * @return 当满足顺序时返回{@code 1}, 相等时返回{@code 0}, 不满足顺序时返回{@code -1}
     * @param <T> 任何实现了{@link Comparable}接口的类
     * @author Askia
     * @since 2026.4.7
     */
    private static <T extends Comparable<? super T>> int orderedEquals(T t1, T t2, Order order){
        if (order == Order.ASC){
            return Integer.compare(0, t1.compareTo(t2));
        }
        else{
            return Integer.compare(t1.compareTo(t2), 0);
        }
    }

    /**
     * 基础数组检查函数.
     * <p>检查项目包括：</p>
     * <ul>
     *     <li>下界是否小于{@code 0}, 正常数组的最小下标只能是{@code 0}</li>
     *     <li>上界是否小于{@code 0}</li>
     *     <li>下界是否大于上界</li>
     *     <li>下界是否大于等于数组长度</li>
     *     <li>上界是否大于等于数组长度</li>
     * </ul>
     * @param lo lo下界
     * @param hi hi上界
     * @param length 数组长度
     */
    private static void checkRange(int lo, int hi, int length){
        if (lo < 0){
            throw new IllegalArgumentException("lo < 0");
        }
        if (hi < 0){
            throw new IllegalArgumentException("hi < 0");
        }
        if (lo > hi)
            throw new IllegalArgumentException("下届不能大于上届, lo = " + lo + ", hi = " + hi);
        if (lo >= length){
            throw new ArrayIndexOutOfBoundsException("下届越界, 必须小于等于"+ (length - 1) +", lo = " + lo + ", length = " + length);
        }
        if (hi >= length){
            throw new ArrayIndexOutOfBoundsException("上届越界, 必须小于等于"+ (length - 1) +", hi = " + hi + ", length = " + length);
        }
    }

    // ============================= 冒泡排序 =============================

    /**
     * 冒泡排序
     *
     * @param target 待排序的目标, 数组原始类型必须是实现了 {@link Comparable} 接口
     * @param lo 下界, 最小为0, 最大为数组成员数减1
     * @param hi 上界, 最小为0, 最大为数组成员数减1
     * @param orderBy {@link Order#ASC}代表升序, {@link Order#DESC}代表降序
     * @param <T> 所有实现了 {@link Comparable} 接口的类
     * @since 2026.4.7
     * @see ArraySortAlgorithmsUtility#bubble(Comparable[])
     * @see ArraySortAlgorithmsUtility#bubble(Comparable[], Order)
     *
     * @see ArraySortAlgorithmsUtility#bubble(Object[], Comparator)
     * @see ArraySortAlgorithmsUtility#bubble(Object[], Order, Comparator)
     * @see ArraySortAlgorithmsUtility#bubble(Object[], int, int, Order, Comparator)
     */
    public static <T extends Comparable<? super T>> void bubble(T[] target, int lo, int hi, Order orderBy){
        checkRange(lo, hi, target.length);
        // 外层循环是要冒泡的规模，因为每次排好一个数所以最后会剩下一个数，因此最后一趟不用排，因此总共需要循环n-1次，这也是为什么从1开始的原因
        // 上一次发生交换的位置
        int k = hi;
        for (int i = (lo + 1); i <= hi; i++) {
            // 设定一个标记，若为true，则表示此次循环没有进行交换，也就是待排序列已经有序，排序已经完成。
            boolean flag = true;
            // 标记交换位置
            int pos = lo;
            // 冒泡过程
            for (int j = lo; j < k; j++) {
                if (ordered(target[j + 1], target[j], orderBy)) {
                    exchange(target, j, j + 1);
                    flag = false;
                    // 设置为当前发生交换的位置
                    pos = j;
                }
            }
            // 如果flag仍然是true，则代表当前数组已没有发生过交换，此时退出循环即可
            if (flag) {
                break;
            }
            // 下一次比较到记录位置即可
            k = pos;
        }
    }

    /**
     * 冒泡排序
     *
     * @param target  待排序的目标, 数组原始类型必须是实现了 {@link Comparable} 接口
     * @param orderBy {@link Order#ASC}代表升序, {@link Order#DESC}代表降序
     * @param <T> 所有实现了 {@link Comparable} 接口的类
     * @since 2026.4.7
     * @see ArraySortAlgorithmsUtility#bubble(Comparable[])
     * @see ArraySortAlgorithmsUtility#bubble(Comparable[], int, int, Order)
     *
     * @see ArraySortAlgorithmsUtility#bubble(Object[], Comparator)
     * @see ArraySortAlgorithmsUtility#bubble(Object[], Order, Comparator)
     * @see ArraySortAlgorithmsUtility#bubble(Object[], int, int, Order, Comparator)
     */
    public static <T extends Comparable<? super T>> void bubble(T[] target, Order orderBy){
        bubble(target, 0, target.length - 1, orderBy);
    }

    /**
     * 冒泡排序
     *
     * @param target 待排序的目标, 数组原始类型必须是实现了 {@link Comparable} 接口
     * @param <T> 所有实现了 {@link Comparable} 接口的类
     * @since 2026.4.7
     * @see ArraySortAlgorithmsUtility#bubble(Comparable[], Order)
     * @see ArraySortAlgorithmsUtility#bubble(Comparable[], int, int, Order)
     *
     * @see ArraySortAlgorithmsUtility#bubble(Object[], Comparator)
     * @see ArraySortAlgorithmsUtility#bubble(Object[], Order, Comparator)
     * @see ArraySortAlgorithmsUtility#bubble(Object[], int, int, Order, Comparator)
     */
    public static <T extends Comparable<? super T>> void bubble(T[] target){
        bubble(target, 0, target.length - 1, Order.ASC);
    }

    /**
     * 冒泡排序
     *
     * <p>此方法提供给那些没有实现了{@link Comparable} 接口的类, 因此需要提供一个 {@link Comparator} 比较器来进行比较</p>
     *
     * @param target 待排序的目标, 数组原始类型不限制
     * @param lo 下界, 最小为0, 最大为数组成员数减1
     * @param hi 上界, 最小为0, 最大为数组成员数减1
     * @param orderBy {@link Order#ASC}代表升序, {@link Order#DESC}代表降序
     * @param comparator 比较器对象
     * @param <T> 任何类型
     * @since 2026.4.7
     * @see ArraySortAlgorithmsUtility#bubble(Comparable[])
     * @see ArraySortAlgorithmsUtility#bubble(Comparable[], Order)
     * @see ArraySortAlgorithmsUtility#bubble(Comparable[], int, int, Order)
     *
     * @see ArraySortAlgorithmsUtility#bubble(Object[], Comparator)
     * @see ArraySortAlgorithmsUtility#bubble(Object[], Order, Comparator)
     */
    public static <T> void bubble(T[] target, int lo, int hi, Order orderBy, Comparator<T> comparator){
        checkRange(lo, hi, target.length);
        // 外层循环是要冒泡的规模，因为每次排好一个数所以最后会剩下一个数，因此最后一趟不用排，因此总共需要循环n-1次，这也是为什么从1开始的原因
        // 上一次发生交换的位置
        int k = hi;
        for (int i = (lo + 1); i <= hi; i++) {
            // 设定一个标记，若为true，则表示此次循环没有进行交换，也就是待排序列已经有序，排序已经完成。
            boolean flag = true;
            // 标记交换位置
            int pos = lo;
            // 冒泡过程
            for (int j = lo; j < k; j++) {
                if (ordered(target[j + 1], target[j], orderBy, comparator)) {
                    exchange(target, j, j + 1);
                    flag = false;
                    // 设置为当前发生交换的位置
                    pos = j;
                }
            }
            // 如果flag仍然是true，则代表当前数组已没有发生过交换，此时退出循环即可
            if (flag) {
                break;
            }
            // 下一次比较到记录位置即可
            k = pos;
        }
    }

    /**
     * 冒泡排序
     *
     * <p>此方法提供给那些没有实现了{@link Comparable} 接口的类, 因此需要提供一个 {@link Comparator} 比较器来进行比较</p>
     *
     * <p>此方法默认将整个数组排序, 即排序范围为 {@code [0, array.length - 1]}
     *
     * @param target 待排序的目标, 数组原始类型不限制
     * @param orderBy {@link Order#ASC}代表升序, {@link Order#DESC}代表降序
     * @param comparator 比较器对象
     * @param <T> 任何类型
     * @since 2026.4.7
     * @see ArraySortAlgorithmsUtility#bubble(Comparable[])
     * @see ArraySortAlgorithmsUtility#bubble(Comparable[], Order)
     * @see ArraySortAlgorithmsUtility#bubble(Comparable[], int, int, Order)
     *
     * @see ArraySortAlgorithmsUtility#bubble(Object[], int, int, Order, Comparator)
     * @see ArraySortAlgorithmsUtility#bubble(Object[], Comparator)
     */
    public static <T> void bubble(T[] target, Order orderBy, Comparator<T> comparator){
        bubble(target, 0, target.length - 1, orderBy, comparator);
    }

    /**
     * 冒泡排序
     *
     * <p>此方法提供给那些没有实现了{@link Comparable} 接口的类, 因此需要提供一个 {@link Comparator} 比较器来进行比较</p>
     *
     * <p>此方法默认将整个数组按照升序顺序排序, 即排序范围为 {@code [0, array.length - 1]}, 顺序使用 {@link Order#ASC}
     *
     * @param target 待排序的目标, 数组原始类型不限制
     * @param comparator 比较器对象
     * @param <T> 任何类型
     * @since 2026.4.7
     * @see ArraySortAlgorithmsUtility#bubble(Comparable[])
     * @see ArraySortAlgorithmsUtility#bubble(Comparable[], Order)
     * @see ArraySortAlgorithmsUtility#bubble(Comparable[], int, int, Order)
     *
     * @see ArraySortAlgorithmsUtility#bubble(Object[], int, int, Order, Comparator)
     * @see ArraySortAlgorithmsUtility#bubble(Object[], Order, Comparator)
     */
    public static <T> void bubble(T[] target, Comparator<T> comparator){
        bubble(target, 0, target.length - 1, Order.ASC, comparator);
    }

    // ============================= 冒泡排序 =============================

    // ============================= 插入排序 =============================
    // 插入排序
    public static <T extends Comparable<? super T>> void insertion(T[] target, int lo, int hi, Order orderBy){

    }
    // ============================= 插入排序 =============================

    // ========================= 选择排序 =========================
    /**
     * 选择排序
     *
     * <p>代码来源自普林斯顿老爷子的《算法4》
     *
     * @param target 待排序的目标, 数组原始类型必须是实现了 {@link Comparable} 接口
     * @param lo 下界, 最小为0, 最大为数组成员数减1
     * @param hi 上界, 最小为0, 最大为数组成员数减1
     * @param orderBy {@link Order#ASC}代表升序, {@link Order#DESC}代表降序
     * @param <T> 所有实现了 {@link Comparable} 接口的类
     * @since 2026.4.7
     * @see ArraySortAlgorithmsUtility#selection(Comparable[])
     * @see ArraySortAlgorithmsUtility#selection(Comparable[], Order)
     *
     * @see ArraySortAlgorithmsUtility#selection(Object[], Comparator)
     * @see ArraySortAlgorithmsUtility#selection(Object[], int, int, Order, Comparator)
     * @see ArraySortAlgorithmsUtility#selection(Object[], Order, Comparator)
     */
    public static <T extends Comparable<? super T>> void selection(T[] target, int lo, int hi, Order orderBy) {
        checkRange(lo, hi, target.length);
        // 第一趟用于判断需要循环的规模
        for (int i = lo; i < hi; i++) {
            // 默认使用第一个元素作为最小(最大)
            int min = i;
            // 第二趟，从第一趟的后一个元素开始遍历，找到一个最小(最大)的元素索引
            for (int j = i + 1; j <= hi; j++) {
                // 如果j元素比min的小(大)，重新更新下标
                if (ordered(target[j], target[min], orderBy)) {
                    min = j;
                }
            }
            // 内层循环结束，得到一个最小(最大的下标), 确定最开始位置应该放什么数字, 进行交换
            if (i != min) {
                // 最小已经改变, 此时我们再进行交换
                exchange(target, i, min);
            }
            // 一轮结束
        }
    }

    /**
     * 选择排序
     *
     * <p>此方法默认将整个数组排序, 即排序范围为 {@code [0, array.length - 1]}
     *
     * @param target 待排序的目标, 数组原始类型必须是实现了 {@link Comparable} 接口
     * @param orderBy {@link Order#ASC}代表升序, {@link Order#DESC}代表降序
     * @param <T> 所有实现了 {@link Comparable} 接口的类
     * @since 2026.4.7
     * @see ArraySortAlgorithmsUtility#selection(Comparable[])
     * @see ArraySortAlgorithmsUtility#selection(Comparable[], int, int, Order)
     *
     * @see ArraySortAlgorithmsUtility#selection(Object[], Comparator)
     * @see ArraySortAlgorithmsUtility#selection(Object[], int, int, Order, Comparator)
     * @see ArraySortAlgorithmsUtility#selection(Object[], Order, Comparator)
     */
    public static <T extends Comparable<? super T>> void selection(T[] target, Order orderBy) {
        selection(target, 0, target.length - 1, orderBy);
    }

    /**
     * 选择排序
     *
     * <p>此方法默认将整个数组按照升序顺序排序, 即排序范围为 {@code [0, array.length - 1]}, 顺序使用 {@link Order#ASC}
     *
     * @param target 待排序的目标, 数组原始类型必须是实现了 {@link Comparable} 接口
     * @param <T> 所有实现了 {@link Comparable} 接口的类
     * @since 2026.4.7
     * @see ArraySortAlgorithmsUtility#selection(Comparable[], Order)
     * @see ArraySortAlgorithmsUtility#selection(Comparable[], int, int, Order)
     *
     * @see ArraySortAlgorithmsUtility#selection(Object[], Comparator)
     * @see ArraySortAlgorithmsUtility#selection(Object[], int, int, Order, Comparator)
     * @see ArraySortAlgorithmsUtility#selection(Object[], Order, Comparator)
     */
    public static <T extends Comparable<? super T>> void selection(T[] target) {
        selection(target, 0, target.length - 1, Order.ASC);
    }

    /**
     * 选择排序
     *
     * <p>此方法提供给那些没有实现了{@link Comparable} 接口的类, 因此需要提供一个 {@link Comparator} 比较器来进行比较</p>
     *
     * @param target 待排序的目标, 数组原始类型不限制
     * @param lo 下界, 最小为0, 最大为数组成员数减1
     * @param hi 上界, 最小为0, 最大为数组成员数减1
     * @param orderBy {@link Order#ASC}代表升序, {@link Order#DESC}代表降序
     * @param comparator 比较器对象
     * @param <T> 任何类型
     * @since 2026.4.7
     * @see ArraySortAlgorithmsUtility#selection(Object[], Comparator)
     * @see ArraySortAlgorithmsUtility#selection(Object[], Order, Comparator)
     *
     * @see ArraySortAlgorithmsUtility#selection(Comparable[])
     * @see ArraySortAlgorithmsUtility#selection(Comparable[], Order)
     * @see ArraySortAlgorithmsUtility#selection(Comparable[], int, int, Order)
     */
    public static <T> void selection(T[] target, int lo, int hi, Order orderBy, Comparator<T> comparator){
        checkRange(lo, hi, target.length);
        // 第一趟用于判断需要循环的规模
        for (int i = lo; i < hi; i++){
            // 默认使用第一个元素作为最小(最大)
            int min = i;
            // 第二趟，从第一趟的后一个元素开始遍历，找到一个最小(最大)的元素索引
            for (int j = i + 1; j <= hi; j++){
                // 如果j元素比min的小(大)，重新更新下标
                if (ordered(target[j], target[min], orderBy, comparator)) {
                    min = j;
                }
            }
            // 内层循环结束，得到一个最小(最大的下标), 确定最开始位置应该放什么数字, 进行交换
            if (i != min){
                // 最小已经改变, 此时我们再进行交换
                exchange(target, i, min);
            }
            // 一轮结束
        }
    }

    /**
     * 选择排序
     *
     * <p>此方法提供给那些没有实现了{@link Comparable} 接口的类, 因此需要提供一个 {@link Comparator} 比较器来进行比较</p>
     *
     * <p>此方法默认将整个数组排序, 即排序范围为 {@code [0, array.length - 1]}
     *
     * @param target 待排序的目标, 数组原始类型不限制
     * @param orderBy {@link Order#ASC}代表升序, {@link Order#DESC}代表降序
     * @param comparator 比较器对象
     * @param <T> 任何类型
     * @since 2026.4.7
     * @see ArraySortAlgorithmsUtility#selection(Object[], Comparator)
     * @see ArraySortAlgorithmsUtility#selection(Object[], int, int, Order, Comparator)
     *
     * @see ArraySortAlgorithmsUtility#selection(Comparable[])
     * @see ArraySortAlgorithmsUtility#selection(Comparable[], Order)
     * @see ArraySortAlgorithmsUtility#selection(Comparable[], int, int, Order)
     */
    public static <T> void selection(T[] target, Order orderBy, Comparator<T> comparator) {
        selection(target, 0, target.length - 1, orderBy, comparator);
    }

    /**
     * 选择排序
     *
     * <p>此方法提供给那些没有实现了{@link Comparable} 接口的类, 因此需要提供一个 {@link Comparator} 比较器来进行比较</p>
     *
     * <p>此方法默认将整个数组按照升序顺序排序, 即排序范围为 {@code [0, array.length - 1]}, 顺序使用 {@link Order#ASC}
     *
     * @param target 待排序的目标, 数组原始类型不限制
     * @param comparator 比较器对象
     * @param <T> 任何类型
     * @since 2026.4.7
     * @see ArraySortAlgorithmsUtility#selection(Object[], Order, Comparator)
     * @see ArraySortAlgorithmsUtility#selection(Object[], int, int, Order, Comparator)
     *
     * @see ArraySortAlgorithmsUtility#selection(Comparable[])
     * @see ArraySortAlgorithmsUtility#selection(Comparable[], Order)
     * @see ArraySortAlgorithmsUtility#selection(Comparable[], int, int, Order)
     */
    public static <T> void selection(T[] target, Comparator<T> comparator) {
        selection(target, 0, target.length - 1, Order.ASC, comparator);
    }
    // ========================= 选择排序 =========================

    // 希尔排序
    public static <T extends Comparable<? super T>> void shell(T[] target, int lo, int hi, Order orderBy){

    }
    public static <T extends Comparable<? super T>> void shell(){

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
     * @param <T> 所有实现了 {@link Comparable} 接口的类
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
