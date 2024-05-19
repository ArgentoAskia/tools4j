package cn.argento.askia.annotations;

import cn.argento.askia.supports.Converter;

import java.lang.annotation.*;
import java.util.function.Function;

/**
 * spring aliasFor impl
 * 支持不同类型之间的aliasfor（使用converter进行转换）
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(AliasFor.AliasForContainer.class)
public @interface AliasFor {
    Class<?> aliasForClass() default Self.class;

    String value();


    /**
     * 当类型不同的时候检查是否有转换器，使用该转换器来转换！
     * @return
     */
    Class<? extends Converter> converterClass() default Converter.class;

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface AliasForContainer {
        AliasFor[] value();
    }

    @interface Self{

    }
}
