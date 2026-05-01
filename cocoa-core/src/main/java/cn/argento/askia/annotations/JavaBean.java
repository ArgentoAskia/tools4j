package cn.argento.askia.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
public @interface JavaBean {

    // 多播事件
    String[] events() default {};

    // 单播事件
    String[] singleEvents() default {};
}
