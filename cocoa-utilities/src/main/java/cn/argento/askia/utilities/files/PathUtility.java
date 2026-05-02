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

    public static List<Path> resolveWildcardFile(String filePath, boolean parallel) throws IOException {
        List<Path> result = new ArrayList<>();
        int i = filePath.lastIndexOf(File.separator);
        String basePath = filePath.substring(0, i);
        String fileName = filePath.substring(i + 1);
        Path basePathObject = Paths.get(basePath);
        Files.walkFileTree(basePathObject, EnumSet.noneOf(FileVisitOption.class),1,new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Objects.requireNonNull(file);
                Objects.requireNonNull(attrs);
                String string = file.getFileName().toString();
                if (PathUtility.wildcardFileMatch(fileName, string)){
                    result.add(file);
                }
                return FileVisitResult.CONTINUE;
            }
        });
        return result;
    }

    public static boolean wildcardFileMatch(String pattern, String str) {
        if (pattern == null || str == null) {
            return false;
        }

        int pLen = pattern.length();
        int sLen = str.length();

        int p = 0; // pattern 指针
        int s = 0; // string 指针
        int starIdx = -1; // 最后一次出现 '*' 的位置
        int match = 0;    // 上一次匹配 '*' 时，str 对应的位置

        while (s < sLen) {
            // 字符匹配 或 pattern 当前为 '?'
            if (p < pLen && (pattern.charAt(p) == '?' || pattern.charAt(p) == str.charAt(s))) {
                p++;
                s++;
            }
            // pattern 当前为 '*'
            else if (p < pLen && pattern.charAt(p) == '*') {
                starIdx = p;   // 记录 '*' 的位置
                match = s;     // 记录当前 str 位置
                p++;           // 跳过 '*'，先尝试匹配 0 个字符
            }
            // 上次遇到过 '*'，需要回溯
            else if (starIdx != -1) {
                p = starIdx + 1; // pattern 指针回到 '*' 的下一个位置
                match++;         // str 匹配位置向前移动一个字符
                s = match;
            }
            // 不匹配且没有 '*' 可回溯
            else {
                return false;
            }
        }

        // 检查 pattern 尾部是否都是 '*'（剩余的 '*' 可匹配空串）
        while (p < pLen && pattern.charAt(p) == '*') {
            p++;
        }
        return p == pLen;
    }

    public static List<Path> resolveWildcardFile(String filePath) throws IOException {
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
    public static boolean isWildcardPath(String path){
        int i = path.lastIndexOf(File.separator);
        if (i == -1){
            throw new InvalidPathException(path, "该路径字符串没有路径分割器, 可能不是合法的路径");
        }
        String parentPath = path.substring(0, i);
        return StringAlgorithmsUtility.KMP(WILDCARD_PATH_RECURSIVE.toCharArray(), parentPath.toCharArray()) >= 0 || parentPath.contains(WILDCARD_PATH_SINGLE);
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


    /**
     * 判断 potentialParent 是否是 potentialChild 的祖先目录（父或更上层）。
     *
     * @param potentialParent 可能的父路径
     * @param potentialChild  可能的子路径
     * @return true 如果 potentialParent 是 potentialChild 的祖先，且两者不相同
     * @since 2026.5.2
     */
    public static boolean isParent(Path potentialParent, Path potentialChild) {
        Objects.requireNonNull(potentialParent, "parent path must not be null");
        Objects.requireNonNull(potentialChild, "child path must not be null");

        // 规范化路径（消除 . 和 .. 等）
        Path parent = potentialParent.toAbsolutePath().normalize();
        Path child = potentialChild.toAbsolutePath().normalize();

        // 根路径相同且子路径以父路径开头
        return !parent.equals(child) && child.startsWith(parent);
    }

    // 字符串版本
    public static boolean isParent(String parentPath, String childPath) {
        return isParent(Paths.get(parentPath), Paths.get(childPath));
    }

    public static void main(String[] args) throws IOException {
        System.out.println(resolveWildcardPath("E:\\OpenSourceProjects\\tools4j\\cocoa-annotations\\src\\**\\*\\supports"));
//        System.out.println(CollectionUtility.toBeautifulString(scanPath("E:\\OpenSourceProjects\\tools4j\\cocoa-annotations\\src\\main\\java\\cn\\argento\\askia\\supports", Integer.MAX_VALUE,1), true, true, true));
    }
}
