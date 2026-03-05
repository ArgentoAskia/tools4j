package cn.argento.askia.utilities;



import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.BiFunction;

/**
 * 注解工具类.
 *
 * @author Askia
 * @version 1.0.2024-05-14
 * @since 1.0.X
 */
public final class AnnotationUtility {


    // 请不要创建工具类的对象
    private AnnotationUtility(){}

    // ------------------------ AnnotatedElement‘s api and it's extension ------------------------
    /**
     * check for annotatedElement not null
     * @param annotatedElement annotatedElement
     */
    private static void checkAnnotatedElementNotNull(AnnotatedElement annotatedElement){
        if (annotatedElement == null)
            throw new NullPointerException("annotatedElement can not be NULL!!!");
    }

    /**
     * 该方法用于判断某个注解是否被标记在某个可标记元素(如：类、方法等)上.
     * <p>
     *     该方法是 {@link AnnotatedElement#isAnnotationPresent(Class)} 的静态包装
     * <hr>
     *     示例如下：
     *     <blockquote style="background-color:rgb(232,232,232)"><pre>
     *     // &#64;Running是一个RUNTIME级别注解, 且可继承
     *     &#64;Running
     *     public class A{
     *          // &#64;Api是一个RUNTIME级别注解
     *          &#64;Api
     *          public void test(){
     *              ...
     *          }
     *
     *          // &#64;Test是一个可重复注解
     *          &#64;Test
     *          &#64;Test
     *          public void test2(){}
     *
     *     }
     *     public class B extends A{}
     *
     *
     *     // true
     *     AnnotationUtility.isAnnotationPresent(B.class, Running.class);
     *     // true
     *     AnnotationUtility.isAnnotationPresent(A.class.getMethod("test"), Api.class);
     *     // false
     *     AnnotationUtility.isAnnotationPresent(A.class.getMethod("test2"), Test.class);
     *     </pre></blockquote>
     * <hr>
     *     <b>注意: 原始的</b> {@link AnnotatedElement#isAnnotationPresent(Class)} <b>有局限性：</b><br>
     *     <ol>
     *         <li>判断是否<b>直接标记了某个{@code Runtime}级别注解</b> (√)</li>
     *         <li>判断是否标记了可重复注解的<b>注解容器</b>  (√)</li>
     *         <li>判断是否标记了针对类的<b>可继承性注解</b>  (√)</li>
     *         <li>判断是否标记了<b>可重复注解</b>          (×)</li>
     *     </ol>
     * 除了前面三种情况可以判断之外，其他情况无法判别！
     * @param annotatedElement 对象组成元素，如{@link Method}、{@link Constructor}、{@link Field}等
     * @param annotationClass 被标记的注解的{@link Class}对象
     * @return 如果有标记,则返回 true, 否则返回 false
     */
    public static boolean isAnnotationPresent(AnnotatedElement annotatedElement, Class<? extends Annotation> annotationClass){
        checkAnnotatedElementNotNull(annotatedElement);
        return annotatedElement.isAnnotationPresent(annotationClass);
    }

    // 只有类和接口（继承性）和方法（Override）才能往下传递注解！

    /**
     * 判断重写方法及其所有父类的原方法上是否标记了某个注解. 兼容接口和类.
     * <p>
     * 该方法可以让父类原方法上的注解被子类Override方法继承！如下示例：
     * <blockquote style="background-color:rgb(232,232,232)"><pre>
     *     public class A{
     *         &#64;CallerSensitive
     *         public void test(){}
     *     }
     *
     *     public class B extends A{
     *         &#64;Override
     *         public void test(){}
     *     }
     *
     *     // true
     *     Method method = B.class.getMethod("test");
     *     AnnotationUtility.isAnnotationInheritedPresent(method, CallerSensitive.class);
     * </pre></blockquote>
     * <hr>
     * <b>另外该方法忽略适配器模式中，被适配的类和接口中的适配器方法.参考下面的例子：</b>
     * <blockquote style="background-color:rgb(232,232,232)"><pre>
     *     public interface C{
     *         &#64;InterfaceMethod
     *         public void test();
     *     }
     *     public class A{
     *         &#64;CallerSensitive
     *         public void test(){}
     *     }
     *     public class B extends A implements C{
     *         &#64;Override
     *         public void test(){}
     *     }
     *
     *     public class D extends A implements C{
     *
     *     }
     *
     *     // true
     *     Method method = B.class.getMethod("test");
     *     AnnotationUtility.isAnnotationInheritedPresent(method, CallerSensitive.class);
     *     AnnotationUtility.isAnnotationInheritedPresent(method, InterfaceMethod.class);
     *
     *     // false
     *     Method method = D.class.getMethod("test");
     *     AnnotationUtility.isAnnotationInheritedPresent(method, CallerSensitive.class);
     *     AnnotationUtility.isAnnotationInheritedPresent(method, InterfaceMethod.class);
     *
     * </pre></blockquote>
     *
     *
     * @param method 方法对象
     * @param annotationClass 注解
     * @return 如果该重写方法及其父类或父接口中的原方法中标记了该注解,则返回 {@code true} , 否则返回 {@code false}
     */
    public static boolean isAnnotationInheritedPresent(Method method, Class<? extends Annotation> annotationClass){
        // 如果当前有标记，则返回true
        if (isDeclaredAnnotationPresent(method, annotationClass)){
            return true;
        }
        // get which class defined this Method
        final Class<?> declaringClass = method.getDeclaringClass();
        // get class’s super class and super interfaces
        final Class<?>[] interfaces = declaringClass.getInterfaces();
        Class<?> superclass = declaringClass.getSuperclass();
        // declaringClass是Object类,an interface, a primitive type, or void， 没有父类
        if (superclass == null){
            return false;
        }
        boolean overrideSuperMethodMarked = isOverrideSuperMethodMarked(method, superclass, interfaces, annotationClass);
        if (!overrideSuperMethodMarked){
            // go on for superClass
            Class<?> superclassAgain = null;
            // 循环遍历父类链
            while ((superclassAgain = superclass.getSuperclass()) != null) {
                final Class<?>[] superclassInterfaces = superclass.getInterfaces();
                overrideSuperMethodMarked = isOverrideSuperMethodMarked(method, superclassAgain, superclassInterfaces, annotationClass);
                if (overrideSuperMethodMarked){
                    return true;
                }
                superclass = superclassAgain;
            }
            // 队列遍历子接口！
            LinkedList<Class<?>[]> travelingInterfaces = new LinkedList<>();
            travelingInterfaces.offer(interfaces);
            while(travelingInterfaces.size() != 0){
                final Class<?>[] pollInterfaces = travelingInterfaces.poll();
                for (int i = 0; i < pollInterfaces.length; i++) {
                    Class<?>[] superInterfacesAgain = pollInterfaces[i].getInterfaces();
                    // 有父接口！
                    if (superInterfacesAgain.length != 0) {
                        overrideSuperMethodMarked = isOverrideSuperMethodMarked(method, null, superInterfacesAgain, annotationClass);
                        // 没有标记，则继续遍历父接口！
                        if (!overrideSuperMethodMarked) {
                            travelingInterfaces.offer(superInterfacesAgain);
                        }else{
                            return true;
                        }
                    }
                }
            }
        }
        return overrideSuperMethodMarked;

    }
    
