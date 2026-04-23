package cn.argento.askia.functions;


import java.math.BigDecimal;

@FunctionalInterface
public interface CombinedAdder<T, C> extends Adder<T, C, T>{
}
