package cn.argento.askia.functions;

import cn.argento.askia.langs.Addable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface Adder<T, U, R> extends BiFunction<T, U, R> {

    R add(T t, U u);

    @Override
    default R apply(T t, U u) {
        return add(t, u);
    }

    default <V, R2> Adder<T, U, R2> andThenAdd(Adder<? super R, ? super V, R2> otherAdder,
                                                   Supplier<? extends V> vSupplier) {
        Objects.requireNonNull(otherAdder);
        Objects.requireNonNull(vSupplier);
        return (t, u) -> otherAdder.add(Adder.this.add(t, u), vSupplier.get());
    }
    default <V> Adder<T, U, V> identityAdd(FunctionalAdder<R, V> unaryAdder){
        Objects.requireNonNull(unaryAdder);
        return (t, u) -> {
            final R add = Adder.this.add(t, u);
            return unaryAdder.add(add, add);
        };
    }
}
