package cn.argento.askia.utilities;

import cn.argento.askia.annotations.Utility;
import cn.argento.askia.utilities.annotation.AnnotationUtility;
import cn.argento.askia.utilities.classes.ClassUtility;
import cn.argento.askia.utilities.collection.CollectionUtility;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 输出当前包下的所有工具类
 */
public class Main {
    public static void main(String[] args) throws IOException {
        Class<?>[] currentClassesPath = ClassUtility.scanCurrentDirectoryClasses(Main.class, false, true);
        Map<String, String> map = new HashMap<>();
        for (Class<?> currentClass : currentClassesPath) {
            Utility annotation = AnnotationUtility.getAnnotation(currentClass, Utility.class);
            if (annotation == null){
                continue;
            }
            StringBuilder sb = new StringBuilder();
            String value = annotation.value();
            String archive = annotation.archive();
            if (archive.equals(Utility.USE_PACKAGE)) {
                archive = currentClass.getSimpleName();
            }
            String author = annotation.author();
            String version = annotation.version();
            sb.append("工具类名称：").append(value).append(", ").append("归档名称：").append(archive).append(", ")
                    .append("作者：").append(author).append(", ").append("版本：").append(version);
            map.put(currentClass.getName(), sb.toString());
        }
        System.out.println(CollectionUtility.toBeautifulString(map));
    }
}
