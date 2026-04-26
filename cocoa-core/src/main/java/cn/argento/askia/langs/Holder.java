package cn.argento.askia.langs;

import java.util.Objects;

/**
 * 用于模拟"引用传递"语义的泛型容器类
 * <p>
 * 在 {@code Java} 中，方法参数是按值传递的，对于对象类型，虽然可以修改对象的内部状态，但无法将参数变量本身重新赋值从而影响调用方的实参。
 * {@code Holder} 类通过包装一个可变字段 {@link #value}，使得方法内部对该字段的修改能够直接被调用方所见，从而模拟 {@code C#} 中的 {@code ref} 或 {@code C++} 中的引用参数。
 * <p>
 * 该容器通常与部分引用标记类型的注解（如 {@code @RefParam}）配合使用，明确标识某个参数应被当作"输出参数"或"输入输出参数"。
 * <p>
 * <b>注意：</b>该类不是线程安全的。如需在线程间共享，请使用 {@link java.util.concurrent.atomic.AtomicReference}。
 * @param <T> 所持有的值类型
 * @see #value
 */
public class Holder<T> {

    public T value;

    private Holder(T value){
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    /**
     * 创建Holder包装器
     *
     * @param value 要包装的值, 不可接受 {@code null}
     * @param <T> 包装的值类型
     * @return 返回 {@link Holder} 包装类型
     */
    public static <T> Holder<T> of(T value) {
        Objects.requireNonNull(value, "不能为null值创建Holder对象");
        return new Holder<>(value);
    }

    /**
     * 创建Holder包装器
     *
     * @param value 要包装的值, 可接受 {@code null}
     * @param <T> 包装的值类型
     * @return 返回 {@link Holder} 包装类型
     */
    public static <T> Holder<T> ofNullable(T value){
        return new Holder<>(value);
    }
}
