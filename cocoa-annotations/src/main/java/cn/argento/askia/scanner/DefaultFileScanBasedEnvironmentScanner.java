package cn.argento.askia.scanner;

import cn.argento.askia.supports.environment.FileScanBasedEnvironmentBean;
import cn.argento.askia.utilities.algorithms.StringAlgorithmsUtility;
import cn.argento.askia.utilities.collection.CollectionUtility;
import cn.argento.askia.utilities.files.PathUtility;
import cn.argento.askia.utilities.lang.StringUtility;
import cn.argento.askia.utilities.text.FormatUtility;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 默认的扫描环境扫描器支持.
 * <p>该扫描器属于重量级扫描器</p>
 */
public class DefaultFileScanBasedEnvironmentScanner implements Scanner<FileScanBasedEnvironmentBean, Map<String, Map<String, List<Path>>>> {

    @Override
    public Map<String, Map<String, List<Path>>> scan(FileScanBasedEnvironmentBean param) {
        // package  ==> class name ==> file name
        Map<String, Map<String, List<Path>>> packageNameClassNamePathMap = new HashMap<>();

        final List<String> classpathList = param.getClasspathList();
        // 并行处理通配符路径
        final List<String> pathNameList = classpathList.parallelStream()
                .filter(PathUtility::isWildcardPath)      // 过滤非 WildcardPath 路径
                .map(PathUtility::resolveWildcardPath)     // 处理通配符遍历实际路径
                .flatMap(List::stream)         // Stream<List<Path>> ==> Stream<Path>
                .distinct()         // 去重
                .map(path -> path.toAbsolutePath().normalize().toString())      // 转为字符串
                .collect(Collectors.toList());  // 收集

        // 处理PackageList
        final List<String> packageList = param.getPackageList();
        // 并行处理packageName
        final List<String> packageNameList = packageList.parallelStream().map(s -> {
            // 如果是包名则将其换成目录
            String packageName = "";
            if (FormatUtility.matchPackage(s, true)) {
                packageName =  s.replace('.', File.separatorChar);
            } else {
                packageName = s;
            }
            if (!packageName.startsWith(File.separator)) {
                packageName = File.separator + packageName;
            }
            return packageName;
        }).collect(Collectors.toList());

        // 笛卡尔积得到带包名通配符的路径
        List<String> pathPackageNameWildcardList = StringUtility.cartesianProduct(pathNameList, packageNameList);
        // 去除假路径
        List<String> pathPackageNameWildcardListFilter = pathPackageNameWildcardList.parallelStream()
                .filter(PathUtility::isWildcardPath)
                .map(PathUtility::resolveWildcardPath)
                .flatMap(List::stream)         // Stream<List<Path>> ==> Stream<Path>
                .distinct()         // 去重
                .peek(path -> {
                    for (String pathName : pathNameList) {
                        Path pathNamePath = Paths.get(pathName);
                        if (PathUtility.isParent(pathNamePath, path)) {
                            Path relativize = pathNamePath.relativize(path);
                            String string = relativize.toString();
                            packageNameClassNamePathMap.putIfAbsent(string, new HashMap<>());
                        }
                    }
                })
                .map(path -> {
                    String string = path.toAbsolutePath().normalize().toString();
                    if (!string.endsWith(File.separator)) {
                        string = string + File.separator;
                    }
                    return string;
                })      // 转为字符串
                .collect(Collectors.toList());  // 收集
        // 寻找文件并合并
        final List<String> classNameList = param.getClassNameList();
        // 先将文件名进行分类
        final List<String> wildcardClassNameList = new ArrayList<>();
        final List<String> normalClassNameList = new ArrayList<>();
        for (String className : classNameList){
            // 文件名包含通配符
            if (className.contains(PathUtility.WILDCARD_FILE_RECURSIVE) ||
                    className.contains(PathUtility.WILDCARD_FILE_SINGLE)){
                wildcardClassNameList.add(className);
            }
            else{
                normalClassNameList.add(className);
            }
        }
        // 笛卡尔积路径(正常路径)
        final List<String> normalFullPathList = StringUtility.cartesianProduct(pathPackageNameWildcardListFilter, normalClassNameList);
        final List<String> wildcardFullPathList = StringUtility.cartesianProduct(pathPackageNameWildcardListFilter, wildcardClassNameList);
        // 处理文件是否存在
        List<Path> finalFullPathList = new ArrayList<>();
        for (String normalFullPath : normalFullPathList){
            final Path path = Paths.get(normalFullPath + ".class");
            if (Files.exists(path)){
                finalFullPathList.add(path);
            }
        }
        // 处理通配符文件
        for (String wildcardFullPath : wildcardFullPathList){
            final List<Path> paths;
            try {
                System.out.println(wildcardFullPath);
                paths = PathUtility.resolveWildcardFile(wildcardFullPath + ".class");
                if (!paths.isEmpty()){
                    finalFullPathList.addAll(paths);
                }
            } catch (IOException e) {
                // 忽略此Path
            }
        }

        // 去重 + 判断结尾
        Set<Path> finalClassPathList = new HashSet<>();
        for (Path path : finalFullPathList){
            if (path.toString().endsWith("class")) {
                // 是否以class结束, 如果是则加入class
                finalClassPathList.add(path);
            }
        }


        // 创建一个完整的映射表
        Set<String> packageKeySet = packageNameClassNamePathMap.keySet();
        for (Path path : finalClassPathList){
            String classFilePath = path.toAbsolutePath().normalize().toString();
            Optional<String> first = packageKeySet.parallelStream()
                    .filter(s -> {
                        char[] text = classFilePath.toCharArray();
                        int kmp = StringAlgorithmsUtility.KMP(s.toCharArray(), text);
                        return kmp >= 0 && kmp < text.length;
                    } )
                    .findFirst();
            if (first.isPresent()){
                String s = first.get();
                String[] split = classFilePath.split(Pattern.quote(s));
                // 一定会有两部分
                if (split.length == 2){
                    String classFileSimpleNameWithSuffix = split[1];
                    String rootPath = split[0];
                    if (rootPath.endsWith(File.separator)){
                        rootPath = rootPath.substring(0,rootPath.length() - 1);
                    }
                    if (classFileSimpleNameWithSuffix.startsWith(File.separator)){
                        classFileSimpleNameWithSuffix = classFileSimpleNameWithSuffix.substring(1);
                    }
                    String classFileSimpleName = classFileSimpleNameWithSuffix.substring(0, classFileSimpleNameWithSuffix.lastIndexOf('.'));
                    Map<String, List<Path>> stringListMap = packageNameClassNamePathMap.getOrDefault(s, new HashMap<>());
                    packageNameClassNamePathMap.put(s, stringListMap);
                    List<Path> paths = stringListMap.computeIfAbsent(classFileSimpleName, s1 -> new ArrayList<>());
                    paths.add(path);
                }
            }
        }

        // 通过映射表加载类
        return packageNameClassNamePathMap;
    }

    public static void main(String[] args) {
        FileScanBasedEnvironmentBean build = FileScanBasedEnvironmentBean.builder()
                .addClassPath("G:\\project\\java\\cocoa-java\\cocoa-annotations\\target\\classes")
                .addPackage("cn.argento.askia.*")
                .addPackage("cn.argento.askia")
                .addClassName("*")
                .addClassName("AnnotationProcessingEnvironmentBean")
                .build();
        DefaultFileScanBasedEnvironmentScanner scanner = new DefaultFileScanBasedEnvironmentScanner();
        Map<String, Map<String, List<Path>>> scan = scanner.scan(build);
        System.out.println(CollectionUtility.toBeautifulString(scan));
    }


    private void testPath(){
        //        Path path = Paths.get("G:\\project\\java\\cocoa-java\\cocoa-annotations\\target\\classes");
        Path path = Paths.get("/etc/sys/classes/");
        int nameCount = path.getNameCount();
        System.out.println(nameCount);
        Path root = path.getRoot();
        System.out.println(root);
        for (Path p : path) {
            System.out.println(p);
        }
    }
}
