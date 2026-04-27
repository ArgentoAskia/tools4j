package cn.argento.askia.annotations.phase;

import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(AnnotationProcessingPhase.AnnotationProcessingPhaseContainer.class)
public @interface AnnotationProcessingPhase {

    /**
     * 绑定哪个注解处理器
     * @return
     */
    String bind() default "";

    /**
     * 执行顺序（数值越小优先级越高，越早执行）
     * 默认 0，可支持负值
     */
    int order() default 0;


    /**
     * 此处理器关心的注解类型（必须至少指定一个），不提供则使用顶层的@AnnotationProcessor的值
     */
    Class<? extends Annotation>[] value() default {};

    /**
     * 指定是否完全覆盖顶层的@AnnotationProcessor的值.
     * @return 默认覆盖
     */
    boolean override() default true;


    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface AnnotationProcessingPhaseContainer{
        AnnotationProcessingPhase[] value();
    }
}
