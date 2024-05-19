package cn.argento.askia.annotations;

import java.lang.annotation.*;

/**
 * 注解处理器发现注解
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.TYPE, ElementType.FIELD})
public @interface AnnotationProcessor {

    Class<? extends cn.argento.askia.processors.AnnotationProcessor> value()
            default cn.argento.askia.processors.AnnotationProcessor.class;

    Class<? extends Annotation> annotationClass() default Annotation.class;
}
