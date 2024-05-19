package cn.argento.askia.utilities;

import cn.argento.askia.exceptions.errors.SystemError;

import javax.swing.text.Style;
import java.io.*;
import java.lang.reflect.Array;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SystemUtility {


    public static void environments(){
        environments(System.out);
    }

    // TODO: 2024/5/4  格式化？
    public static void environments(OutputStream writeOut){
        final Map<String, String> envs = System.getenv();
        final Properties properties = System.getProperties();
        envs.forEach(new BiConsumer<String, String>() {
            @Override
            public void accept(String s, String s2) {
                System.out.println(s + " = " + s2);
            }
        });
        System.out.println("==============");
        properties.list(System.out);
    }

    /**
     * 该方法用于判断当前系统是否属于 {@code Windows NT} 族, 如 {@code Windows 7}、 {@code Windows 8}等
     *
     * @return
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

    public static String[] getClassPath(Function<String, String> functionForEachClasspath){
        final String classPathText = getOriginalClassPathText();
        return splitPathTextToArray(classPathText, functionForEachClasspath,
                "no CLASSPATHS? are you sure? so how you load this class ???");
    }

    public static String getClassPath(MessageFormat formatter){
        final String classPathText = getOriginalClassPathText();
        return formatter == null? classPathText : formatter.format(classPathText);
    }

    public static String getClassPath(){
        final String[] classPaths = getClassPath((Function<String, String>) null);
        StringBuilder stringBuilder = new StringBuilder();
        ArrayUtility.foreach(classPaths, (classPath) ->{
            stringBuilder.append(classPath).append(System.lineSeparator());
        });
        return stringBuilder.toString();
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


    public static void main(String[] args) {
//        environments();
//        System.out.println(getCurrentRunningJDKDir());
        System.out.println(getClassPath());
    }
}
