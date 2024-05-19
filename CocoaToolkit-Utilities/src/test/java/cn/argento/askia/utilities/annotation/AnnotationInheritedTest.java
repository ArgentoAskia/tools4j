package cn.argento.askia.utilities.annotation;

public class AnnotationInheritedTest{

    @Override
    @Report
    @Api
    public String toString() {
        return super.toString();
    }

    @Override
    @Api
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
