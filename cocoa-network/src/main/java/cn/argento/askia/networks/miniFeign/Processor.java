package cn.argento.askia.networks.miniFeign;

import cn.argento.askia.networks.miniFeign.annotations.Delete;
import cn.argento.askia.networks.miniFeign.annotations.Post;
import cn.argento.askia.utilities.AnnotationUtility;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;

/**
 * 处理器接口.
 */
public interface Processor {

    Method getProxyMethod();

    public static Processor getInstance() {
        return null;
    }

    @Delete
    @Post
    default @Type Comparator<?> get() throws @Type IOException {
        return null;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE_USE})
    @interface Type{
    }
    static void main(String[] args) throws NoSuchMethodException {
        System.out.println(Arrays.toString(AnnotationUtility.getAnnotations(Processor.class.getMethod("get"))));
        System.out.println(Arrays.toString(Processor.class.getMethod("get").getAnnotatedReturnType().getAnnotations()));
        System.out.println(Arrays.toString(new java.lang.reflect.Type[]{Processor.class.getMethod("get").getAnnotatedExceptionTypes()[0].getType()}));
        System.out.println(Processor.class.getMethod("get").getReturnType());
        System.out.println(Processor.class.getMethod("get").getGenericReturnType());
    }
}
