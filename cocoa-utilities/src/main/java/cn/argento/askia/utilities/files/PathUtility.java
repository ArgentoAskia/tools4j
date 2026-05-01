package cn.argento.askia.utilities.files;

import cn.argento.askia.annotations.Utility;
import cn.argento.askia.utilities.algorithms.StringAlgorithmsUtility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.regex.Pattern;

@Utility("路径处理工具类")
public class PathUtility {

    public static final String WILDCARD_PATH_SINGLE = "*";    // 匹配单级路径
    public static final String WILDCARD_PATH_RECURSIVE = "**"; // 匹配多级路径
    private static class PathUniqueKey{
        public final String key;
        public final int index;

        private PathUniqueKey(String key, int index){
            this.key = key;
            this.index = index;
        }

        public static PathUniqueKey valueOf(String key, int index){
            return new PathUniqueKey(key, index);
        }

        @Override
        public String toString() {
            return key + "#" + index;
        }
    }
    public static List<Path> resolveWildcardPath(String path, boolean parallel) {
        // 将D://a/b/c/*/d/**/f/e.txt 完全扫描出来 所有匹配的地址
        // 1. 按照分隔符分割目录
        final String[] splitParts = path.split(Pattern.quote(File.separator));
        Map<PathUniqueKey, Path> queueMap = new HashMap<>();
        if (splitParts.length > 0){
            // 组合目录
            StringBuilder dir = new StringBuilder();
            int i = 0;
            for (; i < splitParts.length; i++) {
                if (WILDCARD_PATH_RECURSIVE.equals(splitParts[i]) || WILDCARD_PATH_SINGLE.equals(splitParts[i])){
                    break;
                }
                dir.append(splitParts[i]);
                dir.append(File.separator);
            }
            // 开始扫描
            final String baseScan = dir.toString();
            queueMap.put(PathUniqueKey.valueOf(baseScan, 0), Paths.get(baseScan));
            for (;i < splitParts.length; i++){
                if (WILDCARD_PATH_SINGLE.equals(splitParts[i])){
                    final Set<Map.Entry<PathUniqueKey, Path>> entries = queueMap.entrySet();
                    final Iterator<Map.Entry<PathUniqueKey, Path>> iterator = entries.iterator();
                    Map<PathUniqueKey, Path> addMap = new HashMap<>();
                    while (iterator.hasNext()){
                        final Map.Entry<PathUniqueKey, Path> next = iterator.next();
                        final PathUniqueKey key = next.getKey();
                        final Path value = next.getValue();
                        String s = key.key;
                        // 只扫描目录
                        if (s.endsWith(File.separator)){
                            try{
                                final Map<String, Path> stringPathMap = scanPath(value.toAbsolutePath().normalize().toString(), 1, 1);
                                // 去掉根目录
                                stringPathMap.remove(File.separator);
                                for (Map.Entry<String, Path> entry : stringPathMap.entrySet()){
                                    addMap.put(PathUniqueKey.valueOf(entry.getKey(), i), entry.getValue());
                                }
                            }
                            catch (IOException e){
                                // 抛出异常则忽略此路径
                            }
                        }
                        iterator.remove();
                    }
                    queueMap.putAll(addMap);
                }
                else if (WILDCARD_PATH_RECURSIVE.equals(splitParts[i])){
                    final Set<Map.Entry<PathUniqueKey, Path>> entries = queueMap.entrySet();
                    final Iterator<Map.Entry<PathUniqueKey, Path>> iterator = entries.iterator();
                    Map<PathUniqueKey, Path> addMap = new HashMap<>();
                    while (iterator.hasNext()){
                        final Map.Entry<PathUniqueKey, Path> next = iterator.next();
                        final PathUniqueKey key = next.getKey();
                        final Path value = next.getValue();
                        String s = key.key;
                        // 只扫描目录
                        if (s.endsWith(File.separator)){
                            try{
                                final Map<String, Path> stringPathMap = scanPath(value.toAbsolutePath().normalize().toString(), Integer.MAX_VALUE, 1);
                                stringPathMap.remove(File.separator);
                                for (Map.Entry<String, Path> entry : stringPathMap.entrySet()){
                                    addMap.put(PathUniqueKey.valueOf(entry.getKey(), i), entry.getValue());
                                }
                            }
                            catch (IOException e){
                                // 抛出异常则忽略此路径
                            }
                        }
                        iterator.remove();
                    }
                    queueMap.putAll(addMap);
                }
                else{
                    // 直接所有内容都拼接上目录
                    final Set<Map.Entry<PathUniqueKey, Path>> entries = queueMap.entrySet();
                    final Iterator<Map.Entry<PathUniqueKey, Path>> iterator = entries.iterator();
                    Map<PathUniqueKey, Path> addMap = new HashMap<>();
                    while (iterator.hasNext()){
                        final Map.Entry<PathUniqueKey, Path> next = iterator.next();
                        String part = splitParts[i];
                        final PathUniqueKey key = next.getKey();
                        final Path value = next.getValue();
                        String s = key.key;
                        final StringBuilder pathKey = new StringBuilder(s);
                        if (!s.endsWith(File.separator)){
                            pathKey.append(File.separator);
                        }
                        pathKey.append(part);
                        final Path resolveValue = value.resolve(part);
                        if (Files.exists(resolveValue)){
                            addMap.put(PathUniqueKey.valueOf(pathKey.toString(), i), resolveValue);
                        }
                        iterator.remove();
                    }
                    queueMap.putAll(addMap);
                }
            }
            return new ArrayList<>(queueMap.values());
        }
        return null;
    }
    public static List<Path> resolveWildcardPath(String path) {
        return resolveWildcardPath(path, false);
    }

