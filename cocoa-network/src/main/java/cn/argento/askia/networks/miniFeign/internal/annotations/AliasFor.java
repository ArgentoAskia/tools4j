package cn.argento.askia.networks.miniFeign.internal.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface AliasFor {

    String value();

    Class<? extends Annotation> aliasAnnotation() default Annotation.class;
}