    // 用于判断在父类和所有父接口上的重写方法是否标记了特定注解
    private static boolean isOverrideSuperMethodMarked(Method methodRef,
                                                       Class<?> superClass, Class<?>[] interfaces,
                                                       Class<? extends Annotation> annotationClass){
        if (interfaces == null){
            throw new IllegalArgumentException("interfaces arg can not be null");
        }
        boolean declaredAnnotationPresent = false;
        if (superClass != null){
            final Method superClassOverrideMethod = findMethod(methodRef, superClass);
            // 如果方法在父类中找不到，则无需继续判断是否有标记注解
            if (superClassOverrideMethod != null){
                declaredAnnotationPresent = isDeclaredAnnotationPresent(superClassOverrideMethod, annotationClass);
            }
        }
        // 如果declaredAnnotationPresent还是false，则证明super class中没有该重写方法，则该方法可能位于接口中！
        if (!declaredAnnotationPresent){
            // 没有接口则直接返回
            for (Class<?> interfaceClass :
                    interfaces) {
                final Method superInterfaceOverrideMethod = findMethod(methodRef, interfaceClass);
                // 如果在该接口中找到方法,则进行下一步判断！
                if (superInterfaceOverrideMethod != null){
                    declaredAnnotationPresent = isDeclaredAnnotationPresent(superInterfaceOverrideMethod, annotationClass);
                    // 找到了标记处，则中断循环
                    if (declaredAnnotationPresent){
                        break;
                    }
                }
            }
        }
        return declaredAnnotationPresent;
    }


    /*
        重写方法的原则：
            1. 返回值是原方法返回值或是其子类
            2. 方法名相同
            3. 方法参数类型，数量，顺序相同
            4. 异常类型重写方法可以不抛！(该条件无需判断！)

     */
    
    // 用于寻找父类或者父接口的重写方法
    private static Method findMethod(Method methodRef, Class<?> targetClass){
        final String methodRefName = methodRef.getName();
        final Class<?> methodRefReturnType = methodRef.getReturnType();
        final Class<?>[] methodRefParameterTypes = methodRef.getParameterTypes();
        try {
            final Method superMethod = targetClass.getDeclaredMethod(methodRefName, methodRefParameterTypes);
            // because getDeclaredMethod() sometimes will have more than one choices
            // so we have to check out the return type to avoid Synthetic method！
            final Class<?> returnType = superMethod.getReturnType();
            if (methodRefReturnType.isAssignableFrom(returnType)){
                return superMethod;
            }else{
                // if we find a method, but it is a Synthetic method and have different return type!
                // this may not happen, but it just takes preventive measures！
                return null;
            }
        } catch (NoSuchMethodException e) {
            // not found the method，so we return null
            return null;
        }
    }

    /**
     * 在该类本身、该类的所有父类以及该类的所有父接口上判断是否标记了特定注解.
     * <hr>
     * <blockquote style="background-color:rgb(232,232,232)"><pre>
     *     &#64;CallerSensitive
     *     public interface A{}
     *
     *     &#64;CallerSensitive
     *     public interface B{}
     *
     *     &#64;Cancelable
     *     public class C{}
     *
     *     &#64;Api
     *     public class D extends C implements A, B{
     *         &#64;Override
     *         public void test(){}
     *
     *         public void test2(){}
     *     }
     *     // true
     *     AnnotationUtility.isAnnotationInheritedPresent(D.class, CallerSensitive.class);
     *     // true
     *     AnnotationUtility.isAnnotationInheritedPresent(D.class, Api.class);
     *     // true
     *     AnnotationUtility.isAnnotationInheritedPresent(D.class, Cancelable.class);
     * </pre></blockquote>
     * <hr>
     *     该方法和{@link AnnotationUtility#isAnnotationPresent(AnnotatedElement, Class)} 在处理继承性注解上的主要区别是：
     *     {@link AnnotationUtility#isAnnotationPresent(AnnotatedElement, Class)} 只会判断类继承类上的继承性注解,
     *     对于类实现接口以及接口继承接口，则无法判断.而本方法在
     *     {@link AnnotationUtility#isAnnotationPresent(AnnotatedElement, Class)}
     *     的基础上实现了这两种情况的判断.
     *
     * @param clazz 类或者接口的{@link Class}对象
     * @param annotationClass 注解
     * @return 如果该类或接口及其所有父类父接口上标记了该注解, 则返回{@code true},否则返回{@code false}
     * 
     */
    public static boolean isAnnotationInheritedPresent(Class<?> clazz, Class<? extends Annotation> annotationClass){
        checkAnnotatedElementNotNull(clazz);
        boolean annotationMarked = false;
        if (!clazz.isInterface()){
            annotationMarked = isAnnotationPresent(clazz, annotationClass);
        }
        // 所有父类都没有发现特定的继承性注解，开始在所有父类的实现接口中寻找
        if(!annotationMarked){
            Class<?> classTraver = clazz;
            LinkedList<Class<?>[]> classesQueue = new LinkedList<>();
            while(classTraver != null){
                final Class<?>[] interfaces = classTraver.getInterfaces();
                if (interfaces.length != 0){
                    // 加入队列
                    classesQueue.offer(interfaces);
                    // 当前的接口中寻找注解！
                    for (Class<?> interfaceClass:
                            interfaces) {
                        annotationMarked = isDeclaredAnnotationPresent(interfaceClass, annotationClass);
                        if (annotationMarked){
                            return true;
                        }
                    }
                }
                classTraver = classTraver.getSuperclass();
            }

            while(classesQueue.size() != 0){
                final Class<?>[] subSuperInterfaces = classesQueue.poll();
                for (Class<?> subSuperInterface : subSuperInterfaces) {
                    final Class<?>[] subSuperInterfacesAgain = subSuperInterface.getInterfaces();
                    // 子接口中仍然有继承了多个接口
                    if (subSuperInterfacesAgain.length != 0) {
                        classesQueue.offer(subSuperInterfacesAgain);
                    }
                    for (Class<?> sub :
                            subSuperInterfacesAgain) {
                        annotationMarked = isDeclaredAnnotationPresent(sub, annotationClass);
                        if (annotationMarked) {
                            return true;
                        }
                    }
                }
            }
        }

        return annotationMarked;
    }

