package cn.argento.askia.unknown.windows.reg.args;


import cn.argento.askia.utilities.lang.AssertionUtility;
import cn.argento.askia.unknown.windows.reg.commands.AbstractRegCommand;
import cn.argento.askia.unknown.windows.reg.commands.RegCommandBuilder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiConsumer;

// all optional args impls
public class OptionalArgsHandler<Args> implements InvocationHandler {

    // 存放具体Reg指令调用的参数，如RegAdd指令的ve()、v()等
    // 该Map中的数据会参与最终Reg指令的拼接
    // 设计格式：类名:方法名<-->ArgsInfo，其中key如此设计是为了方便定位具体的调用接口和方法
    // 比如调用了ArgV接口的v()方法就会找到ArgV:v的key
    // 其值是一个ArgsInfo的包装类，能够直接将该参数转为参数字符串，例如：ArgV接口的v(String name)方法会被转为参数/v name
    private Map<String, ArgsInfo> argsCalled;
    // 子指令具体的内部可选参数接口，如RegAdd的RegAddOptionalArgs接口
    // 保存该接口是为了获取子指令支持的参数，通过获取RegAddOptionalArgs接口的父接口即可获得
    private final Class<Args> argsClassProxy;
    // 保存RegCommand实例，并为其包装成Builder类，第一是辅助Arg接口的Build()方法的返回
    // 保存该实例用于提前build command，以及通过该Builder暴露一些方法给OptionalArgsHandler使用！
    // 比如registerSameArgs()等
    private final RegCommandBuilder command;

    // 预防重复参数，比如v和ve,不能同时调用
    // 怎么排除？通过注册参数位的形式，让v和ve占用同一个位即可，然后每次调用方法就进行参数位确认
    protected BitSet argsDuplicatedSet;

    // args<->Bit container, for sub command args!
    // Don‘t use 0! 00000000 meas no args were put in BitSet yet！
    // 存储所有的参数及其参数位
    protected Map<String, BitSet> argBitSetMap;


    OptionalArgsHandler(Class<Args> argsClass, AbstractRegCommand command){
        this.command = new RegCommandBuilder(command);
        this.argsClassProxy = argsClass;
        // 初始化argsDuplicatedSet
        this.argsDuplicatedSet = new BitSet();
        this.argsCalled = new HashMap<>();
        // 相同keys的封装
        sameKeys = new SameKeys();
        registerSameArgs();
        registerArgBits();
    }

    class SameKeys {
        Map<String, Set<String>> sameKeyMap;
        SameKeys(){
            sameKeyMap = new HashMap<>();
        }
        private Set<String> createKeysSet(){
            return new HashSet<>();
        }
        void addSameKeys(String key, String sameAs){
            AssertionUtility.requireNotEquals(key, sameAs);
            final Set<String> sameKeySet = sameKeyMap.getOrDefault(sameAs, null);
            // sameKeyMap内没有该代表相同的Key，则注册
            // 注意，有相同key的前提是已经存在了一对相同的key（2个key或以上），否则不可能会存在相同key
            // 所以遇到这种情况就需要新注册两个相同的key
            if (sameKeySet == null){
                // 1. 创建KeySet
                final Set<String> keysSet = createKeysSet();
                keysSet.add(key);
                keysSet.add(sameAs);
                sameKeyMap.put(key, keysSet);
                sameKeyMap.put(sameAs, keysSet);
            }
            else{
                sameKeySet.add(key);
                sameKeyMap.put(key, sameKeySet);
            }
        }
        Set<String> getSameKeysSet(String key){
            // 获取所有相同的keys
            final Set<String> sameKeysSet = sameKeyMap.get(key);
            // 移除所有相同 Set 的 keys
            for (String sameKey : sameKeysSet) {
                sameKeyMap.remove(sameKey, sameKeysSet);
            }
            return sameKeysSet;
        }
        boolean hasSameKeysSet(){
            return sameKeyMap.size() > 0;
        }

    }
    private SameKeys sameKeys;

    // 注册相同类型的参数的方法，回调给RegCommand对象
    private void registerSameArgs() {
        BiConsumer<String, String> registerSameFunction = (s, s2) -> {
            sameKeys.addSameKeys(s, s2);
        };
        command.registerSameArgs(registerSameFunction);
    }

