package cn.argento.askia.utilities.classes;

import cn.argento.askia.annotations.Utility;
import cn.argento.askia.utilities.files.FileUtility;
import cn.argento.askia.utilities.lang.StringUtility;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;

/**
 * URL、URI增强
 */

@Utility("资源工具类")
public class ResourceUtility {

    private ResourceUtility() {
        throw new IllegalAccessError("ResourceUtility为工具类, 无法创建该类的对象");
    }

    //  演示常用资源的获取方式
    public static void main(String[] args) throws URISyntaxException, IOException {
        // 在IDE中输出：file:/E:/OpenSourceProjects/yumitoy-backend/yumitoy-utilities/target/classes/com/yumitoy/utilities/
        // 在Jar(SpringBoot插件)中输出：jar:file:/E:/OpenSourceProjects/yumitoy-backend/yumitoy-utilities/target/yumitoy-utilities-1.0-SNAPSHOT.jar!/BOOT-INF/classes!/com/yumitoy/utilities/
        System.out.println("ResourceUtility.class.getResource: " + ResourceUtility.class.getResource(""));
        // 在IDE中输出：file:/E:/OpenSourceProjects/yumitoy-backend/yumitoy-utilities/target/classes/
        // 在Jar(SpringBoot插件)中输出：jar:file:/E:/OpenSourceProjects/yumitoy-backend/yumitoy-utilities/target/yumitoy-utilities-1.0-SNAPSHOT.jar!/BOOT-INF/classes!/
        System.out.println("ResourceUtility.class.getResource /: " + ResourceUtility.class.getResource("/"));
        // 在IDE中输出：file:/E:/OpenSourceProjects/yumitoy-backend/yumitoy-utilities/target/classes/
        // 在Jar(SpringBoot插件)中输出：jar:file:/E:/OpenSourceProjects/yumitoy-backend/yumitoy-utilities/target/yumitoy-utilities-1.0-SNAPSHOT.jar!/BOOT-INF/classes!/
        System.out.println("ResourceUtility.class.getClassLoader().getResource: " + ResourceUtility.class.getClassLoader().getResource(""));
        // 在IDE中输出：null
        // 在Jar(SpringBoot插件)中输出：jar:file:/E:/OpenSourceProjects/yumitoy-backend/yumitoy-utilities/target/yumitoy-utilities-1.0-SNAPSHOT.jar!/BOOT-INF/classes!/
        System.out.println("ResourceUtility.class.getClassLoader().getResource /: " +ResourceUtility.class.getClassLoader().getResource("/"));
        // 在IDE中输出：file:/E:/OpenSourceProjects/yumitoy-backend/yumitoy-utilities/target/classes/
        // 在Jar(SpringBoot插件)中输出：jar:file:/E:/OpenSourceProjects/yumitoy-backend/yumitoy-utilities/target/yumitoy-utilities-1.0-SNAPSHOT.jar!/BOOT-INF/classes!/
        System.out.println("ResourceUtility.class.getProtectionDomain().getCodeSource().getLocation: " + ResourceUtility.class.getProtectionDomain().getCodeSource().getLocation());

        // 结论：只有ResourceUtility.class.getClassLoader().getResource("/")在IDE中才会受伤
        // 测试能否readAllLines
//        System.out.println(Paths.get("E:/OpenSourceProjects/yumitoy-backend/yumitoy-utilities/target/yumitoy-utilities-1.0-SNAPSHOT.jar!/BOOT-INF/classes!", "SimpleChineseDictionary.txt").toString().contains("!" + FileSystems.getDefault().getSeparator()));
//        System.out.println(Arrays.toString(Paths.get("E:/OpenSourceProjects/yumitoy-backend/yumitoy-utilities/target/yumitoy-utilities-1.0-SNAPSHOT.jar!/BOOT-INF/classes!", "SimpleChineseDictionary.txt").toString().split("!\\\\")));
//        System.out.println(strings);
//        Path path = Paths.get("E:/OpenSourceProjects/yumitoy-backend/yumitoy-utilities/target/yumitoy-utilities-1.0-SNAPSHOT.jar!/BOOT-INF/classes!", "com/yumitoy/utilities/SimpleChineseDictionary.txt");
////        List<String> strings = Files.readAllLines(path);
////        System.out.println(strings);
//        List<String> strings1 = FileUtility.readAllLines(path, Charset.defaultCharset());
//
//        System.out.println(strings1);

        System.out.println(getCurrentClasspath0());

    }


    /**
     * 读取一个文件的所有行
     * @param domain
     * @param resourceName
     * @return
     * @throws IOException
     * @since 2026.2.2
     */
    public static List<String> readAllLinesFromClassPath(String domain, String resourceName) throws IOException {
        String parent = getCurrentClasspath0();
        if (StringUtility.isBlank(domain)){
            return FileUtility.readAllLines(Paths.get(parent, resourceName));
        }
        return FileUtility.readAllLines(Paths.get(parent, domain.replace('.', File.separatorChar), resourceName));
    }

    public static URL getCurrentClasspath(){
        return ResourceUtility.class.getProtectionDomain().getCodeSource().getLocation();
    }
    private static String getCurrentClasspath0(){
        URL currentClasspathUrl = getCurrentClasspath();
        String currentClasspath = currentClasspathUrl.getPath();
        if (currentClasspathUrl.getPath().startsWith("file:")){
            currentClasspath = currentClasspath.replace("file:", "");
        }
        return currentClasspath.substring(1);
    }




}