    // 只有类才有继承性！
    // 1.判断是否标记了可重复注解 √
    // 2.判断是否标记了可重复注解容器 √(直接标记！)
    // 3.判断是否标记了可继承注解(类) ×

    /**
     * 该方法用于判断某个注解是否直接标记在了可被注解元素上.
     * <hr>
     *     方法提供了：
     * <ol>
     *     <li>判断是否直接标记了某个注解         (√)</li>
     *     <li>判断是否标记了可重复注解的注解容器  (√) [相当于直接标记]</li>
     *     <li>判断是否标记了类继承类的可继承性注解  (×)</li>
     *     <li>判断是否标记了可重复注解          (√)</li>
     * </ol>
     * <hr>
     *     本方法和 {@link AnnotationUtility#isAnnotationPresent(AnnotatedElement, Class)} 的区别体现在"直接标记"上,
     *     即本方法会忽略所有的类继承类上的继承性注解.
     * <hr>
     *     另外, 本方法添加了对 {@code Repeatable Annotation}的直接判断, 一般情况下, 使用{@link AnnotationUtility#isAnnotationPresent(AnnotatedElement, Class)}
     *     无法直接判断是否标记了{@code Repeatable Annotation}, 一种方法是通过判断是否存在{@code Repeatable Annotation}的{@code Annotation Container}来间接判断,
     *     或者调用{@link AnnotatedElement#getDeclaredAnnotationsByType(Class)} 并判断其返回的数组长度是否为 {@code 0}来判断.
     *     本方法采用了第二种方法来实现{@code Repeatable Annotation}的直接判断.
     *
     * @param annotatedElement 可被注解的元素, 如{@link Method}、{@link Constructor}、{@link Field}等
     * @param annotationClass 标记的注解
     * @return 如果该可被注解的元素标记了该注解, 则返回{@code true},否则返回{@code false}
     */
    public static boolean isDeclaredAnnotationPresent(AnnotatedElement annotatedElement, Class<? extends Annotation> annotationClass){
        checkAnnotatedElementNotNull(annotatedElement);
        if (annotatedElement.getDeclaredAnnotation(annotationClass) != null){
            return true;
        } else {
            return annotatedElement.getDeclaredAnnotationsByType(annotationClass).length != 0;
        }
    }

    // 1.判断是否标记了可重复注解 √
    // 2.判断是否标记了可重复注解容器 √(直接标记！)
    // 3.判断是否标记了可继承注解(类) √

    /**
     * 该方法相当于{@link AnnotationUtility#isDeclaredAnnotationPresent(AnnotatedElement, Class)}和
     * {@link AnnotationUtility#isAnnotationPresent(AnnotatedElement, Class)}的合并，实现了：
     * <ol>
     *     <li>判断是否直接标记了某个注解         (√)</li>
     *     <li>判断是否标记了可重复注解的注解容器  (√) [相当于直接标记]</li>
     *     <li>判断是否标记了类继承类的可继承性注解  (×)</li>
     *     <li>判断是否标记了可重复注解          (√)</li>
     * </ol>
     *
     * @param annotatedElement 可被注解的元素, 如{@link Method}、{@link Constructor}、{@link Field}等
     * @param annotationClass 标记的注解
     * @return 如果该可被注解的元素标记了该注解, 则返回{@code true},否则返回{@code false}
     * @apiNote 由于一些原因, 暂时不公开该方法
     */
    @Deprecated
    protected static boolean isFullAnnotationPresent(AnnotatedElement annotatedElement, Class<? extends Annotation> annotationClass){
        checkAnnotatedElementNotNull(annotatedElement);
        // 获取普通注解和可继承（类）注解！
        if (isAnnotationPresent(annotatedElement, annotationClass)){
            return true;
        } else {
            // 获取可重复注解和类继承的可重复注解！
            return annotatedElement.getAnnotationsByType(annotationClass).length != 0;
        }
    }

    /**
     * {@link AnnotatedElement#getDeclaredAnnotation(Class)} 的静态包装.
     *
     * @param annotatedElement 可被注解的元素, 如{@link Method}、{@link Constructor}、{@link Field}等
     * @param annotationClass 待获取注解的{@link Class}对象
     * @param <A> Annotation Type
     * @return 获取的特定 {@code annotationClass}对应的直接标记注解对象,包含注解的标记信息, 不包括继承性注解
     */
    public static <A extends Annotation> A getDeclaredAnnotation(AnnotatedElement annotatedElement, Class<A> annotationClass){
        checkAnnotatedElementNotNull(annotatedElement);
        return annotatedElement.getDeclaredAnnotation(annotationClass);
    }

