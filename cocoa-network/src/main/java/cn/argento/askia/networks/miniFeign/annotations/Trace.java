package cn.argento.askia.networks.miniFeign.annotations;


import cn.argento.askia.networks.miniFeign.internal.annotations.AliasFor;
import cn.argento.askia.networks.miniFeign.supports.HttpRequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@HttpCaller(method = HttpRequestMethod.TRACE)
public @interface Trace {

    @AliasFor(value = "value", aliasAnnotation = HttpCaller.class)
    String value() default "";
}
