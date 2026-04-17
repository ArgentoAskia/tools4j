package cn.argento.askia.utilities.calc;

import cn.argento.askia.annotations.Utility;
import cn.argento.askia.utilities.generate.RandomUtility;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.LongStream;

/**
 * 数学计算工具类.
 *
 *  <p>此工具类为{@link Math}补充了一些额外的计算方法，这些方法常用于通用计算、科学、游戏开发、金融、精确计算、算法竞赛。</p>
 *  <p>原版{@link Math}为我们提供了下面的计算能力：</p>
 *  <ul>
 *      <li>三角函数(sin、cos、tan、tan2)、反三角函数计算(acos、atan、atan2、asin)、双曲三角函数(cosh、sinh、tanh)</li>
 *      <li>幂运算(pow、exp[e^a]、expm1[e^a - 1])、对数计算(log、log10、log1p)、平方根(sqrt)、立方根(cbrt)、勾股定理计算(hypot)、</li>
 *      <li>四舍五入(round)、接近数(rint)、上下取整(ceil[上取整]、floor[下取整])、取绝对值(abs)、取最大值(max)、取最小值(min)、取相反数(copySign)、取正负号(signum)</li>
 *      <li>类型范围安全的加减乘除(addExact、subtractExact、multiplyExact、decrementExact、incrementExact、negateExact)</li>
 *      <li>取整除法和余数：floorDiv、floorMod</li>
 *      <li>getExponent() 方法用于获取浮点数的 无偏指数，即 Java 内部表示形式 m·2^x 中的 x 值。它支持 double 和 float 类型。</li>
 *      <li>IEEEremainder()方法对指定的参数执行除法运算，并根据IEEE 754标准返回余数。</li>
 *      <li>scalab() 方法返回浮点数 x 和整数 y 的公式 x·2y 的结果。</li>
 *      <li>浮点截断：返回比 a 大的最小浮点数（正无穷方向）(nextUp)、返回比 a 小的最大浮点数（负无穷方向）(nextDown)、返回从 start 向 direction 方向的下一个浮点数(nextAfter)</li>
 *      <li>toIntExact(long): 将 long 值转为 int，超出范围抛异常</li>
 *      <li>toRadians(angdeg): 将角度制转为弧度制</li>
 *      <li>toDegrees(angrad): 将弧度制转为角度制</li>
 *      <li>ulp(): 返回参数“最后一位的单位”</li>
 *  </ul>
 */
@Utility("数学计算工具类")
public class MathUtility {

    private MathUtility() {
        throw new IllegalAccessError("MathUtility为工具类, 无法创建该类的对象");
    }


    /**
     * 快速幂
     * @param a int
     * @param b int
     * @return long
     */
    public static long fastPow(int a, int b){
        long ans = 1;
        while (b > 0){
            if ((b & 1) == 1){
                ans *= a;
            }
            a *= a;
            b >>>= 1;
        }
        return ans;
    }

    /**
     * 快速幂实现,非递归版本.
     *
     *
     * ref: https://zhuanlan.zhihu.com/p/95902286
     * @param a BigInteger
     * @param b BigInteger
     * @return BigInteger结果
     */
    public static BigInteger fastPow(BigInteger a, BigInteger b){
        BigInteger ans = BigInteger.valueOf(1);
        // 一直右移位，直到位的数等于0
        while (b.compareTo(BigInteger.ZERO) > 0){
            // 取b中的一位，如果是1，则相乘a
            final BigInteger and = b.and(BigInteger.valueOf(1));
            if (and.equals(BigInteger.ONE)){
                ans = ans.multiply(a);
            }
            // 让a一直自乘
            a = a.multiply(a);
            // 右移一位
            b = b.shiftRight(1);
        }
        return ans;
    }

    /**
     * 限制数值范围.
     * 将 value 限制在 [min, max] 之间。
     *
     * @param value int值
     * @param min 最小值
     * @param max 最大值
     * @return 如果value处于[min, max] 之间则返回value, 如果小于min则返回min, 如果大于max则返回max
     * @since 2026.4.16
     */
    public static int clamp(int value, int min, int max){
        return value < min ? min : (Math.min(value, max));
    }