    /**
     * {@link AnnotatedElement#getDeclaredAnnotationsByType(Class)}的静态包装.
     *
     * @param annotatedElement 可被注解的元素, 如{@link Method}、{@link Constructor}、{@link Field}等
     * @param repeatableAnnotationClass 待获取的可重复注解的{@link Class}对象
     * @param <A> Annotation Type
     * @return 可重复标记注解对象数组,包含可重复注解的所有标记信息,不包括继承性注解
     */
    public static <A extends Annotation> A[] getRepeatableDeclaredAnnotations(AnnotatedElement annotatedElement, Class<A> repeatableAnnotationClass){
        checkAnnotatedElementNotNull(annotatedElement);
        return annotatedElement.getDeclaredAnnotationsByType(repeatableAnnotationClass);
    }

    /**
     * {@link AnnotatedElement#getDeclaredAnnotations()}的静态包装.
     * @param annotatedElement 可被注解的元素, 如{@link Method}、{@link Constructor}、{@link Field}等
     * @return 所有直接标记的注解,不包括可重复注解和继承性注解
     */
    public static Annotation[] getDeclaredAnnotations(AnnotatedElement annotatedElement){
        checkAnnotatedElementNotNull(annotatedElement);
        return annotatedElement.getDeclaredAnnotations();
    }

    /**
     * {@link AnnotatedElement#getAnnotations()} 的静态包装
     * @param annotatedElement 可被注解的元素, 如{@link Method}、{@link Constructor}、{@link Field}等
     * @return 包括继承性注解和普通注解，可重复注解将返回注解容器
     */
    public static Annotation[] getAnnotations(AnnotatedElement annotatedElement){
        checkAnnotatedElementNotNull(annotatedElement);
        return annotatedElement.getAnnotations();
    }


    /**
     * {@link AnnotatedElement#getAnnotationsByType(Class)} 的静态包装.
     *
     * @param annotatedElement 可被注解的元素, 如{@link Method}、{@link Constructor}、{@link Field}等
     * @param repeatableAnnotationClass 可重复注解的Class对象
     * @param <A> 可重复注解类型
     * @return 特定的可重复注解，需要注意，该方法并非获取注解容器而是直接获取可重复注解本身，因此返回的数组应该都是同一类型的注解
     */
    public static <A extends Annotation> A[] getRepeatableAnnotations(AnnotatedElement annotatedElement, Class<A> repeatableAnnotationClass){
        checkAnnotatedElementNotNull(annotatedElement);
        return annotatedElement.getAnnotationsByType(repeatableAnnotationClass);
    }

    /**
     * {@link AnnotatedElement#getAnnotation(Class)} 的静态包装.
     *
     * @param annotatedElement 可被注解的元素, 如{@link Method}、{@link Constructor}、{@link Field}等
     * @param annotationClass 待获取注解的{@link Class}对象
     * @param <A> 注解类型
     * @return 注解实例
     */
    public static <A extends Annotation> A getAnnotation(AnnotatedElement annotatedElement, Class<A> annotationClass){
        checkAnnotatedElementNotNull(annotatedElement);
        return annotatedElement.getAnnotation(annotationClass);
    }


    // --------------------------------------------------------------------

    // TODO: 2024/2/12 测试！

    /**
     * 扫描 {@code annotatedElement} 上的所有注解, 包括注解上的注解.
     * <hr>
     * 扫描方式是先扫描 {@code annotatedElement}, 再深入扫描其上面的注解, 如：
     * <blockquote style="background-color:rgb(232,232,232)"><pre>
     *     &#64;Target({ElementType.ANNOTATION_TYPE})
     *     &#64;Retention(RetentionPolicy.RUNTIME)
     *     &#64;Documented
     *     &#64;ApiSupport
     *     &#64;Native
     *     &#64;Api(version = "1.0")
     *     &#64;Running(period = "D")
     *     &#64;Running(period = "NOT")
     *     public &#64;interface Api{
     *          String version();
     *     }
     *
     *     &#64;Api(version = "2.0")
     *     &#64;Running
     *     &#64;Running(period = "high")
     *     Method method = public void method(){}
     *
     *     // result：
     *     //  &#64;Api(version = "2.0")
     *     //  &#64;ApiSupport
     *     //  &#64;Native
     *     //  &#64;Running
     *     //  &#64;Running(period = "high")
     *     //  &#64;Running(period = "D")
     *     //  &#64;Running(period = "NOT")
     *     AnnotationUtility.annotationScan(method);
     * </pre></blockquote>
     * 在上面的例子中, 除了&#64;Running可重复注解外, 注解扫描过程中还出现了两个&#64;Api()注解：&#64;Api(version = "2.0")和 &#64;Api(version = "1.0"),
     * 默认情况下, 出现的所有可重复注解都会被扫描, 而非可重复注解则只会保留一个, 当扫描到两个非可重复注解的时候, 可以提供一个 {@link BiFunction} 来指定保留哪一个注解
     * <hr>
     * 一般情况下,如果不提供 {@code conflictSolver}参数, 方法会采用默认的 {@code conflictSolver}, 即保留第一次扫描得到的非可重复注解, 后续扫描到相同的非可重复注解全部丢弃！
     *
     * @param annotatedElement 待扫描的位置
     * @param conflictSolver 非可重复注解重复冲出处理代码，是一个函数式对象
     * @return 一个 {@link Map}, Key是注解的类型, Value是注解对象数组(可重复注解会有多个成员, 而非可重复注解只有一个成员)
     */
    public static Map<Class<? extends Annotation>, Annotation[]> annotationScan(AnnotatedElement annotatedElement,
                                                                                BiFunction<Annotation, Annotation, Annotation[]> conflictSolver){
        Objects.requireNonNull(annotatedElement);
        if (conflictSolver == null){
            conflictSolver = defaultConflictSolver();
        }
        Map<Class<? extends Annotation>, List<Annotation>> annotationMapper = new HashMap<>();
        // 获取当前annotatedElement上的所有注解
        final Annotation[] annotations = getDeclaredAnnotations(annotatedElement);
        // 递归获取注解的注解
        for (Annotation a :
                annotations) {
            addAnnotationsToMap(annotationMapper, a, conflictSolver);
        }

        // 转换成数组
        // 开始递归遍历注解上的所有扩展元注解注解！
        @SuppressWarnings("unchecked")
        final List<Annotation>[] travelerAnnotations = annotationMapper.values().toArray(new List[0]);
        final LinkedList<Annotation> queue = new LinkedList<>();
        for (List<Annotation> annotationList:travelerAnnotations){
            queue.add(annotationList.get(0));
        }
        Annotation traver = null;
        while((traver = queue.poll()) != null){
            //
            addExtendedMetaAnnotationToMap(annotationMapper, traver.annotationType(), queue, conflictSolver);
        }


        return wrapListToArray(annotationMapper);
    }

