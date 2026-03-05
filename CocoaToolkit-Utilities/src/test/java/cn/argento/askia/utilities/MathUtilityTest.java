package cn.argento.askia.utilities;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

public class MathUtilityTest {
    public static void main(String[] args) {
    }

    @Test
    public void testFastPow(){
        final long l = MathUtility.fastPow(2, 3);
        System.out.println(Math.pow(2, 3));
        System.out.println(l);

//        final double v = MathUtility.fastPow(2.3, 3.6);
//        System.out.println(Math.pow(2.3, 3.6));
//        System.out.println(v);
//
//        final BigDecimal bigDecimal = MathUtility.fastPow(BigDecimal.valueOf(3), BigDecimal.valueOf(3.23));
//        System.out.println(Math.pow(3, 3.23));
//        System.out.println(bigDecimal);

        final BigInteger bigInteger = MathUtility.fastPow(BigInteger.valueOf(33), BigInteger.valueOf(3));
        System.out.println(Math.pow(33, 3));
        System.out.println(bigInteger);
    }
}