    /**
     * 限制数值范围.
     * 将 value 限制在 [min, max] 之间。
     * @param value long值
     * @param min 最小值
     * @param max 最大值
     * @return 如果value处于[min, max] 之间则返回value, 如果小于min则返回min, 如果大于max则返回max
     * @since 2026.4.16
     */
    public static long clamp(long value, long min, long max){
        return value < min ? min : (Math.min(value, max));
    }

    /**
     * 限制数值范围.
     * 将 value 限制在 [min, max] 之间。
     * @param value double值
     * @param min 最小值
     * @param max 最大值
     * @return 如果value处于[min, max] 之间则返回value, 如果小于min则返回min, 如果大于max则返回max
     * @since 2026.4.16
     */
    public static double clamp(double value, double min, double max){
        return value < min ? min : (Math.min(value, max));
    }

    /**
     * 限制数值范围.
     * 将 value 限制在 [min, max] 之间。
     * @param value Comparable<T>
     * @param min 最小值
     * @param max 最大值
     * @param <T> 任何实现了Comparable的类型
     * @return 如果value处于[min, max] 之间则返回value, 如果小于min则返回min, 如果大于max则返回max
     * @since 2026.4.16
     */
    public static <T extends Comparable<T>> T clamp(T value, T min, T max){
        return value.compareTo(min) < 0? min: (value.compareTo(max) > 0? max : value);
    }

    /**
     * 限制数值范围.
     * 将 value 限制在 [min, max] 之间。
     * @param value value Comparable<T>
     * @param min 最小值
     * @param max 最大值
     * @param comparator 比较器
     * @param <T> 任何类型
     * @return 如果value处于[min, max] 之间则返回value, 如果小于min则返回min, 如果大于max则返回max
     * @since 2026.4.16
     */
    public static <T> T clamp(T value, T min, T max, Comparator<T> comparator){
        assert comparator != null;
        return comparator.compare(value, min) < 0? min : (comparator.compare(value, max) > 0? max:value);
    }

    /**
     * 检查 value 与 Math.round(value) 的差值是否小于 epsilon。
     *
     * @return 如果小于则返回{@code true}, 否则返回{@code false}
     * @since 2026.4.16
     */
    public static boolean isNearInteger(double value, double epsilon){
        double round = Math.round(value);
        final double max = Math.max(round, value);
        final double min = Math.min(round, value);
        final double sum = Double.sum(min, epsilon);
        return Double.compare(sum, max) > 0;
    }

    /**
     * 将数值四舍五入到指定有效数字位数。
     *
     * @param value   原始数值
     * @param sigFigs 有效数字位数（必须 ≥ 1）
     * @return 保留 sigFigs 位有效数字后的值
     * @throws IllegalArgumentException 如果 sigFigs < 1
     * @since 2026.4.16
     */
    public static double roundToSignificantFigures(double value, int sigFigs) {
        if (sigFigs < 1) {
            throw new IllegalArgumentException("有效数字位数必须 ≥ 1");
        }
        if (Double.isNaN(value) || Double.isInfinite(value) || value == 0.0) {
            return value; // 无法处理或直接返回
        }

        final double sign = Math.signum(value);
        final double absValue = Math.abs(value);

        // 数量级: floor(log10(absValue))
        final double magnitude = Math.floor(Math.log10(absValue));
        // 缩放因子: 10^(sigFigs - 1 - magnitude)
        final double scale = Math.pow(10, sigFigs - 1 - magnitude);
        // 缩放后四舍五入，再缩放回去
        final double rounded = Math.round(absValue * scale) / scale;

        return sign * rounded;
    }