    /**
     *this method for init args<->Bit mapping.<br>
     *
     * in this method, you can use {@linkplain #register(String...) register} method for adding args, like：
     * <code>register("s", "ve", "v")</code>.<br>
     *
     * <b>Remember don't add '/' to the arg! <code>register("/s", "/ve", "/v")</code> is not permitted</b>
     */
    private void registerArgBits(){
        final Class<?>[] interfaces = argsClassProxy.getInterfaces();
        final Set<String> registeredArgs = new HashSet<>();
        for (Class<?> interfacez: interfaces) {
            final String simpleName = interfacez.getSimpleName();
            final Method[] methods = interfacez.getMethods();
            for (Method m : methods) {
                final String methodName = m.getName();
                final RegCommandArg regCommandArgAnnotation = ArgManager.getRegCommandArgAnnotation(simpleName, methodName);
                // 如果该参数没有注册过,则注册该参数
                if (!registeredArgs.contains(regCommandArgAnnotation.argName())){
                    // 注册参数并加入已注册参数列表
                    register(regCommandArgAnnotation.argName());
                    registeredArgs.add(regCommandArgAnnotation.argName());
                    // 获取同类参数
                    final Set<String> sameKeysSet = sameKeys.getSameKeysSet(regCommandArgAnnotation.argName());
                    // 如果有同类参数则进行注册，并且添加到已注册列表中
                    if (sameKeysSet != null){
                        for (String arg : sameKeysSet) {
                            registerAsSame(regCommandArgAnnotation.argName(), arg);
                            registeredArgs.add(arg);
                        }
                    }
                }
                // 如果参数注册过了，则直接注册下一个参数
            }
        }
    }
    /**
     *
     * you don't need to register the bitset for args,
     * the bitset will be mapped to the arg automatically.<br>
     *
     * just point out what args the sub command has!:)
     *
     * @param argNames what args this sub command has
     */
    protected void register(String... argNames){
        // get current register bits counts
        // for example: return 3 means you have registered 001、 010、 100 for 3 args
        final int currentExistBitSize = argBitSetMap.size();
        // traverse args
        for (int i = 0; i < argNames.length; i++) {
            // make sure the highest bit is 1
            BitSet bitSet = new BitSet();
            // bitset counts bits index from 0
            // that means set(0) will get 001
            // set(1) will get 010
            bitSet.set(currentExistBitSize + i);
            argBitSetMap.put(argNames[i], bitSet);
        }

    }

    /**
     * if two args are Mutually exclusive, using this <code>registerAsSame</code> method.<br>
     *
     * for example, "v" and "ve", if you use "v", that "ve" will be disable, or
     * if you use "ve" , that "v" will be disable.<br>
     *
     * <b>using this method, you must pay attention to make one of these two args was registered!
     * </b>
     *
     * @param key1 key1
     * @param key2 key2
     */
    protected void registerAsSame(String key1, String key2){
        final BitSet bitSet = argBitSetMap.get(key1);
        if (bitSet == null){
            final BitSet bitSet1 = argBitSetMap.get(key2);
            Objects.requireNonNull(bitSet1);
            argBitSetMap.put(key1, (BitSet) bitSet1.clone());
            return;
        }
        argBitSetMap.put(key2, (BitSet) bitSet.clone());
    }

    private static RegCommandArg findRegCommandArgAnnotationFromMethod(Method method) throws NoSuchMethodException {
        RegCommandArg annotation = method.getAnnotation(RegCommandArg.class);
        if (annotation == null) {
            // TODO: 2024/11/1 如果当前方法没有找到，则需要尝试在其父接口中寻找！
            // 获取方法在哪个类中被声明
            final Class<?> declaringClass = method.getDeclaringClass();
            final String name = method.getName();
            final List<Class<?>> argsDefineInterfaces = ArgManager.getArgsDefineInterfaces(name);
            for (Class<?> argInterface:
                    argsDefineInterfaces) {
                // 由于相同方法名的不同参数接口只会实现其一，因此可以这样写！
                if (argInterface.isAssignableFrom(declaringClass)) {
                    final Method interfaceMethod = argInterface.getMethod(method.getName(), method.getParameterTypes());
                    annotation = interfaceMethod.getAnnotation(RegCommandArg.class);
                }
            }
        }
        return annotation;
    }

    private final Object objectMethodProxy = new Object();
    // 1.调用同类参数--> 过argsDuplicatedSet --> 发现重复 --> 去除所有重复的内容 --> 重新添加ArgInfo
    // 2.调用非同类参数 --> 包装成ArgInfo
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 调用了Arg的build()方法,返回RegCommand！
        if (method.getName().equalsIgnoreCase("build")){
            // TODO: 2024/11/1 执行build()，需要进一步重写
            command.build();
            return command;
        }
        // 寻找Arg接口以及子接口方法上的@RegCommandArg注解
        // todo：该方法需要重写
        RegCommandArg annotation = findRegCommandArgAnnotationFromMethod(method);
        // 如果找不到，则证明不是Arg接口上的方法（除了build，但是前面已经判断过）
        // 转为委托Object进行调用
        if (annotation == null){
            return method.invoke(objectMethodProxy, args);
        }
        // 如果找到了，则需要将其包装成ArgInfo，并存入argsCalled
        // 在包装之前，需要进行参数验证，即如果已经调用过同类的参数，则我们需要做一次参数替换，如果没有调用过，则添加对应的位
        // 比如RegAdd中我们已经调用了v()，然后再次调用ve()，则需要去除v参数相关的ArgInfo等内容
        final String argName = annotation.argName();
        final BitSet bitSet = argBitSetMap.get(argName);
        // 发现重复参数
        if (checkArgExists(argsDuplicatedSet, bitSet)){
            // 去除相同的参数
            removeDuplicatedArg(argName);
        }
        // TODO: 2024/11/1 包装现在的参数



        // 返回代理对象即可，因为代理对象是接口的实现。
        return proxy;
    }
    private boolean checkArgExists(BitSet bitSet1, BitSet bitSet2){
        final BitSet clone = (BitSet)bitSet1.clone();
        clone.and(bitSet2);
        return clone.equals(bitSet2);
    }
    private void removeDuplicatedArg(String argName){
        // 因为参数不多，轮询argsCalled找出相同的参数并去除
        final Set<String> set = argsCalled.keySet();
        for (String key : set) {
            if (argBitSetMap.get(key).equals(argBitSetMap.get(argName))){
                argsCalled.remove(key);
                return;
            }
        }
    }


    // 获取参数
    public ArgsInfo[] getArgsInfo(){
        return argsCalled.values().toArray(new ArgsInfo[0]);
    }


}
