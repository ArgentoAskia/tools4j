package cn.argento.askia.utilities.classes;

import cn.argento.askia.annotations.Utility;
import cn.argento.askia.utilities.system.SystemUtility;
import cn.argento.askia.utilities.collection.ArrayUtility;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 和Class(类扫描)、ByteCode(字节码处理)、ClassLoader(类加载)相关的工具类.
 *
 * <p>提供基础的包扫面能力
 *
 * @author askia
 */
@Utility("Java类分析工具类")
public class ClassUtility {

    private ClassUtility(){
        throw new IllegalAccessError("ClassUtility为工具类, 无法创建该类的对象");
    }

    /**
     * 获取当前类文件所在的目录
     * @param cl
     * @return
     * @throws MalformedURLException
     */
    public static URL getCurrentClassesPath(Class<?> cl) throws MalformedURLException {
        ClassLoader classLoader = cl.getClassLoader();
//        URL systemResource = ClassLoader.getSystemClassLoader().getResource(".");
//        System.out.println(systemResource);
        Package classNamePackage = cl.getPackage();
        String classNamePackageName = classNamePackage.getName().replace('.', '/');
        if (classLoader == null){
            // classLoader是BootStrap Class Loader
            // 所有的jdk.*、Java.*包会被这个加载
            // 我们能做的只有返回当前JDK的位置
            String currentRunningJDKDir = SystemUtility.getCurrentRunningJDKDir();
            File file = new File(currentRunningJDKDir);
            return file.toURI().toURL();
        }
        return classLoader.getResource(classNamePackageName);
    }

    /**
     * 获取当前类所在的根目录（不算Package）
     * @param cl
     * @return
     * @throws MalformedURLException
     */
    public static String getCurrentClassesPathRoot(Class<?> cl) throws MalformedURLException {
        URL currentClassesPath = getCurrentClassesPath(cl);
        Package clPackage = cl.getPackage();
        String packageName = clPackage.getName().replace('.', '/');
        String protocol = currentClassesPath.getProtocol();
        String path = currentClassesPath.getPath();
        if("file".equalsIgnoreCase(protocol)){
            // 文件或者目录
            if (path.contains(packageName)){
                // 替换掉Package部分
                path = path.replace(packageName, "");
            }
            return path;
        }
        else if ("jar".equalsIgnoreCase(protocol)){
            // spring boot BOOT-INF scan Path!
            String scanPath = "!/BOOT-INF/classes!/";
            if (path.contains(packageName)){
                // 替换掉Package部分
                path = path.replace(packageName, "");
            }
            if (path.contains(scanPath)){
                // 替换掉springboot部分
                path = path.replace(scanPath, "");
            }
            return path;
        }
        throw new RuntimeException("并非是Jar文件或者目录");
    }

