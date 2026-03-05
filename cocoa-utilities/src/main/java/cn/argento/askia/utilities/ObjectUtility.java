package cn.argento.askia.utilities;

import java.util.Objects;

public class ObjectUtility {

    // Java
    public static boolean less(Object o1, Object o2){
        Objects.requireNonNull(o1);
        Objects.requireNonNull(o2);
        Class<?> o1Class = o1.getClass();
        Class<?> o2Class = o2.getClass();
        if (o1Class.isPrimitive() && o2Class.isPrimitive()){
            AssertionUtility.requireComparableVariable(o1);

        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(Integer.toBinaryString(Float.floatToRawIntBits(5.3f)));
    }

}
