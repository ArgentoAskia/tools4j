package cn.argento.askia.utilities.windows.reg.args;

import cn.argento.askia.utilities.windows.reg.commands.AbstractRegCommand;
import cn.argento.askia.utilities.windows.reg.commands.RegCommand;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.file.Files;
import java.util.*;

// 重写
public final class ArgManager {

    // do not instance!
    private ArgManager(){}

    public static void main(String[] args) {
        final Package aPackage = ArgManager.class.getPackage();
        System.out.println(aPackage.getName());
        System.out.println(Arrays.toString(scanArgsClasses()));
        System.out.println(argsCache);

    }

    // 缓存类型 类名:方法名 ，RegCommand
    private static Map<String, RegCommandArg> argsCache = new HashMap<>();
    static {
        final Class<?>[] classes = scanArgsClasses();
        for (Class<?> cl: classes) {
            final Method[] methods = cl.getDeclaredMethods();
            for (Method m: methods) {
                addArgsDefineInterface(m.getName(), cl);
                // 过滤掉非public方法和static方法
                if (Modifier.isPublic(m.getModifiers()) && !Modifier.isStatic(m.getModifiers())){
                    final RegCommandArg annotation = m.getAnnotation(RegCommandArg.class);
                    final String methodName = m.getName();
                    final String clSimpleName = cl.getSimpleName();
                    argsCache.put(clSimpleName + ":" + methodName, annotation);
                }
            }
        }
    }
    // 获取解析完成的类型
    public static RegCommandArg getRegCommandArgAnnotation(String argClassName, String methodName){
        return argsCache.getOrDefault(argClassName + ":" + methodName, null);
    }
    // 扫描所有标记了@RegCommandArg注解的类, 这些类都是参数类！
    private static Class<?>[] scanArgsClasses(){
        final URL argsClassesPath = ArgManager.class.getResource("./");
        System.out.println(argsClassesPath.getPath());
        File file = new File(argsClassesPath.getPath());
        final String[] argsClassesNames = file.list();
        assert argsClassesNames != null;
        List<Class<?>> argClassesList = new ArrayList<>();
        for (String argClassName : argsClassesNames) {
            final Class<?> argsClass = getArgsClass(argClassName);
            if (argsClass != null){
                argClassesList.add(argsClass);
            }
        }
        return argClassesList.toArray(new Class[0]);
    }
    // scanArgsClasses()辅助类加载方法
    private static Class<?> getArgsClass(String className) {
        int end = className.length() - 6;
        String simpleClassName = className.substring(0, end);
        String fullClassName = ArgManager.class.getPackage().getName() + "." + simpleClassName;
        final Class<?> argClass;
        try {
            argClass = Class.forName(fullClassName, false, ArgManager.class.getClassLoader());
            if (argClass.isAnnotationPresent(RegCommandArg.class)){
                return argClass;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static Map<String, List<Class<?>>> argsDefineInterfaces = new HashMap<>();
    private static void addArgsDefineInterface(String arg, Class<?> cl){
        List<Class<?>> classList = argsDefineInterfaces.get(arg);
        if (classList == null){
            classList = new ArrayList<>();
        }
        classList.add(cl);
        argsDefineInterfaces.put(arg, classList);
    }
    public static List<Class<?>> getArgsDefineInterfaces(String methodName){
        return argsDefineInterfaces.get(methodName);
    }


    // TODO: 2024/11/1 buid
    public static ArgsInfo buildArgInfo(){
        return null;
    }


    @SuppressWarnings("unchecked")
    public static <A> A createOptionArgsProxy(Class<A> opaClass, OptionalArgsHandler<?> subCommandHandlerProxy){
        return (A) Proxy.newProxyInstance(ArgManager.class.getClassLoader(),
                new Class[]{opaClass},
                subCommandHandlerProxy);
    }

    public static <A> OptionalArgsHandler<A> newOptionalArgsHandler(Class<A> opaClass, AbstractRegCommand regCommand){
        return new OptionalArgsHandler<>(opaClass, regCommand);
    }




}
