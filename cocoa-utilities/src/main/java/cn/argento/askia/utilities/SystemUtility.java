package cn.argento.askia.utilities;

import cn.argento.askia.exceptions.errors.lang.SystemError;
import cn.argento.askia.utilities.algorithms.StringAlgorithmsUtility;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SystemUtility {


    public static void environments(){
        environments(System.out);
    }

    // TODO: 2024/5/4  格式化=false

    /**
     * println the SYSTEM levels variables for the specified writeOut Object.
     * <p>for more information, you can touch with {@link System#getenv()} and {@link System#getProperties()}
     *
     * @param writeOut Output Object to output all these environments
     */
    public static void environments(PrintStream writeOut){
        final Map<String, String> envs = System.getenv();
        final Properties properties = System.getProperties();
        writeOut.println("=============== system environments ===============");
        envs.forEach((s, s2) -> writeOut.println(s + " = " + s2));
        writeOut.println("=============== system properties ===============");
        System.out.println();
        properties.forEach((o, o2) -> writeOut.println(o + "=" + o2));
        writeOut.println("=============== END OF LINE =============");
    }

    /**
     * 该方法用于判断当前系统是否属于 {@code Windows NT} 族, 如 {@code Windows 7}、 {@code Windows 8}等
     *
     * @return 如果当前系统是Windows NT族, 则返回true，否则返回false
     */
    public static boolean isWindowsNTOS(){
        // bug here!
        // if “OS” is not exist ,this method will throw NPE
        // this is not we want!
        final String os = System.getenv("OS");
        if (os != null && os.equalsIgnoreCase("Windows_NT")){
            return true;
        }
        // "os.name" creates by jvm!
        return System.getProperty("os.name").toLowerCase().contains("window") ||
                System.getProperty("os.name").toLowerCase().contains("win");
    }

    public static boolean isUnixClassesOS(){
        String osName = System.getProperty("os.name").toLowerCase();
        return osName.contains("nix") || osName.contains("nux") || osName.contains("aix");
    }
    public static boolean isMacOS(){
        String osName = System.getProperty("os.name").toLowerCase();
        return osName.contains("mac");
    }
    public static String getOSArch(){
        return System.getProperty("os.arch");
    }

    public static String getOSVersion(){
        return System.getProperty("os.version");
    }

    public static String getUserDir(){
        return System.getProperty("user.dir");
    }

    public static String getCurrentRunningJDKDir(){
        final String jdkDir = System.getProperty("java.home");
        return jdkDir.substring(0, jdkDir.length() - 4);
    }
    public static String getTmpPath(){
        return System.getProperty("java.io.tmpdir");
    }

    public static String getLinuxReleaseMessage(){
        if (!isUnixClassesOS()) {
            throw new UnsupportedOperationException("OS is not a Linux OS!");
        }
        return getLinuxReleaseMessage0();
    }


    private static String getLinuxReleaseMessage0() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[]{"cat", "/etc/*-release"});
            // 读取命令的输出
            final InputStream inputStream = process.getInputStream();
            final byte[] bytes = IOStreamUtility.readAllBytes(inputStream);
            final String releaseMessages = new String(bytes);
            inputStream.close();
            process.destroy();
            return releaseMessages;
        } catch (IOException e){
            e.printStackTrace();
            return "Unknown Linux release";
        }
    }

    private static String getMacReleaseMessage0(){
        String version = "";
        try {
            Process process = Runtime.getRuntime().exec("system_profiler SPSoftwareDataType");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            Pattern pattern = Pattern.compile("System Version: (.+)");
            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    version = matcher.group(1);
                    break;
                }
            }
            reader.close();
            process.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return version.trim();
    }

    public static String getMacReleaseMessage(){
        if (!isMacOS()) {
            throw new UnsupportedOperationException("OS is not a Mac OS!");
        }
        return getMacReleaseMessage0();
    }


    private static String getOriginalClassPathText(){
        return System.getProperty("java.class.path", "");
    }

    private static String getOriginalLibraryPathText(){
        return System.getProperty("java.library.path", "");
    }
    private static String[] splitPathTextToArray(String pathText,
                                                 Function<String, String> functionForEachPath,
                                                 String errorMsg){
        // this must not happen, but for safety，we just check about it！
        if (pathText.equals("")){
            throw new SystemError(errorMsg);
        }
        if (functionForEachPath == null){
            // so we just split the Text!
            return pathText.split(";");
        }
        final String[] splitClassPathTxt = pathText.split(";");
        return ArrayUtility.computeForeach(splitClassPathTxt, String.class, functionForEachPath);
    }

    // 将长classpath转为短classpath
    public static String[] getClassPath(Function<String, String> functionForEachClasspath){
        final String classPathText = getOriginalClassPathText();
        return splitPathTextToArray(classPathText, functionForEachClasspath,
                "no CLASSPATHS? are you sure? so how you load this class ???");
    }

    // 格式化classpath
    public static String getClassPath(MessageFormat formatter){
        final String classPathText = getOriginalClassPathText();
        return formatter == null? classPathText : formatter.format(classPathText);
    }

    // 获取classpath
    public static String getClassPath(){
        final String[] classPaths = getClassPath((Function<String, String>) null);
        StringBuilder stringBuilder = new StringBuilder();
        ArrayUtility.foreach(classPaths, (classPath) ->{
            stringBuilder.append(classPath).append(System.lineSeparator());
        });
        return stringBuilder.toString();
    }

    // 过滤classpath
    public static List<String> getClassPath(Predicate<String> condition){
        String originalClassPathText = getOriginalClassPathText();
        String[] split = originalClassPathText.split(";");
        return Arrays.stream(split).filter(condition).collect(Collectors.toList());
    }


    public static String[] getLibraryPath(Function<String, String> functionForEachLibraryPath){
        final String originalLibraryPathText = getOriginalLibraryPathText();
        return splitPathTextToArray(originalLibraryPathText, functionForEachLibraryPath,
                "no LiBRARY PATHS? are you sure? so how you use the system classes ???");
    }

    public static String getLibraryPath(MessageFormat formatter){
        final String originalLibraryPathText = getOriginalLibraryPathText();
        return formatter == null? originalLibraryPathText : formatter.format(originalLibraryPathText);
    }

    public static String getLibraryPath(){
        final String[] libraryPaths = getLibraryPath((Function<String, String>) null);
        StringBuilder stringBuilder = new StringBuilder();
        ArrayUtility.foreach(libraryPaths, (libraryPath) ->{
            stringBuilder.append(libraryPath).append(System.lineSeparator());
        });
        return stringBuilder.toString();
    }


    // TODO: 2024/5/12  Linux env and Windows Env!
    public static String getSystemPathEnv(){
        return null;
    }

    public static Map<String, String> getSystemEnvironments(){
        Map<String, String> envs = new HashMap<>();
        String cmd = "";
        String separator = "\r\n";
        if (isWindowsNTOS()){
            cmd = "set";
        }
        else if (isUnixClassesOS()){
            cmd = "printenv";
        }
        else if (isMacOS()){
            cmd = "printenv";
        }
        if (cmd.equalsIgnoreCase("")){
            return null;
        }
        final String set = exec(cmd);
        final String[] split = Objects.requireNonNull(set).split(separator);
        for (String s : split) {
            final String[] keyValue = s.split("=");
            envs.put(keyValue[0], keyValue[1]);
        }
        return envs;
    }

    public static void setSystemEnvironment(String varName,
                                            String varValue){

    }

    public static void setUserEnvironment(String varName,
                                          String varValue){

    }

    protected static String exec(String command){
        final Runtime runtime = Runtime.getRuntime();
        try {
            final Process exec = runtime.exec("cmd /C " + command);
            final InputStream inputStream = exec.getInputStream();
            final byte[] bytes = IOStreamUtility.readAllBytes(inputStream);
            return new String(bytes, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) {
//        environments();
//        System.out.println(getCurrentRunningJDKDir());
//        System.out.println(getClassPath());
//        final String set = exec("set");
//        System.out.println(set);
//        System.out.println(getSystemEnvironments());
        StdOut.print(new char[]{'a', '2'});
        System.out.println(getMavenProjectRoot(new File("yumitoy-utilities/src/main/java/com/yumitoy/utilities/SystemUtility.java")));
        System.out.println(getMavenProjectModules(new File("yumitoy-utilities/src/main/java/com/yumitoy/utilities/SystemUtility.java")));
    }

    public static class StdOut{

        private StdOut(){}

        public static void print(Object o){
            System.out.print(o);
        }

        public static void print(char[] chars){
            System.out.println(chars);
        }

        public static void println(){
            System.out.println();
        }

        public static void println(Object o){
            System.out.println(o);
        }

        public static void println(char[] a){
            System.out.println(a);
        }

        // print Delimiter line
        public static <T> void printDln(T[] o){
            final String s = ArrayUtility.toDelimiterString(o);
            System.out.println(s);
        }

        public static void main(String[] args) {
            String[] st = {
                    "cn.argento.askia.bean.Person{name='Neo', age=45, country='USA'}",
                    "cn.argento.askia.bean.Person{name='Alex', age=20, country='UK'}",
                    "cn.argento.askia.bean.Person{name='Sebastian', age=40, country='FR'}"};
            printDln(st);
        }
    }

    public static class StdIn{
        private static final Scanner stdInScanner;
        static {
            stdInScanner = new Scanner(System.in);
        }
        private StdIn(){

        }
        public static String readLineBlock(){
            if(stdInScanner.hasNextLine()){
                return stdInScanner.nextLine();
            }
            throw new Error("这个地方不可能也绝对不能发生！");
        }

    }


    // Maven工程相关的扫描

    /**
     * 获取Maven工程的顶级工程(父工程)目录
     *
     * <b>注意：此API仅限Maven项目使用, 非Maven项目失效！</b>
     *
     *
     * @param path
     * @return
     */
    public static File getMavenProjectRoot(File path){
        File mavenRoot = null;
        if (!path.isDirectory()){
            // 不是目录则获取目录
            path = path.getParentFile();
        }
        path = path.getAbsoluteFile();
        // 如果用户提交的目录本身就是根目录, 则直接返回
        mavenRoot = _getMavenProjectRoot(path, mavenRoot);
        return mavenRoot;
    }
    private static File _getMavenProjectRoot(File path, File traverse){
        // 如果用户提供的目录有pom.xml，则记录
        if (isRoot(path.toPath())){
            // 如果是根则直接返回即可
            return traverse;
        }
        if (hasPomFile(path)){
            // 替换maveRoot，设置下一个循环
            return _getMavenProjectRoot(path.getParentFile(), path);
        }
        else{
            return _getMavenProjectRoot(path.getParentFile(), traverse);
        }
    }
    private static boolean hasPomFile(File path){
        File pomFile = new File(path, "pom.xml");
        return pomFile.exists();
    }
    private static boolean isRoot(Path path) {
        return path != null && path.getParent() == null;
    }

    // todo 这个方法有问题！
    public static List<File> getMavenProjectModules(File path){
        File mavenRoot = null;
        if (!path.isDirectory()){
            // 不是目录则获取目录
            path = path.getParentFile();
        }
        path = path.getAbsoluteFile();
        List<File> list = new ArrayList<>();
        // 如果用户提交的目录本身就是根目录, 则直接返回
        _getMavenProjectRootList(path, mavenRoot, list);
        return list;
    }
    private static void _getMavenProjectRootList(File path, File traverse, List<File> ret){
        // 如果用户提供的目录有pom.xml，则记录
        if (isRoot(path.toPath())){
            // 如果是根则直接返回即可
            return;
        }
        if (hasPomFile(path)){
            // 替换maveRoot，设置下一个循环
            ret.add(path);
            _getMavenProjectRootList(path.getParentFile(), path, ret);
        }
        else{
            _getMavenProjectRootList(path.getParentFile(), traverse, ret);
        }
    }

    // 项目、项目依赖等相关

    /**
     * 此方法能够获取classPath中特定的依赖包.
     * <p>方法能够判断当前项目中是否引入了某个第三方包.
     * @param groupId 对应maven中的groupId, 如果提供的groupId为空，则采用artifactId + version进行搜索
     * @param artifactId 对应maven中的artifactId, 如果提供的artifactId为空, 则采用groupId + version进行搜索
     * @param version 对应maven中的version, 如果提供的version为空, 则采用groupId + artifactId进行搜索
     * @return 返回相关的包所在的位置
     * @since 2025.12.24
     */
    public static List<String> getDependencies(String groupId, String artifactId, String version){
        if (StringUtility.isBlank(groupId) && StringUtility.isBlank(artifactId) && StringUtility.isBlank(version)){
            throw new IllegalArgumentException("groupId, artifactId, version至少有一个不为null");
        }
        // 处理GroupId,
        List<String> resultMap = new ArrayList<>();
        if (!StringUtility.isBlank(groupId)){
            // 提供的GroupId是 com.xyz.abc模式, 我们要替换掉
            groupId = groupId.replace(".", File.separator);
            String finalGroupId = groupId;
            _getClassPathByAnyIdInternal(finalGroupId, resultMap);
        }
        // 处理artifactId
        resultMap = _continueMatchClassPath(artifactId, resultMap);

        // 处理Version
        resultMap = _continueMatchClassPath(version, resultMap);

        return resultMap;
    }
    // 继续Match ClassPath所用
    private static List<String> _continueMatchClassPath(String artifactIdOrVersion, List<String> resultMap) {
        if (!StringUtility.isBlank(artifactIdOrVersion)){
            if (resultMap.size() > 0){
                // 如果resultMap中有内容，则从resultMap中再次匹配
                resultMap = resultMap.parallelStream().filter(s -> {
                    //  过滤artifactId
                    char[] versionChars = artifactIdOrVersion.toCharArray();
                    char[] classPathChars = s.toCharArray();
                    // KMP 算法模式匹配ClassPath 和 artifactId
                    return StringAlgorithmsUtility.KMP(versionChars, classPathChars) != -1;
                }).collect(Collectors.toList());
            }
            else{
                // 第一次匹配直接按照getClassPath()进行
                _getClassPathByAnyIdInternal(artifactIdOrVersion, resultMap);
            }
        }
        return resultMap;
    }
    // 获取ClassPath通过Id
    private static void _getClassPathByAnyIdInternal(String Id, List<String> resultMap) {
        List<String> matchClassPath = getClassPath((Predicate<String>) s -> {
            String classPath = s.replace("/", File.separator);
            // KMP匹配
            char[] finalGroupIdChars = Id.toCharArray();
            char[] classPathChars = classPath.toCharArray();
            int kmp = StringAlgorithmsUtility.KMP(finalGroupIdChars, classPathChars);
            return kmp != -1;
        });
        resultMap.addAll(matchClassPath);
    }



    // 桌面、屏幕、系统事件、热键、截图等等
}
