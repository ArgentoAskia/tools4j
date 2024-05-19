package cn.argento.askia.utilities.annotation;

import cn.argento.askia.utilities.AnnotationUtility;

import java.lang.reflect.Method;

public class AnnotationUtilityTest {
    public static void main(String[] args) {
        // 该方法可能会返回Object类的native方法！！！！！！
        final Method[] methods = AnnotationInheritedTest2.class.getMethods();
        System.out.println(methods.length);
        for (Method me :
                methods) {
            final String name = me.getName();

            final boolean apiPresent = AnnotationUtility.isAnnotationInheritedPresent(me, Api.class);
            final boolean reportPresent = AnnotationUtility.isAnnotationInheritedPresent(me, Report.class);
            System.out.println("方法名：" + name + ", 是否标记了@Api = " + apiPresent + ", 是否标记了@Report = " + reportPresent);
        }
    }
}