    /**
     * 此方法会扫描{@code class}文件所在的{@code classpath}下的所有类, 兼容{@code maven}和{@code jar}文件
     * @param cl class对象
     * @return  {@code class}所在的{@code classpath}下的所有类(包括所有子包！)
     * @throws MalformedURLException
     */
    public static Class<?>[] scanClassesFromClass(Class<?> cl) throws MalformedURLException {
        String scanPath = getCurrentClassesPathRoot(cl);
        // 如果是Jar包
        String protocol = scanPath.contains(".jar")? "jar":"file";
        File scanPathFile = new File(scanPath);
        if ("file".equalsIgnoreCase(protocol) && scanPathFile.isDirectory()){
            return doDirectory(scanPath);
        }
        else {
            return doJarFile(scanPath);
        }
    }
    // 扫描classpath下的所有类
    private static Class<?>[] doJarFile(String path){
        List<Class<?>> resultList = new ArrayList<>();
        try {
            // 去除jar:file:/中的file:/
            if (path.startsWith("file:/")){
                path = path.replace("file:/", "");
            }
            System.out.println(path);
            JarFile jarFile = new JarFile(path);
            Queue<JarEntry> queue = new LinkedList<>();
            // 扫描jar files
            for (Enumeration<JarEntry> e = jarFile.entries(); e.hasMoreElements(); ) { //这个循环会读取jar包中所有文件，包括文件夹
                JarEntry jarEntry = e.nextElement();
                System.out.println(jarEntry.getName());
                queue.offer(jarEntry);
            }
            while(queue.size() > 0){
                JarEntry poll = queue.poll();
                if (poll == null){
                    continue;
                }
                // 目录则过滤
                if (poll.isDirectory()){
                    continue;
                }
                //getName()会获取文件全路径名称
                String classFullPath = poll.getName();
                if (!classFullPath.endsWith(".class") && !classFullPath.endsWith(".java")){
                    continue;
                }
                if (classFullPath.contains("BOOT-INF/classes/")){
                    classFullPath = classFullPath.replace("BOOT-INF/classes/", "");
                }
                String classFullPathWithoutClassSuffix = classFullPath.replace(".class", "");
                String classFullPathWithoutClassSuffixPointer = classFullPathWithoutClassSuffix.replace('/', '.');
                System.out.println("classFullPathWithoutClassSuffixPointer = " + classFullPathWithoutClassSuffixPointer);
                Class<?> clz = Class.forName(classFullPathWithoutClassSuffixPointer);
                resultList.add(clz);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return resultList.toArray(new Class[0]);
    }
    // 扫描classpath下的所有类
    private static Class<?>[] doDirectory(String path){
        File scanPath = new File(path);
        if (!scanPath.exists()){
            return new Class[0];
        }
        List<Class<?>> retList = new ArrayList<>();
        Queue<File> dirs = new LinkedList<>();
        dirs.offer(scanPath);
        while(dirs.size() > 0){
            File poll = dirs.poll();
            File[] files = poll.listFiles(pathname -> {
                // 如果是目录则放弃
                if (pathname.isDirectory()){
                    dirs.offer(pathname);
                    return false;
                }
                // 获取路径
                String fileName = pathname.getName();
                // class结尾或者java结尾, 那包可以运行的
                // 其他的话可能就不行了！
                return fileName.endsWith("class") || fileName.endsWith("java");
            });
            if (files == null){
                files = new File[0];
            }
            ArrayUtility.foreach(files, file -> {
                Path resolve = Paths.get(path.substring(1)).relativize(file.toPath());
                String fullClassName = resolve.toString();
                if (fullClassName.contains(".class")){
                    fullClassName = fullClassName.replace(".class", "");
                }
                if (fullClassName.contains(".java")){
                    fullClassName = fullClassName.replace(".java", "");
                }
                fullClassName = fullClassName.replace("/", ".");
                fullClassName = fullClassName.replace("\\", ".");
                try {
                    Class<?> cl = Class.forName(fullClassName);
                    retList.add(cl);
                } catch (ClassNotFoundException exception) {
                    exception.printStackTrace();
                }
            });
        }
        return retList.toArray(new Class[0]);
    }

    public static Class<?>[] scanCurrentDirectoryClasses(Class<?> cl, boolean includeInnerClasses, boolean includeDeepDirectories) throws IOException {
        if (!includeDeepDirectories){
            return scanCurrentDirectoryClasses(cl, includeInnerClasses);
        }
        URL currentClassesPath = getCurrentClassesPath(cl);
        String path = currentClassesPath.getPath();
        String packageName = cl.getPackage().getName();
        if (currentClassesPath.getProtocol().equalsIgnoreCase("file")){
            return deepScanDir(path, packageName, includeInnerClasses);
        }
        else if (currentClassesPath.getProtocol().equalsIgnoreCase("jar")){
            return deepScanJar(path, packageName, includeInnerClasses);
        }
        return new Class[0];
    }
    private static Class<?>[] deepScanDir(String path, String packageName, boolean includeInnerClasses){
        File baseScanDir = new File(path);
        File[] scanResult = baseScanDir.listFiles();
        // baseScanDir不是真正的路径(路径不能访问或者是文件)
        if (scanResult == null){
            scanResult = new File[0];
        }
        List<String> classNames = new ArrayList<>();
        Queue<File> deepDirQueue = new LinkedList<>();
        for (File f : scanResult) {
            // 目录
            if (f.isDirectory()){
                deepDirQueue.offer(f);
            }
            else {
                // class文件
                if (f.getName().endsWith(".class")){
                    classNames.add(packageName + "." + f.getName().replace(".class", ""));
                }
                // java文件
                if (f.getName().endsWith(".java")) {
                    classNames.add(packageName + "." + f.getName().replace(".java", ""));
                }

            }
        }
        while(deepDirQueue.size() > 0){
            File poll = deepDirQueue.poll();
            recursionDeepScanDir(poll, deepDirQueue, classNames, packageName);
        }
        return classNames.stream().map( s -> {
            if (s == null){
                return null;
            }
            if (!includeInnerClasses && s.contains("$")){
                return null;
            }
            try {
                return Class.forName(s);
            } catch (ClassNotFoundException exception) {
                exception.printStackTrace();
                return null;
            }
        }).filter(Objects::nonNull).toArray(Class<?>[]::new);
    }
    private static void recursionDeepScanDir(File file, Queue<File> deepDirQueue, List<String> classNames, String packageName){
        File[] scanResult = file.listFiles();
        packageName = packageName + "." + file.getName();
        // baseScanDir不是真正的路径(路径不能访问或者是文件)
        if (scanResult == null){
            scanResult = new File[0];
        }
        for (File f : scanResult) {
            // 目录
            if (f.isDirectory()){
                deepDirQueue.offer(f);
            }
            else {
                // class文件
                if (f.getName().endsWith(".class")){
                    classNames.add(packageName + "." + f.getName().replace(".class", ""));
                }
                // java文件
                if (f.getName().endsWith(".java")) {
                    classNames.add(packageName + "." + f.getName().replace(".java", ""));
                }
            }
        }
    }
    private static Class<?>[] deepScanJar(String path, String packageName, boolean includeInnerClasses){
        List<Class<?>> resultList = new ArrayList<>();
        String packageNameClassPath = packageName.replace('.', '/');
        try {
            // 去除jar:file:/中的file:/
            if (path.startsWith("file:/")){
                path = path.replace("file:/", "");
            }
            System.out.println(path);
            JarFile jarFile = new JarFile(path);
            Queue<JarEntry> queue = new LinkedList<>();
            // 扫描jar files
            for (Enumeration<JarEntry> e = jarFile.entries(); e.hasMoreElements(); ) { //这个循环会读取jar包中所有文件，包括文件夹
                JarEntry jarEntry = e.nextElement();
                // 过滤所有的
                if (jarEntry.getName().contains(packageNameClassPath)){
                    queue.offer(jarEntry);
                }
            }
            while(queue.size() > 0){
                JarEntry poll = queue.poll();
                if (poll == null){
                    continue;
                }
                // 目录则过滤
                if (poll.isDirectory()){
                    continue;
                }
                //getName()会获取文件全路径名称
                String classFullPath = poll.getName();
                if (!classFullPath.endsWith(".class") && !classFullPath.endsWith(".java")){
                    continue;
                }
                if (classFullPath.contains("BOOT-INF/classes/")){
                    classFullPath = classFullPath.replace("BOOT-INF/classes/", "");
                }
                String classFullPathWithoutClassSuffix = classFullPath.replace(".class", "");
                String classFullPathWithoutClassSuffixPointer = classFullPathWithoutClassSuffix.replace('/', '.');
                System.out.println("classFullPathWithoutClassSuffixPointer = " + classFullPathWithoutClassSuffixPointer);
                if (classFullPathWithoutClassSuffixPointer.contains("$") && !includeInnerClasses){
                    continue;
                }
                Class<?> clz = Class.forName(classFullPathWithoutClassSuffixPointer);
                resultList.add(clz);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return resultList.toArray(new Class[0]);
    }

    /*
        此方法会扫描当前cl对象所在的包下的所有类，注意不包含子包！仅仅是当前目录.
        includeInnerClasses = false时, 忽略所有的$类
     */
    public static Class<?>[] scanCurrentDirectoryClasses(Class<?> cl, boolean includeInnerClasses) throws IOException {
        URL currentClassesPath = getCurrentClassesPath(cl);
        String path = currentClassesPath.getPath();
        String packageName = cl.getPackage().getName();
        if (currentClassesPath.getProtocol().equalsIgnoreCase("file")){
            return scanDir(path, packageName, includeInnerClasses);
        }
        else if (currentClassesPath.getProtocol().equalsIgnoreCase("jar")){
            return scanJar(path, packageName, includeInnerClasses);
        }
        return new Class[0];
    }
    // 扫描当前目录下的所有class和java文件(不包含子目录)
    private static Class<?>[] scanDir(String path, String packageName, boolean includeInnerClasses){
        File file = new File(path);
        File[] files = file.listFiles();
        if (files == null){
            files = new File[0];
        }
        List<String> className = Arrays.stream(files).map(file1 -> {
            // 过滤.java和.class
            if (file1.isFile()){
                return file1.getName().endsWith(".class")?
                        packageName + "." + file1.getName().replace(".class", ""):
                        file1.getName().endsWith(".java")?
                                packageName + "." + file1.getName().replace(".java", ""):
                                null;
            }
            return null;
        }).collect(Collectors.toList());

        return className.stream().map( s -> {
            if (s == null){
                return null;
            }
            if (!includeInnerClasses && s.contains("$")){
                return null;
            }
            try {
                return Class.forName(s);
            } catch (ClassNotFoundException exception) {
                exception.printStackTrace();
                return null;
            }
        }).filter(Objects::nonNull).toArray(Class<?>[]::new);
    }
    // 扫描当前jar目录下的所有class和java文件(不包含子目录)
    private static Class<?>[] scanJar(String path, String packageName, boolean includeInnerClasses) throws IOException {
        String jarFilePath = path;
        if (!jarFilePath.endsWith(".jar")){
            int i = jarFilePath.indexOf("!");
            jarFilePath = jarFilePath.substring(0, i);
        }
        if (jarFilePath.startsWith("file:/")){
            jarFilePath = jarFilePath.replace("file:", "");
        }
        System.out.println("jarFilePath = " + jarFilePath);
        JarFile jarFile = new JarFile(jarFilePath);
        return jarFile.stream().parallel().filter(jarEntry -> {
            String packagePathName = packageName.replace('.', '/');
            // entry必须是文件，必须是包含特定的包名前缀，必须是java文件或者class文件
            return !jarEntry.isDirectory() &&
                    jarEntry.getName().contains(packagePathName) &&
                    (jarEntry.getName().endsWith(".java") || jarEntry.getName().endsWith(".class"));
        }).map(jarEntry -> {
            String name = jarEntry.getName();
            // 过滤掉springboot前缀
            if (name.contains("BOOT-INF/classes/")) {
                name = name.replace("BOOT-INF/classes/", "");
            }
            // 过滤掉.class后缀
            if (name.endsWith(".class")) {
                name = name.replace(".class", "");
            }
            // 过滤掉.java后缀
            if (name.endsWith(".java")) {
                name = name.replace(".java", "");
            }
            // 替换为.
            return name.replace('/', '.');
        }).map((Function<String, Class<?>>) s -> {
            if (!includeInnerClasses && s.contains("$")){
                return null;
            }
            try {
                return Class.forName(s);
            } catch (ClassNotFoundException exception) {
                exception.printStackTrace();
                return null;
            }
        }).filter(Objects::nonNull).toArray(Class<?>[]::new);
    }



//    public static void main(String[] args) throws IOException, Exception {
////        Class<?> aClass = Class.forName("java.lang.String", false, ClassLoader.getSystemClassLoader());
//        Class<?> aClass = Class.forName(ClassUtility[].class.getName());
//        System.out.println(aClass);
////        Class<?>[] classes = scanClassesFromClass(RandomUtility.class);
////        System.out.println(Arrays.toString(classes));
////        System.out.println(classes.length);
////        Class<?>[] classes = scanCurrentDirectoryClasses(RandomUtility.class, false, true);
////        System.out.println(Arrays.toString(classes));
//        // [A-Za-z_]\\w*(\\.[A-Za-z_]\\w*)?(,[A-Za-z_]\\w*(\\.[A-Za-z_]\\w*)*)*
////        Pattern compile = Pattern.compile("([A-Za-z_]\\w*(?:\\.[A-Za-z_]\\w*)*)");
////        Matcher matcher = compile.matcher("com.yumitoy.wms.pages.datasources.SqlRunner");
////        System.out.println(matcher.groupCount());
////        while(matcher.find()){
////            System.out.println(matcher.group(1));
////        }
////        System.out.println(compile.matcher("com.yumitoy.wms.pages.datasources.SqlRunner<java.lang.Object, com.yumitoy.wms.dao.ProductsDAO>").matches());
////        String test = "com.yumitoy.wms.pages.runner.SqlRunner<java.util.List<com.yumitoy.wms.beans.products.vo.ProductCardVO>, com.yumitoy.wms.pages.runner.SqlRunner$MainPageDaoWrapper>";
//        String test = "java.util.List<com.yumitoy.wms.beans.products.vo.ProductCardVO>";
//        Pattern noGenericType = Pattern.compile("([A-Za-z_]\\w*(?:\\.[A-Za-z_]\\w*)*)\\s*<\\s*(.*[,.*]*)\\s*>\\s*");
//        Matcher matcher = noGenericType.matcher(test);
//        System.out.println(matcher.matches());
//        System.out.println(matcher.groupCount());
//        String group = matcher.group(1);
//        String group2 = matcher.group(2);
//        System.out.println(group);
//        System.out.println(group2);
//        ArrayUtility.foreach(group2.split(","), new Consumer<String>() {
//            @Override
//            public void accept(String s) {
//                System.out.println(s.trim());
//            }
//        });
//    }

    // todo move to ReflectUtils
    public static int getGenericTypeClassesCount(Type type){
        String typeName = type.getTypeName();
        Pattern noGenericType = Pattern.compile("([A-Za-z_]\\w*(?:\\.[A-Za-z_]\\w*)*)\\s*<\\s*((?:[A-Za-z_]\\w*(?:\\.[A-Za-z_]\\w*)*)(?:(?:,\\s*)(?:[A-Za-z_]\\w*(?:\\.[A-Za-z_]\\w*)*))*)\\s*>\\s*");
        Matcher matcher = noGenericType.matcher(typeName);
        if (matcher.matches()) {
            String group = matcher.group(2);
            return group.split(",").length;
        }
        return 0;
    }



    // todo move to ReflectUtils

    /**
     * 获取Generic Class上的所有Generic Type,
     * <p>比如：com.yumitoy.wms.pages.datasources.SqlRunner<java.lang.Object, com.yumitoy.wms.dao.ProductsDAO>,
     * 则获取 java.lang.Object 和 com.yumitoy.wms.dao.ProductsDAO.
     * <p>再如：com.yumitoy.wms.pages.datasources.SqlRunner<java.lang.Object, java.lang.String, com.yumitoy.wms.dao.ProductsDAO>,
     * 则获取 java.lang.Object、java.lang.String 和 com.yumitoy.wms.dao.ProductsDAO.
     *
     * @param type 泛型类型 {@code GenericType}
     * @return 找到的 {@link Class}对象
     */
    public static Class<?>[] getGenericTypeClassNames(Type type){
        List<Class<?>> classes = new ArrayList<>();
        String typeName = type.getTypeName();
        Pattern noGenericType = Pattern.compile("([A-Za-z_][\\w$]*(?:\\.[A-Za-z_][\\w$]*)*)\\s*<\\s*(.*[,.*]*)\\s*>\\s*");
        Matcher matcher = noGenericType.matcher(typeName);
        if (matcher.matches()) {
            String group = matcher.group(2);
            String[] classNamesArray = group.split(",");
            for (String className: classNamesArray) {
                String trimClassName = className.trim();
                try {
                    Class<?> aClass = Class.forName(trimClassName);
                    classes.add(aClass);
                } catch (ClassNotFoundException exception) {
                    // 如果他是泛型Class, 则获取他的原始版本即可
                    Matcher matcher1 = noGenericType.matcher(trimClassName);
                    if (matcher1.matches()){
                        // 获取
                        String componentType = matcher1.group(1);
                        try {
                            Class<?> aClass = Class.forName(componentType);
                            classes.add(aClass);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return classes.toArray(new Class[0]);
    }


    /**
     * Wrapper for {@link Class#forName(String)}, 如果找不到类则会返回null
     * @param className
     * @return
     */
    public static Class<?> loadClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
            return null;
        }
    }
    public static Class<?> loadClass(String className, boolean initialize){
        // 使用默认的sun.misc.Launcher$AppClassLoader (ClassLoader.getSystemClassLoader()) 进行加载。
        try {
            return Class.forName(className, initialize, ClassLoader.getSystemClassLoader());
        } catch (ClassNotFoundException exception) {
            return null;
        }
    }

    public static Class<?> loadClass(String className, boolean initialize, ClassLoader classLoader){
        try {
            return Class.forName(className, initialize, classLoader);
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static Class<?> loadClass(String className, boolean initialize, File... extClassPaths){
        final List<URI> classPathUris = Arrays.stream(extClassPaths).parallel().map(File::toURI).collect(Collectors.toList());
        final List<URL> classPathUrls = new ArrayList<>();
        for (URI uri :
                classPathUris) {
            try {
                final URL url = uri.toURL();
                classPathUrls.add(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                System.out.println("移除" + uri);
            }
        }
        final URL[] urls = classPathUrls.toArray(new URL[0]);
        try(URLClassLoader urlClassLoader = new URLClassLoader(urls)){
            final Class<?> aClass = Class.forName(className, initialize, urlClassLoader);
            return aClass;
        } catch (ClassNotFoundException | IOException exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