    // 默认使用顶层的注解覆盖底层的（第一次的注解覆盖后面的，因为后面的注解一般都是标记在注解上的[扩展元注解]）
    private static BiFunction<Annotation, Annotation, Annotation[]> defaultConflictSolver(){
        return (oldAnnotation, newAnnotation) -> new Annotation[]{ oldAnnotation };
    }

    // 添加扩展元注解到Map, 并且如果是新注解则添加入队列
    private static void addExtendedMetaAnnotationToMap(Map<Class<? extends Annotation>, List<Annotation>> listMap,
                                                       Class<? extends Annotation> annotationClass,
                                                       List<Annotation> queue,
                                                       BiFunction<Annotation, Annotation, Annotation[]> conflictSolver){
        final Annotation[] declaredAnnotations = annotationClass.getDeclaredAnnotations();
        for (Annotation a :
                declaredAnnotations) {
            // 排除元注解
            if (isMetaAnnotation(a)){
                continue;
            }
            if (isRepeatableAnnotationContainer(a.annotationType())){
                // 获取所有的可重复标记注解！
                final Annotation[] repeatableAnnotationsFromContainer = getRepeatableAnnotationsFromContainer(a);
                // 当注解已经在map内的时候，及已经入过queue了
                listMap.computeIfPresent(getContainerOriginalRepeatableAnnotationType(a.annotationType()), (aClass, annotationList) -> {
                    Collections.addAll(annotationList, repeatableAnnotationsFromContainer);
                    return annotationList;
                });
                listMap.computeIfAbsent(getContainerOriginalRepeatableAnnotationType(a.annotationType()), aClass -> {
                    final ArrayList<Annotation> annotationArrayList = new ArrayList<>();
                    Collections.addAll(annotationArrayList, repeatableAnnotationsFromContainer);
                    // 存在注解容器则至少有一个可重复注解被标记！加入队列继续进行遍历
                    queue.add(repeatableAnnotationsFromContainer[0]);
                    return annotationArrayList;
                });
            }
            // 不是可重复注解
            else{
                // 当注解已经在map内的时候，及已经入过queue了
                listMap.computeIfPresent(a.annotationType(), (aClass, annotationList) -> {
                    final Annotation oldAnnotation = annotationList.get(0);
                    final Annotation newAnnotation = a;
                    // 推给用户来决定是否保留哪一个注解，或者全部保留！
                    final Annotation[] applyResult = conflictSolver.apply(oldAnnotation, newAnnotation);
                    Collections.addAll(annotationList, applyResult);
                    return annotationList;
                });
                // 如果不存在！创建List
                listMap.computeIfAbsent(a.annotationType(), aClass -> {
                    final ArrayList<Annotation> annotationArrayList = new ArrayList<>();
                    annotationArrayList.add(a);
                    queue.add(a);
                    return annotationArrayList;
                });
            }
        }
    }

    // 转换Map list参数到数组！
    private static Map<Class<? extends Annotation>, Annotation[]> wrapListToArray(Map<Class<? extends Annotation>, List<Annotation>> listMap){
        Map<Class<? extends Annotation>, Annotation[]> classMap = new HashMap<>();
        listMap.forEach((key, value) -> {
            final Annotation[] annotations = value.toArray(new Annotation[0]);
            classMap.put(key, annotations);
        });
        return classMap;
    }

    // 添加元素到Map，如果List不存在则创建，如果存在则直接添加
    // 如果提供的注解是可重复注解容器，则获取value值，否则直接添加进去
    private static void addAnnotationsToMap(Map<Class<? extends Annotation>, List<Annotation>> listMap,
                                            Annotation annotation,
                                            BiFunction<Annotation, Annotation, Annotation[]> conflictSolver){
        // 注解容器
        if (isRepeatableAnnotationContainer(annotation.annotationType())){
            // 获取注解容器内的所有可重复注解！
            final Annotation[] repeatableAnnotationsFromContainer = getRepeatableAnnotationsFromContainer(annotation);
            listMap.computeIfPresent(getContainerOriginalRepeatableAnnotationType(annotation.annotationType()), (aClass, annotationList) -> {
                Collections.addAll(annotationList, repeatableAnnotationsFromContainer);
                return annotationList;
            });
            listMap.computeIfAbsent(getContainerOriginalRepeatableAnnotationType(annotation.annotationType()), (aClass -> {
                final ArrayList<Annotation> annotationArrayList = new ArrayList<>();
                Collections.addAll(annotationArrayList, repeatableAnnotationsFromContainer);
                return annotationArrayList;
            }));
        }
        else{
            // 如果存在List
            listMap.computeIfPresent(annotation.annotationType(), (aClass, annotationList) -> {
                final Annotation oldAnnotation = annotationList.get(0);
                final Annotation newAnnotation = annotation;
                // 推给用户来决定是否保留哪一个注解，或者全部保留！
                final Annotation[] applyResult = conflictSolver.apply(oldAnnotation, newAnnotation);
                Collections.addAll(annotationList, applyResult);
                return annotationList;
            });
            // 如果不存在！创建List
            listMap.computeIfAbsent(annotation.annotationType(), aClass -> {
                final ArrayList<Annotation> annotationArrayList = new ArrayList<>();
                annotationArrayList.add(annotation);
                return annotationArrayList;
            });
        }
    }

