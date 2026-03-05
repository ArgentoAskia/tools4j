package cn.argento.askia.supports;

import cn.argento.askia.annotations.Utility;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * {@docRoot }
 * @param <T1>
 * @param <T2>
 */
public interface Converter<T1, T2> {

    default <T3> Converter<T1, T3> andThen(Converter<? super T2, ? extends T3> after){
        Objects.requireNonNull(after);
        return type -> after.convert(convert(type));
    }

    T2 convert(T1 type);

}
