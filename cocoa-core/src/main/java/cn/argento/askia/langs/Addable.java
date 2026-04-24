package cn.argento.askia.langs;


import java.security.InvalidParameterException;
import java.util.Objects;

/**
 * 可加法接口.
 *
 * <p>实现此方法的类明确具备可累加特性, 即可以进行加法操作.</p>

 * <h3>可变对象加法 VS 不可变对象加法</h3>
 *
 * <p>通常, 我们建议实现此接口的类都应该是不可变对象, 这是因为在 {@code JDK} 中几乎所有可计算的值类都是不可变的, 如果你希望为一个非值类提供加法操作的时候, 请自行维护对象的字段安全问题, 避免因为加法操作导致对象字段污染, 我们建议非值类在进行加法的时候, 要明确最终的修改方是对象本身还是 {@code other}, 实现者应该在文档中明确标明以避免出现混肴.</p>
 *
 * @param <T> 任何值类或者非值类
 * @author Admin
 */
@FunctionalInterface
public interface Addable<T> {

    T add(T other);

    /**
     * 自增函数, 返回 {@code addable + addable}.
     *
     * @param addable {@link Addable}对象
     * @param <A> 任何继承了 {@link Addable} 的类型
     * @return 如果参数{@code addable}为 {@code null} 则返回 {@code null}, 否则返回自增 {@code addable + addable} 的结果
     * @since 2026.4.23
     * @author Askia
     */
    static <A extends Addable<A>> A identitySum(A addable){
        if (addable == null){
            return null;
        }
        return addable.add(addable);
    }

    /**
     * 加法函数, 返回 {@code add1 + add2}.
     *
     * @param add1 加数1, 不能为 {@code null}
     * @param add2 加数2, 不能为 {@code null}
     * @param <A> 任何继承了 {@link Addable} 的类型
     * @return 如果加数1和加数2都不为 {@code  null}, 则返回 {@code add1 + add2} 结果, 否则将抛出异常.
     * @throws NullPointerException 如果 {@code add1} 和 {@code add2} 有一个为 {@code null} 则抛出此异常
     * @since 2026.4.24
     */
    static <A extends Addable<A>> A sum(A add1, A add2){
        Objects.requireNonNull(add1, "第一个加数不能为 null");
        Objects.requireNonNull(add2, "第二个加数不能为 null");
        return add1.add(add2);
    }

    /**
     * 求和函数. 返回 {@code u + add1 + add2 + add3 + ...}
     *
     * @param u 初始值, 不能为 {@code null}
     * @param adds 相加的值, 提供 {@code null} 时将返回 {@code u}
     * @param <A> 任何继承了 {@link Addable} 的类型
     * @return 返回求和累加的结果
     * @since 2026.4.24
     */
    @SafeVarargs
    static <A extends Addable<A>> A sum(A u, A... adds){
        if (u == null && adds == null){
            throw new InvalidParameterException("两个参数同时为null, 无法进行求和");
        }
        if (u == null){
            throw new InvalidParameterException("起始值不能为null");
        }
        if (adds == null){
            return u;
        }
        A sum = u;
        for (A a : adds){
            sum = sum.add(a);
        }
        return sum;
    }

}