    // 从容器注解中获取可重复注解内容
    private static Annotation[] getRepeatableAnnotationsFromContainer(Annotation a) {
        try {
            final Annotation[] value = (Annotation[]) getAnnotationAttributeValue(a, "value");
            return value;
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }


    // 从容器注解中获取可重复注解的类型
    private static Class<? extends Annotation> getContainerOriginalRepeatableAnnotationType(Class<? extends Annotation> containerClass){
        try {
            final Method value = containerClass.getMethod("value");
            @SuppressWarnings("unchecked")
            final Class<? extends Annotation> returnType = (Class<? extends Annotation>) value.getReturnType();
            return returnType;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("containerClass[" + containerClass + "] is NOT A ANNOTATION CONTAINER because it has no \"value\" attribute!");
        }
    }

    // --------------------------------------------------------------------
    // TODO: 2024/2/11
    // Annotation对象的getClass()返回的是一个Proxy对象！

    /**
     * 返回在注解定义上使用的所有扩展元注解{@code ExtendedMetaAnnotation}.
     * <hr>
     * 一般而言, 用来定义注解的注解会被称为元注解({@code Meta Annotation}),因此对于下面的情况：
     * <blockquote><pre>
     *     &#64;Target({ElementType.ANNOTATION_TYPE})
     *     &#64;Retention(RetentionPolicy.RUNTIME)
     *     &#64;Documented
     *     &#64;ApiSupport
     *     public &#64;interface Api{
     *
     *     }
     * </pre></blockquote>
     *
     * 则可以称 &#64;Target() 、 &#64;Retention 、&#64;Documented、&#64;ApiSupport为元注解.
     * 其中前面三个是{@code JDK}提供给我们的, 而最后一个则可能是我们定义的或者第三方库提供的, 我们一般称最后一个为{@code ExtendedMetaAnnotation}
     * <hr>
     * 如果 {@code annotationClass} 指向一个注解容器, 则获取该注解容器对应的可重复注解上的元注解.
     *
     * @param annotationClass 待获取的注解
     * @return 返回 {@code annotationClass} 上的所有 {@code ExtendedMetaAnnotation}
     */
    public static List<Annotation> getExtendedMetaAnnotations(Class<? extends Annotation> annotationClass){
        Objects.requireNonNull(annotationClass);
        final Annotation[] metaAnnotations = annotationClass.getDeclaredAnnotations();
        List<Annotation> extendedMetaAnnotations = new ArrayList<>();
        for (Annotation a :
                metaAnnotations) {
            if (!isMetaAnnotation(a)){
                if (isRepeatableAnnotationContainer(a.annotationType())){
                    try {
                        // 获取注解容器value值
                        final Object repeatableAnnotations = getAnnotationAttributeValue(a, "value");
                        final int length = Array.getLength(repeatableAnnotations);
                        for (int i = 0; i < length; i++) {
                            final Annotation repeatableAnnotation = (Annotation) Array.get(repeatableAnnotations, i);
                            extendedMetaAnnotations.add(repeatableAnnotation);
                        }
                    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    extendedMetaAnnotations.add(a);
                }
            }
        }
        return extendedMetaAnnotations;
    }
    // 判断注解是否是一个元注解
    private static boolean isMetaAnnotation(Annotation annotation){
        // we marking ALL ANNOTATIONS in package "java.lang.annotation" are MetaAnnotation
        // so we may remove all this annotations while getting Extended MetaAnnotations
        // not include like @Deprecated @FunctionInterface etc..(package java.lang)
        return annotation.annotationType().equals(Target.class) || annotation.annotationType().equals(Retention.class) ||
            annotation.annotationType().equals(Documented.class) || annotation.annotationType().equals(Repeatable.class) ||
            annotation.annotationType().equals(Inherited.class) || Native.class.equals(annotation.annotationType());
    }

    // --------------------------------- api for checking Annotation MetaData ---------------------------------
    // --------------------------------- api for JDK built-in Annotations checking ---------------------------------


    // 如何判断一个注解是否是注解容器？
    // 1.属性的类型是注解，且是数组！
    // 2.对应的可重复注解上标记了@Repeatable，且值是该注解容器

    /**
     * 判断一个注解是否是注解容器
     * @param annotationClass 注解的Class
     * @param <A> 注解类型
     * @return 如果{@code annotationClass}是注解容器，则返回true，否则返回false
     */
    public static <A extends Annotation> boolean isRepeatableAnnotationContainer(Class<A> annotationClass){
        try {
            final Method valueAttr = annotationClass.getMethod("value");
            final Class<?> returnType = valueAttr.getReturnType();
            // 如果注解容器属性类型不是数组,则绝对不是注解容器
            if (!returnType.isArray()){
                return false;
            }
            // 注解容器value属性的ComponentType也必须是注解类型,否则不是注解容器
            final Class<?> componentType = returnType.getComponentType();
            if (!componentType.isAnnotation()){
                return false;
            }
            // 获取可重复注解的注解容器
            @SuppressWarnings("unchecked")
            final Class<? extends Annotation> repeatableAnnotationContainerClass = getRepeatableAnnotationContainerClass((Class<? extends Annotation>) componentType);
            return repeatableAnnotationContainerClass.equals(annotationClass);
        } catch (NoSuchMethodException e) {
            // 注解容器一定会有value()属性，如果一个注解没有value()则肯定不是注解容器
            return false;
        }
    }

    /**
     * 判断注解是否是可重复标记注解.
     *
     * @param annotationClass 注解的{@link Class}对象
     * @param <A> Annotation Type
     * @return 如果是, 则返回{@code true},否则返回 {@code false}
     */
    public static <A extends Annotation> boolean isRepeatableAnnotation(Class<A> annotationClass){
        checkAnnotatedElementNotNull(annotationClass);
        return isAnnotationPresent(annotationClass, Repeatable.class);
    }

    /**
     * 判断注解是否是可继承标记注解.
     *
     * @param annotationClass 注解的{@link Class}对象
     * @param <A> Annotation Type
     * @return 如果是, 则返回{@code true},否则返回 {@code false}
     */
    public static <A extends Annotation> boolean isInheritedAnnotation(Class<A> annotationClass){
        checkAnnotatedElementNotNull(annotationClass);
        return isAnnotationPresent(annotationClass, Inherited.class);
    }

    /**
     * 判断方法或者构造器是否被废弃！
     *
     * @param methodOrConstructor {@link Method} 或者{@link Constructor} 对象
     * @return 如果是, 则返回{@code true},否则返回 {@code false}
     */
    public static boolean isDeprecatedMethod(Executable methodOrConstructor){
        checkAnnotatedElementNotNull(methodOrConstructor);
        return methodOrConstructor.isAnnotationPresent(Deprecated.class);
    }

    /**
     * 判断字段是否被废弃！
     *
     * @param field {@link Field}对象
     * @return 如果是, 则返回{@code true},否则返回 {@code false}
     */
    public static boolean isDeprecatedField(Field field){
        checkAnnotatedElementNotNull(field);
        return field.isAnnotationPresent(Deprecated.class);
    }

    /**
     * 判断方法参数是否废弃！
     *
     * @param parameter {@link Parameter} 对象
     * @return 如果是, 则返回{@code true},否则返回 {@code false}
     */
    public static boolean isDeprecatedParameter(Parameter parameter){
        checkAnnotatedElementNotNull(parameter);
        return parameter.isAnnotationPresent(Deprecated.class);
    }

    /**
     * 判断接口是否标记了 {@code @FunctionalInterface} 注解！
     * <p>
     *     <b>
     *         注意：标记了 {@link FunctionalInterface} 注解的接口一定是函数式接口，但函数式接口不一定会标记 {@link FunctionalInterface}
     *     </b>
     *
     * @param clazz 接口的 {@link Class} 对象
     * @return 如果是, 则返回{@code true},否则返回 {@code false}
     * @throws IllegalArgumentException 当 {@code clazz} 参数不是接口的时候抛出
     */
    public static boolean isFunctionalInterface(Class<?> clazz){
        checkAnnotatedElementNotNull(clazz);
        if (clazz.isInterface()){
            return clazz.isAnnotationPresent(FunctionalInterface.class);
        }
        throw new IllegalArgumentException("clazz is not an interface!");
    }

    /**
     * 方法或者构造器上是否标记了 {@code @SafeVarargs}
     *
     * @param methodOrConstructor methodOrConstructor
     * @return 如果是, 则返回{@code true},否则返回 {@code false}
     */
    public static boolean isMethodSafeVarargs(Executable methodOrConstructor){
        checkAnnotatedElementNotNull(methodOrConstructor);
        return methodOrConstructor.isAnnotationPresent(SafeVarargs.class);
    }

    /**
     * 获取可重复注解对应的注解容器的 {@code Class} 对象
     * @param repeatableAnnotation 可重复注解的 {@code Class} 对象
     * @return 如果是, 则返回{@code true},否则返回 {@code false}
     */
    public static Class<? extends Annotation> getRepeatableAnnotationContainerClass(Class<? extends Annotation> repeatableAnnotation){
        if (isRepeatableAnnotation(repeatableAnnotation)){
            final Repeatable declaredAnnotation = repeatableAnnotation.getDeclaredAnnotation(Repeatable.class);
            return declaredAnnotation.value();
        }
        throw new IllegalArgumentException("Annotation is not Repeatable");
    }

    /**
     * 获取注解上的元数据 &#64;{@linkplain Retention}的值, 其值是 {@linkplain RetentionPolicy} 枚举类型.
     *
     * @param annotationClass 待获取的注解
     * @return 返回 {@linkplain RetentionPolicy}
     */
    public static RetentionPolicy getAnnotationRetentionPolicy(Class<? extends Annotation> annotationClass){
        final Retention retention = annotationClass.getAnnotation(Retention.class);
        return retention.value();
    }

    /**
     * 获取注解上的元数据 &#64;{@linkplain Target}的值. 其值是 {@linkplain ElementType} 枚举类型.
     *
     * @param annotationClass 待获取的注解
     * @return 返回 {@linkplain ElementType}
     */
    public static ElementType[] getAnnotationTarget(Class<? extends Annotation> annotationClass){
        final Target target = annotationClass.getAnnotation(Target.class);
        return target.value();
    }


    // --------------------------------- api for Annotation Definition ---------------------------------

    /**
     * 获取注解的属性的默认值.
     *
     * @param annotationClass 待获取的注解
     * @param propertyName 注解属性名
     * @return 注解属性值
     * @throws NoSuchMethodException 如果不存在该属性, 则会抛出该异常
     */
    public static Object getDefaultValue(Class<? extends Annotation> annotationClass, String propertyName) throws NoSuchMethodException {
        final Method property = annotationClass.getMethod(propertyName);
        return property.getDefaultValue();
    }

    /**
     * 获取注解某个属性的类型.
     *
     * @param annotationClass 待获取的注解的 {@linkplain Class}对象
     * @param attributeName 注解属性名
     * @return 注解属性类型
     * @throws NoSuchMethodException 如果不存在该属性, 则会抛出该异常
     */
    public static Class<?> getAnnotationAttributeType(Class<? extends Annotation> annotationClass,
                                                          String attributeName) throws NoSuchMethodException {
        final Method attribute = annotationClass.getMethod(attributeName);
        return attribute.getReturnType();
    }


    /**
     * 获取注解某个属性的值.
     *
     * @param annotation 待获取的注解
     * @param attributeName 注解属性名
     * @param attributeValueType 属性值的类型, 提供{@linkplain Class}对象
     * @param <V> 值类型
     * @return 注解属性值
     * @throws NoSuchMethodException 如果不存在该属性, 则会抛出该异常
     * @throws InvocationTargetException if the underlying method throws an exception.
     * @throws IllegalAccessException if this {@code Method} object is enforcing Java language access control and the underlying method is inaccessible.
     * @see #getAnnotationAttributeValue(Annotation, String)
     */
    public static <V> V getAnnotationAttributeValue(Annotation annotation, String attributeName, Class<V> attributeValueType) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final Object value = getAnnotationAttributeValue(annotation, attributeName);
        return attributeValueType.cast(value);
    }

    /**
     * 通用的获取注解成员属性的值.
     * <p>
     * 提供该方法是为了当我们拿到的注解是以父类型{@linkplain Annotation}对象形式存在的时候，提供通用的获取方式(无视具体注解类型，而只需关心其有某个属性即可)
     *
     * @param annotation 待获取的注解
     * @param attributeName 注解属性名
     * @return 注解属性值
     * @throws NoSuchMethodException 如果不存在该属性, 则会抛出该异常
     * @throws InvocationTargetException if the underlying method throws an exception.
     * @throws IllegalAccessException if this {@code Method} object is enforcing Java language access control and the underlying method is inaccessible.
     * @see #getAnnotationAttributeValue(Annotation, String, Class)
     */
    public static Object getAnnotationAttributeValue(Annotation annotation, String attributeName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final Class<? extends Annotation> annotationClass = annotation.getClass();
        final Method attribute = annotationClass.getMethod(attributeName);
        final Object value = attribute.invoke(annotation);
        return value;
    }


    /**
     * 判断注解是否拥有某个属性.
     *
     * @param annotationClass 待获取的注解的 {@linkplain Class}对象
     * @param attributeName 注解属性名
     * @return 如果存在, 返回{@code true}, 否则, 返回{@code false}
     */
    public static boolean hasAttribute(Class<? extends Annotation> annotationClass, String attributeName){
        try {
            annotationClass.getMethod(attributeName);
            return true;
        } catch (NoSuchMethodException e) {
            // Attribute Not Found！
            return false;
        }
    }

    /**
     * 修改注解属性值.
     *
     * @param target 待修改的注解
     * @param attributeName 属性名
     * @param attributeValue 属性值
     * @param <A> 注解类型
     * @since 1.0.X
     *
     * @apiNote
     * 无论如何，注解对象一定是一个代理对象, 如下面的 {@code A target} , 对它调用 {@linkplain Object#getClass()} 都会返回 {@code com.sun.proxy.Proxy$X}类名
     * 只要它是一个代理类, 那就绝对会有 InvocationHandler, 因此我们调用 Proxy.getInvocationHandler(target)来获取该InvocationHandler,
     * 而在 Oracle 的 JDK 中，该 InvocationHandler 是位于sun.reflect.annotation下的AnnotationInvocationHandler类，该类内部有一个
     * private final Map<String, Object> memberValues;成员记录着注解当前各成员的值，我们可以通过修改该字段，来达到修改注解属性值的目的，
     * 由于其值参数是Object，你甚至可以替换成注解不允许的值类型
     * 另外，由于没有做更多的测试，因此不知道在其他发行版本的JDK中，是否也存在这样一个AnnotationInvocationHandler，因此，当前该方法的实现思路仅限 Oracle JDK，或者说
     * 仅限提供了AnnotationInvocationHandler类的 Oracle JDK，因此该方法实际上存在一定的Bug
     *
     */
    public static <A extends Annotation> void modifyAnnotation(A target, String attributeName, Object attributeValue){
        final InvocationHandler annotationInvocationHandler = Proxy.getInvocationHandler(target);
        final Field memberValues;
        try {
            // get map field!
            memberValues = annotationInvocationHandler.getClass().getDeclaredField("memberValues");
            memberValues.setAccessible(true);
            // get map field instance！
            @SuppressWarnings("unchecked")
            final Map<String, Object> memberMapper = (Map<String, Object>) memberValues.get(annotationInvocationHandler);
            // check attribute exist
            final Object o = memberMapper.get(attributeName);
            Objects.requireNonNull(o, "注解不存在名为：" + attributeName + "的属性！");
            // check value type
            if (o.getClass().isAssignableFrom(attributeValue.getClass())){
                // replace value
                memberMapper.put(attributeName, attributeValue);
                return;
            }
            throw new IllegalArgumentException("属性值类型不匹配, 期望类型：" + o.getClass() + ", 实际传递值类型：" + attributeValue.getClass());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取注解的所有属性名
     * @param annotationClass 注解的{@linkplain Class}对象
     * @retur 一个字符串数组, 返回所有属性值！
     */
    public static String[] getAnnotationAllAttributes(Class<? extends Annotation> annotationClass){
        final Method[] attributes = annotationClass.getDeclaredMethods();
        String[] result = new String[attributes.length];
        int index = 0;
        for (Method attribute : attributes) {
            result[index++] = attribute.getName();
        }
        return result;
    }

    /**
     * 该方法判断一个注解的某个属性是否有默认值！
     * @param annotationClass 注解类型的{@linkplain Class}对象
     * @param attributeName 属性名
     * @return 如果有默认值，则返回true, 否则返回false!
     */
    public static boolean hasDefaultValue(Class<? extends Annotation> annotationClass, String attributeName){
        try {
            final Object defaultValue = getDefaultValue(annotationClass, attributeName);
            return defaultValue != null;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return false;
        }
    }


    // ------------------------ helper Method ------------------------
    private static Map<Class<?>, Method> getOverrideSuperMethod(Method method, Class<?> superclass, Class<?>[] interfaces){
        Map<Class<?>, Method> overrideSuperMethodsMap = new HashMap<>();
        final Method classOverrideSuperMethod = findMethod(method, superclass);
        if (classOverrideSuperMethod != null) {
            overrideSuperMethodsMap.put(superclass, classOverrideSuperMethod);
        }

        Arrays.stream(interfaces).forEach(interfacesClass -> {
            final Method interfaceOverrideSuperMethod = findMethod(method, interfacesClass);
            overrideSuperMethodsMap.put(interfacesClass, interfaceOverrideSuperMethod);
        });
        return overrideSuperMethodsMap;
    }
}