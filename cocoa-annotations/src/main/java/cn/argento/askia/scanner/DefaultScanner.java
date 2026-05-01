package cn.argento.askia.scanner;

import cn.argento.askia.annotations.AliasFor;
import cn.argento.askia.supports.beans.ScannedEnvironmentBean;
import cn.argento.askia.utilities.files.PathUtility;
import cn.argento.askia.utilities.lang.StringUtility;
import cn.argento.askia.utilities.text.FormatUtility;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.text.Format;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DefaultScanner implements Scanner<ScannedEnvironmentBean, List<Class<?>>>{

    @Override
    public List<Class<?>> scan(ScannedEnvironmentBean param) {
        final List<String> classpathList = param.getClasspathList();
        // 并行处理通配符路径
        final List<String> pathNameList = classpathList.parallelStream()
                .filter(s -> PathUtility.isWildcardPath(Paths.get(s)))      // 过滤非 WildcardPath 路径
                .map(PathUtility::resolveWildcardPath)     // 处理通配符遍历实际路径
                .flatMap(List::stream)         // Stream<List<Path>> ==> Stream<Path>
                .distinct()         // 去重
                .map(path -> path.toAbsolutePath().normalize().toString())      // 转为字符串
                .collect(Collectors.toList());  // 收集
        final List<String> packageList = param.getPackageList();
        // 并行处理packageName
        Map<String, List<String>> packageNameClassPathMap = new HashMap<>();
        final List<String> packageNameList = packageList.parallelStream().map(s -> {
            if (FormatUtility.matchPackage(s, true)) {
                return s.replace('.', File.separatorChar);
            } else {
                return s;
            }
        }).collect(Collectors.toList());

        // todo 重构此代码
        // 笛卡尔积得到带包名通配符的路径
        final List<String> pathPackageNameWildcardList = StringUtility.cartesianProduct(pathNameList, packageNameList);
        // 去除假路径
        List<String> pathPackageNameWildcardListFilter = pathPackageNameWildcardList.parallelStream()
                .filter(s -> PathUtility.isWildcardPath(Paths.get(s)))
                .map(PathUtility::resolveWildcardPath)
                .flatMap(List::stream)         // Stream<List<Path>> ==> Stream<Path>
                .distinct()         // 去重
                .map(path -> path.toAbsolutePath().normalize().toString())      // 转为字符串
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
            final Path path = Paths.get(normalFullPath);
            if (Files.exists(path)){
                finalFullPathList.add(path);
            }
        }
        // 处理通配符文件
        for (String wildcardFullPath : wildcardFullPathList){
            final Path path = Paths.get(wildcardFullPath);
            final List<Path> paths = PathUtility.resolveWildcardFile(path);
            if (!paths.isEmpty()){
                finalFullPathList.addAll(paths);
            }
        }

        List<Path> finalClassPathList = new ArrayList<>();
        for (Path path : finalFullPathList){
            if (path.toString().endsWith("class")) {
                // 是否以class结束, 如果是则加入class
                finalClassPathList.add(path);
            }
        }

        // 加载类
        List<Class<?>> classes = new ArrayList<>();
        for (Path path : finalClassPathList){

        }
        return null;
    }
}
