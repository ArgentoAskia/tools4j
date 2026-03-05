package cn.argento.askia.utilities;

import org.junit.Test;

import java.util.Comparator;

public class AssertionUtilityTest {

    class C{}
    class A implements Comparator{
        @Override
        public int compare(Object o1, Object o2) {
            return 0;
        }
    }

    class B extends A{}

    @Test
    public void testRequireComparableVariable(){
        B b = new B();
        C c = new C();
        AssertionUtility.requireComparableVariable(b);
        AssertionUtility.requireComparableVariable(c);
    }
}
