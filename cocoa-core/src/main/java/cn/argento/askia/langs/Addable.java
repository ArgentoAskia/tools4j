package cn.argento.askia.langs;


/**
 * 可加法接口.
 *
 * <p>实现此方法的类明确具备可累加特性, 既可以进行加法操作.</p>
 *
 *
 * <p>可变对象加法 VS 不可变对象加法</p>
 *
 * @param <T>
 */
@FunctionalInterface
public interface Addable<T> {

    T add(T other);


    /**
     * 自增函数, 返回 {@code addable + addable}.
     *
     * @param addable
     * @param <A>
     * @return
     */
    static <A extends Addable<A>> A identitySum(A addable){
        return addable.add(addable);
    }

    /**
     * 加法函数, 返回 {@code add1 + add2}.
     *
     * @param add1
     * @param add2
     * @param <A>
     * @return
     */
    static <A extends Addable<A>> A sum(A add1, A add2){
        return add1.add(add2);
    }

    /**
     * 求和函数.
     * @param u
     * @param adds
     * @param <A>
     * @return
     */
    static <A extends Addable<A>> A sum(A u, A... adds){
        for (A a : adds){
            u = u.add(a);
        }
        return u;
    }

}
