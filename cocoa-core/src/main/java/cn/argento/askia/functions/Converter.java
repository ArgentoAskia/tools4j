package cn.argento.askia.functions;

import java.util.Objects;

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
