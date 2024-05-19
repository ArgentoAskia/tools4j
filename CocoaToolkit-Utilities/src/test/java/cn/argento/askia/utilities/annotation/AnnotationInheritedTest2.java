package cn.argento.askia.utilities.annotation;

public class AnnotationInheritedTest2 extends AnnotationInheritedTest implements InheritedTestInterface1, InheritedTestInterface2{
    @Override
    public void func1() {

    }

    @Override
    public void func2() {

    }

    @Override
    @Api
    public String toString() {
        return super.toString();
    }
}