    public static List<Path> resolveWildcardFile(Path filePath, boolean parallel){
        List<Path> result = new ArrayList<>();
        return result;
    }

    public static List<Path> resolveWildcardFile(Path filePath){
        return resolveWildcardFile(filePath, false);
    }



    public static boolean isWildcardPath(Path path){
        final Path parent = path.toAbsolutePath().normalize().getParent();
        // 根目录没有通配符
        if (parent == null){
            return false;
        }
        final String s = parent.toString();
        return StringAlgorithmsUtility.KMP(WILDCARD_PATH_RECURSIVE.toCharArray(), s.toCharArray()) >= 0 || s.contains(WILDCARD_PATH_SINGLE);
    }
    public static final String WILDCARD_FILE_SINGLE = "?";
    public static final String WILDCARD_FILE_RECURSIVE = "*";

    public static boolean isWildcardFile(Path path){
        final Path fileName = path.getFileName();
        if (fileName == null){
            return false;
        }
        return fileName.toString().contains(WILDCARD_FILE_SINGLE) || fileName.toString().contains(WILDCARD_FILE_RECURSIVE);
    }

    private static Map<String, Path> scanPath(String pathStr, int deep, int type) throws IOException {
        final Path path = Paths.get(pathStr);
        if (Files.notExists(path)){
            // 不存在
            throw new FileNotFoundException(path.toAbsolutePath().normalize().toString());
        }
        Map<String, Path> pathRet = new HashMap<>();
        Files.walkFileTree(path,
                EnumSet.noneOf(FileVisitOption.class),
                deep,
                new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (type == 0){
                    if (Files.isDirectory(file)){
                        return preVisitDirectory(file, attrs);
                    }
                    // 处理每一个文件
                    final Path resolve = path.relativize(file);
//                System.out.println("File: " + file + ", relativize = " + resolve);
                    pathRet.put(resolve.toString(), file);
                }
                else if (type > 0){
                    if (Files.isDirectory(file)){
                        return preVisitDirectory(file, attrs);
                    }
                }
                else{
                    if (!Files.isDirectory(file)) {
                        // 处理每一个文件
                        final Path resolve = path.relativize(file);
//                System.out.println("File: " + file + ", relativize = " + resolve);
                        pathRet.put(resolve.toString(), file);
                    }
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
//                System.out.println("Entering: " + dir);
                if (type >= 0){
                    final Path resolve = path.relativize(dir);
                    pathRet.put(resolve.toString() + File.separator, dir);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                // 记录失败原因，例如权限不足
                System.err.println("Failed: " + file + " -> " + exc);
                return FileVisitResult.CONTINUE;   // 继续遍历其他文件
            }
        });
        return pathRet;
    }

    public static void main(String[] args) throws IOException {
        System.out.println(resolveWildcardPath("E:\\OpenSourceProjects\\tools4j\\cocoa-annotations\\src\\**\\*\\supports"));
//        System.out.println(CollectionUtility.toBeautifulString(scanPath("E:\\OpenSourceProjects\\tools4j\\cocoa-annotations\\src\\main\\java\\cn\\argento\\askia\\supports", Integer.MAX_VALUE,1), true, true, true));
    }
}
