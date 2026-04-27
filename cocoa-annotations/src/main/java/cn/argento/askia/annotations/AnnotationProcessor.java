package cn.argento.askia.annotations;

import java.lang.annotation.*;

/**
 * 注解处理器发现注解
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
public @interface AnnotationProcessor {

    /**
     *
     * @return
     * @see #resolves()
     * @see #binds()
     */
    Class<?>[] value() default {};

    /**
     * 此处理器关心的注解类型（必须至少指定一个），指定此处的值，则证明该注解处理器的所有阶段都能处理该注解
     *
     * @return 注解
     */
    Class<? extends Annotation>[] resolves() default {};

    /**
     * 绑定哪个注解处理器, 仅当该注解标记在注解上时有效
     * @return 注解处理器
     */
    Class<?>[] binds() default {};


    /**
     * 注解处理器级的执行顺序（数值越小优先级越高，越早执行）安排
     * 默认 0，可支持负值
     */
    int order() default 0;

    /**
     * 处理器名称（若不指定，默认使用类的简单名称）
     */
    String name() default "";
}
