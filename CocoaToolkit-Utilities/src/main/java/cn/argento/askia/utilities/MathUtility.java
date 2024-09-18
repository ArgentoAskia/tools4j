package cn.argento.askia.utilities;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class MathUtility {


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
     * @param a
     * @param b
     * @return
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

    public static double fastPow(double a, double b){
        // 因为精度问题，不能直接对double进行操作
        // 0 == 0.00000000000001
        // 所以借助 fastPow(BigDecimal a, BigDecimal b, RoundingMode mode)实现
        return fastPow(BigDecimal.valueOf(a), BigDecimal.valueOf(b)).doubleValue();
    }

    // TODO: 2024/9/18 fix fastPow(BigDecimal a, BigDecimal b)
    /**
     * 快速幂实现，递归版本.
     *
     *
     * ref: https://zhuanlan.zhihu.com/p/95902286
     * @param a
     * @param b
     * @return
     */
    public static BigDecimal fastPow(BigDecimal a, BigDecimal b){
        if (b.compareTo(BigDecimal.ZERO) == 0){
            return BigDecimal.valueOf(1);
        }
        //  0.1 0.001...
        if (b.compareTo(BigDecimal.ZERO) > 0 && b.compareTo(BigDecimal.ONE) < 0){
            // 计算a的0.1 -0.9次方
            return BigDecimal.valueOf(1);
        }
        final BigDecimal[] bigDecimals = b.divideAndRemainder(BigDecimal.valueOf(2));
        // 偶数情况，a/2 再平方
        if (bigDecimals[1].equals(BigDecimal.ZERO)){
            // 偶数，记录a/2，直接(a/2)平方即可！
            BigDecimal divideHalf = fastPow(a, bigDecimals[0]);
            return divideHalf.multiply(divideHalf);
        }
        else {
            // 奇数情况变偶数继续乘，ans = ans^n-1 * ans
            // 0.01 0.1的情况
            return fastPow(a, b.subtract(bigDecimals[1])).multiply(fastPow(a, bigDecimals[1]));
        }
    }
}