    /**
     * 将 BigDecimal 数值四舍五入到指定有效数字位数，并返回 double 结果。
     *
     * @param value   原始数值（不能为 null）
     * @param sigFigs 有效数字位数（必须 ≥ 1）
     * @return 保留 sigFigs 位有效数字后的 double 值
     * @throws IllegalArgumentException 如果 sigFigs < 1 或 value 为 null
     */
    public static BigDecimal roundToSignificantFigures(BigDecimal value, int sigFigs) {
        if (value == null) {
            throw new IllegalArgumentException("value cannot be null");
        }
        if (sigFigs < 1) {
            throw new IllegalArgumentException("sigFigs must be >= 1");
        }
        // 使用 MathContext 进行有效数字舍入
        MathContext mc = new MathContext(sigFigs, RoundingMode.HALF_UP);
        return value.round(mc);
    }

    // 几何与向量计算
    /**
     * 计算平面两点的直线距离
     * @param x1 x1
     * @param y1 y1
     * @param x2 x2
     * @param y2 y2
     * @return 两点的直线距离
     */
    public static double distance(double x1, double y1, double x2, double y2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        return Math.hypot(dx, dy);
    }

    /**
     * 计算三维空间两点距离
     * @param x1 x1
     * @param y1 y1
     * @param z1 z1
     * @param x2 x2
     * @param y2 y2
     * @param z2 z2
     * @return 两点的直线距离
     */
    public static double distance3D(double x1, double y1, double z1,
                                    double x2, double y2, double z2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        double dz = z1 - z2;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    private static double distanceSquared(double x1, double y1, double x2, double y2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        return dx * dx + dy * dy;
    }

    /**
     * 比较平面中两个线段的大小
     * @param x1 线段1坐标1的x
     * @param y1 线段1坐标1的y
     * @param x2 线段1坐标2的x
     * @param y2 线段1坐标2的y
     * @param x3 线段2坐标1的x
     * @param y3 线段2坐标1的y
     * @param x4 线段2坐标2的x
     * @param y4 线段2坐标2的y
     * @return 如果线段1的长度小于线段2则返回{@code -1}, 如果线段1的长度等于线段2则返回{@code 0}, 否则返回{@code 1}
     * @since 2026.4.16
     */
    public static int distanceMin(double x1, double y1, double x2, double y2,
                                  double x3, double y3, double x4, double y4){
        final double v = distanceSquared(x1, y1, x2, y2);
        final double v1 = distanceSquared(x3, y3, x4, y4);
        return Double.compare(v, v1);
    }


    private static double distance3DSquared(double x1, double y1, double z1,
                                            double x2, double y2, double z2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        double dz = z1 - z2;
        return dx * dx + dy * dy + dz * dz;
    }

    /**
     * 比较三维空间中两个线段的大小
     * @param x1 线段1坐标1的x
     * @param y1 线段1坐标1的y
     * @param z1 线段1坐标1的z
     * @param x2 线段1坐标2的x
     * @param y2 线段1坐标2的y
     * @param z2 线段1坐标2的z
     * @param x3 线段2坐标1的x
     * @param y3 线段2坐标1的y
     * @param z3 线段2坐标1的z
     * @param x4 线段2坐标2的x
     * @param y4 线段2坐标2的y
     * @param z4 线段2坐标2的z
     * @return 如果线段1的长度小于线段2则返回{@code -1}, 如果线段1的长度等于线段2则返回{@code 0}, 否则返回{@code 1}
     * @since 2026.4.16
     */
    public static int distance3DCompare(double x1, double y1, double z1,
                                        double x2, double y2, double z2,
                                        double x3, double y3, double z3,
                                        double x4, double y4, double z4){
        final double v = distance3DSquared(x1, y1, z1, x2, y2, z2);
        final double v1 = distance3DSquared(x3, y3, z3, x4, y4, z4);
        return Double.compare(v, v1);
    }

    /**
     * 线性插值。
     *
     * @param start 起始值（t = 0 时的值）
     * @param end   结束值（t = 1 时的值）
     * @param t     插值系数，通常推荐在 [0, 1] 范围内，但超出时仍会外推
     * @return 插值结果
     * @since 2026.4.16
     */
    public static double lerp(double start, double end, double t) {
        // 使用 start + t * (end - start) 形式，数值稳定性较好
        return start + t * (end - start);
    }

    /**
     * 禁止外推的线性插值
     *
     * @param start 起始值（t = 0 时的值）
     * @param end 结束值（t = 1 时的值）
     * @param t 插值系数，通常推荐在 [0, 1] 范围内，如果超过此范围时,将不会进行外推。
     * @return 如果出现外推，则当差值系数小于0时返回起始值，大于0时返回结束值
     * @since 2026.4.16
     */
    public static double lerpClamped(double start, double end, double t) {
        if (t <= 0) return start;
        if (t >= 1) return end;
        return start + t * (end - start);
    }


    // 多值取最大最小

    /**
     * 取数值最大值.
     * <p>此方法支持多个数字的最大值判别, 是{@link Math#max(int, int)}的增强版本, 支持数组</p>
     *
     * @param numbers int类型可变参数, 可提供数组
     * @return 最大值
     * @since 2026.4.16
     */
    public static int max(int... numbers){
        if (numbers.length == 0){
            throw new IllegalArgumentException("不能提供空长度参数");
        }
        if (numbers.length == 1){
            return numbers[0];
        }
        int max = numbers[0];
        for (int traver : numbers){
            if (traver > max){
                max = traver;
            }
        }
        return max;
    }

    /**
     * 取数值最大值.
     * <p>此方法支持多个数字的最大值判别, 是{@link Math#max(long, long)}的增强版本, 支持数组</p>
     *
     * @param numbers long类型可变参数, 可提供数组
     * @return 最大值
     * @since 2026.4.17
     */
    public static long max(long... numbers){
        if (numbers.length == 0){
            throw new IllegalArgumentException("不能提供空长度参数");
        }
        if (numbers.length == 1){
            return numbers[0];
        }
        long max = numbers[0];
        for (long traver : numbers){
            if (traver > max){
                max = traver;
            }
        }
        return max;
    }

    /**
     * 取数值最大值.
     * <p>此方法支持多个数字的最大值判别, 是{@link Math#max(long, long)}的增强版本, 支持数组</p>
     *
     * @param numbers double类型可变参数, 可提供数组
     * @return 最大值
     * @since 2026.4.17
     */
    public static double max(double... numbers){
        if (numbers.length == 0){
            throw new IllegalArgumentException("不能提供空长度参数");
        }
        if (numbers.length == 1){
            return numbers[0];
        }
        double max = numbers[0];
        for (double traver : numbers){
            if (Double.compare(traver, max) > 0){
                max = traver;
            }
        }
        return max;
    }

    /**
     * 取所有数值中的最小值.
     * <p>此方法支持多个数字的最小值判别, 是{@link Math#min(int, int)}的增强版本, 支持数组</p>
     *
     * @param numbers int类型可变参数, 可提供数组
     * @return 最小值
     * @since 2026.4.17
     */
    public static int min(int... numbers){
        if (numbers.length == 0) throw new IllegalArgumentException("不能提供空参数");
        if (numbers.length == 1){
            return numbers[0];
        }
        int min = numbers[0];
        for (int traver : numbers){
            if (traver < min){
                min = traver;
            }
        }
        return min;
    }

    /**
     * 取所有数值中的最小值.
     * <p>此方法支持多个数字的最小值判别, 是{@link Math#min(long, long)}的增强版本, 支持数组</p>
     *
     * @param numbers long类型可变参数, 可提供数组
     * @return 最小值
     * @since 2026.4.17
     */
    public static long min(long... numbers){
        if (numbers.length == 0){
            throw new IllegalArgumentException("不能提供空参数");
        }
        if (numbers.length == 1){
            return numbers[0];
        }
        long min = numbers[0];
        for (long traver : numbers){
            if (traver < min){
                min = traver;
            }
        }
        return min;
    }

    /**
     * 取所有数值中的最小值.
     * <p>此方法支持多个数字的最小值判别, 是{@link Math#min(double, double)}的增强版本, 支持数组</p>
     * @param numbers double类型可变参数, 可提供数组
     * @return 最小值
     * @since 2026.4.17
     */
    public static double min(double... numbers){
        if (numbers.length == 0){
            throw new IllegalArgumentException("不能提供空参数");
        }
        if (numbers.length == 1){
            return numbers[0];
        }
        double min = numbers[0];
        for (double traver : numbers){
            if (Double.compare(traver, min) < 0){
                min = traver;
            }
        }
        return min;
    }


    // =========================== 统计函数 ===========================

    /**
     * 求和函数.
     *
     * <p>注意：此方法无法用于大数求和, 由于方法求和使用的是数组, 此方法使用{@code long}返回结果来尽最大可能保证数据不溢出, 因此调用方要保证自己的数据位于{@link Integer#MIN_VALUE} 和 {@link Integer#MAX_VALUE}之间</p>
     *
     * <p>求和函数支持并行处理, 指定参数二来决定是否使用并行流, 该方法底层使用{@link LongStream#sum()}来进行并行计算</p>
     *
     * @param numbers int类型数组
     * @param parallel 是否使用并行处理
     * @return 求和结果
     * @since 2026.4.17
     */
    public static long sum(int[] numbers, boolean parallel){
        if (numbers== null || numbers.length == 0){
            throw new IllegalArgumentException("不能提供空参数");
        }
        long sum = 0;
        if (parallel){
            // 并行计算
            sum = Arrays.stream(numbers)   // 创建普通流
                    .asLongStream()        // 转为long流
                    .parallel()            // 并行处理
                    .sum();                // 求和
        }
        else{
            for (int traver : numbers){
                sum += traver;
            }
        }
        return sum;
    }

    /**
     * 求和函数.
     * <p>求和函数支持并行处理, 指定参数二来决定是否使用并行流, 该方法底层使用{@link java.util.stream.DoubleStream#sum()}来进行并行计算</p>
     * @param numbers double类型数组
     * @param parallel 是否使用并行处理
     * @return 求和结果
     * @since 2026.4.17
     */
    public static double sum(double[] numbers, boolean parallel){
        if (numbers== null || numbers.length == 0){
            throw new IllegalArgumentException("不能提供空参数");
        }
        double sum = 0;
        if (parallel){
            sum = Arrays.stream(numbers)
                    .parallel()
                    .sum();
        }
        else{
            for (double traver : numbers){
                sum = Double.sum(sum, traver);
            }
        }
        return sum;
    }


    /**
     * 总体平均值.
     *
     * @param numbers int类型数组, 代表总体样本数值
     * @param parallel 是否使用并行处理
     * @return 总体平均值结果
     * @since 2026.4.17
     */
    public static double average(int[] numbers, boolean parallel){
        if (numbers== null || numbers.length == 0){
            throw new IllegalArgumentException("不能提供空参数");
        }
        double average = 0;
        if (parallel){
            OptionalDouble optionalAverage = Arrays.stream(numbers)
                    .asLongStream()
                    .parallel()
                    .average();
            average = optionalAverage.orElseThrow(() -> new ArithmeticException("无法计算数组"+ Arrays.toString(numbers) +"的平均值, 原因是Stream流调用average()之后得到的OptionalDouble的isPresent()返回false"));
        }
        else{
            int n = numbers.length;
            long sum = sum(numbers, false);
            average = sum * 1.0 / n;
        }
        return average;
    }

    /**
     * 样本平均值.
     *
     * @param numbers int类型数组, 代表总体样本数值
     * @param sampleCount 样本抽取个数
     * @param parallel 是否使用并行处理
     * @param sampleFunction 抽样函数, 参数1是总体样本, 参数2用于装载已选择的抽样样本, 此参数较为灵活, 实际使用时, 调用方可以决定如何记录抽样样本(按样本值, 按样本下标等), 要求返回一个样本数据
     * @return 样本平均值结果
     * @since 2026.4.17
     */
    public static double sampleAverage(int[] numbers, int sampleCount, boolean parallel, BiFunction<int[], Set<Integer>, Integer> sampleFunction){
        if (numbers== null || numbers.length == 0){
            throw new IllegalArgumentException("请提供总体样本");
        }
        if (sampleFunction == null){
            throw new IllegalArgumentException("请提供抽样函数");
        }
        if (sampleCount <= 0 || sampleCount > numbers.length){
            throw new IllegalArgumentException("抽样个数不能小于等于0或者超过样本总数");
        }
        int[] samples = new int[sampleCount];
        Set<Integer> pickSet = new HashSet<>();
        // 1.开始抽样
        for (int i = 0; i < sampleCount; i++){
            Integer apply = sampleFunction.apply(numbers, pickSet);
            samples[i] = apply;
        }
        // 2.计算样本
        return average(samples, parallel);
    }

    /**
     * 样本平均值.
     * @param numbers int类型数组, 代表总体样本数值
     * @param sampleCount 样本抽取个数
     * @param parallel 是否使用并行处理
     * @return 样本平均值结果
     * @since 2026.4.17
     */
    public static double sampleAverage(int[] numbers, int sampleCount, boolean parallel){
        BiFunction<int[], Set<Integer>, Integer> function = new BiFunction<int[], Set<Integer>, Integer>() {
            @Override
            public Integer apply(int[] numbers, Set<Integer> indexSet) {
                int randomIndex = RandomUtility.randomInt(0, numbers.length - 1);
                // 如果该样本已抽取则重新抽取
                while (indexSet.contains(randomIndex)){
                    randomIndex = RandomUtility.randomInt(0, numbers.length - 1);
                }
                indexSet.add(randomIndex);
                return numbers[randomIndex];
            }
        };
        return sampleAverage(numbers, sampleCount, parallel, function);
    }

    /**
     * 随机样本平均值.
     * <p>样本数随机, 样本来源随机的样本平均值</p>
     *
     * @param numbers int类型数组, 代表总体样本数值
     * @param parallel 是否使用并行处理
     * @return 样本平均值结果
     * @since 2026.4.17
     */
    public static double sampleAverage(int[] numbers, boolean parallel){
        int sampleCount = RandomUtility.randomInt(0, numbers.length - 1);
        return sampleAverage(numbers, sampleCount, parallel);
    }
//    public static double sum(double[] arr);
//    public static double mean(double[] arr);
//    public static double variance(double[] arr);  // 样本方差
//    public static double stdDev(double[] arr);
//    public static double average(int[] arr);
    // =========================== 统计函数 ===========================

//    /**
//     * 快速幂.
//     * @param a double
//     * @param b double
//     * @return double
//     */
//    public static double fastPow(double a, double b){
//        // 因为精度问题，不能直接对double进行操作
//        // 0 == 0.00000000000001
//        // 所以借助 fastPow(BigDecimal a, BigDecimal b, RoundingMode mode)实现
//        return fastPow(BigDecimal.valueOf(a), BigDecimal.valueOf(b)).doubleValue();
//    }
//
//    /**
//     * 快速幂实现，递归版本.
//     * ref: https://zhuanlan.zhihu.com/p/95902286
//     * @param a BigDecimal
//     * @param b BigDecimal
//     * @return
//     */
//    public static BigDecimal fastPow(BigDecimal a, BigDecimal b) {
//        if (b.compareTo(BigDecimal.ZERO) == 0) {
//            return BigDecimal.valueOf(1);
//        }
//        //  0.1 0.001...
//        if (b.compareTo(BigDecimal.ZERO) > 0 && b.compareTo(BigDecimal.ONE) < 0) {
//            // 计算a的0.1 -0.9次方
//            return BigDecimal.valueOf(1);
//        }
//        final BigDecimal[] bigDecimals = b.divideAndRemainder(BigDecimal.valueOf(2));
//        // 偶数情况，a/2 再平方
//        if (bigDecimals[1].equals(BigDecimal.ZERO)) {
//            // 偶数，记录a/2，直接(a/2)平方即可！
//            BigDecimal divideHalf = fastPow(a, bigDecimals[0]);
//            return divideHalf.multiply(divideHalf);
//        } else {
//            // 奇数情况变偶数继续乘，ans = ans^n-1 * ans
//            // 0.01 0.1的情况
//            return fastPow(a, b.subtract(bigDecimals[1])).multiply(fastPow(a, bigDecimals[1]));
//        }
//    }


    // 基本的加减乘除计算

    //  判别方法
//    public static boolean isOdd(int n);               // 是否是基数
//    public static boolean isEven(int n);              // 是否是偶数
//    public static boolean isPrime(int n);             // 是否是质数 O(√n) 试除法
//    public static boolean isPowerOfTwo(int n);        // 是否是2的幂次
//    public static boolean isBetween(int value, int low, int high); // 数值范围检查，包含边界
//    public static boolean[] sieveOfEratosthenes(int n) // 返回 [0..n] 的布尔数组
//    public static List<Integer> primeFactors(int n)    // 质因数分解

    // 数学公式
//    public static long factorial(int n);      // 阶乘（递归或迭代，注意溢出）
    // 排列组合
//    public static long combination(int n, int k);     // C(n,k)
//    public static long permutation(int n, int k);     // A(n, k)

    // 公约数(递归版本)
    public static int gcd(int a, int b){
        a = Math.abs(a);
        b = Math.abs(b);
        return b == 0 ? a : gcd(b, a % b);
    }

    // 非递归版本公约数, 内部使用
    private static int gcd(int a, int b, Void noUse) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    // 公倍数
    /**
     * 计算两个整数的最小公倍数（LCM）。
     *
     * @param a 第一个整数（可为负数）
     * @param b 第二个整数（可为负数）
     * @return a 和 b 的最小公倍数（非负）
     * @throws ArithmeticException 如果计算结果溢出 int 范围
     */
    public static int lcm(int a, int b){
        if (a == 0 || b == 0) {
            return 0;   // 0 与任何数的 LCM 定义为 0
        }
        // 使用 long 防止中间乘法溢出
        return Math.abs(a) / gcd(a, b, null) * Math.abs(b);
    }
    // 扩展欧几里得 (求 ax + by = gcd(a,b) 的一组解)
//    public static long[] extendedGcd(long a, long b) // 返回 {gcd, x, y}

    // 百分比计算
//    public static BigDecimal percentOf(BigDecimal part, BigDecimal total, int scale)
//    public static BigDecimal addPercent(BigDecimal original, double percent)

//    矩阵快速幂
//    public static long[][] matrixMul(long[][] a, long[][] b, long mod)
//    public static long[][] matrixPow(long[][] base, long exp, long mod)

    // 任意进制转换
//    public static String parseFromBase(String s, int radix)  // 任意进制转十进制

    // 几何常用函数
//    public static long cross(long x1, long y1, long x2, long y2)  // 向量叉积，判断方向
//    public static double polygonArea(int[] x, int[] y)   // 鞋带公式

    // 分数(加减乘除)计算



    public static void main(String[] args) {
        final long v = fastPow(2, 3);
        System.out.println(v);
        BigDecimal v1 = new BigDecimal("12345");
        System.out.println(roundToSignificantFigures(v1, 3));   // 12300.0

        BigDecimal v2 = new BigDecimal("0.001234");
        System.out.println(roundToSignificantFigures(v2, 2));   // 0.0012

        BigDecimal v3 = new BigDecimal("-0.0005678");
        System.out.println(roundToSignificantFigures(v3, 2));   // -0.00057

        BigDecimal v4 = BigDecimal.ZERO;
        System.out.println(roundToSignificantFigures(v4, 3));   // 0.0

        BigDecimal v5 = new BigDecimal("999.9");
        System.out.println(roundToSignificantFigures(v5, 3));   // 1000.0

        BigDecimal v6 = new BigDecimal("0.9999");
        System.out.println(roundToSignificantFigures(v6, 2));   // 1.0
    }

}
