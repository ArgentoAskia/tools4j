package cn.argento.askia.annotations;

import java.io.File;
import java.lang.annotation.*;

/**
 * 加载配置文件到JavaBean
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Properties {

    // 默认取当前类名和作为文件
    @AliasFor("value")
    String locations() default "{{inherit}}";

    @AliasFor("locations")
    String value() default "{{inherit}}";

    FileType fileType() default FileType.PROPERTIES;

    enum FileType{
        JSON, PROPERTIES, XML, YML, INI
    }

    @interface PropertiesMapping{
        String key() default "";
    }
}
