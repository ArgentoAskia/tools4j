package cn.argento.askia.utilities.annotation;

import cn.argento.askia.utilities.AnnotationUtility;

import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;

public interface B extends A{
    static void main(String[] args) {
        Report report = new Report(){

            @Override
            public String value() {
                return null;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }
        };
        System.out.println(report.value());
        final boolean declaredAnnotationPresent = AnnotationUtility.isDeclaredAnnotationPresent(Report.class, Inherited.class);
        System.out.println(declaredAnnotationPresent);
    }
}


@Report
interface A{

}