package cn.argento.askia.supports.beans;

import cn.argento.askia.utilities.classes.ClassUtility;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ScannedEnvironmentBean {

    // 指定classpath, 支持通配符
    private List<String> classpathList;

    // 指定包名, 支持通配符
    private List<String> packageList;

    // 指定类名, 支持通配符
    private List<String> classNameList;

    ScannedEnvironmentBean(List<String> classpathList, List<String> packageList, List<String> classNameList) {
        this.classpathList = classpathList;
        this.packageList = packageList;
        this.classNameList = classNameList;
    }

    public List<String> getClasspathList() {
        return classpathList;
    }

    public List<String> getPackageList() {
        return packageList;
    }

    public List<String> getClassNameList() {
        return classNameList;
    }

    public List<String[]> getAllAvailablePaths(){
        List<String[]> ret = new ArrayList<>();
        for (String classpath : classpathList){
            for (String packageName : packageList){
                for (String className : classNameList){
                    String[] path = new String[3];
                    path[0] = classpath;
                    path[1] = packageName;
                    path[2] = className;
                    ret.add(path);
                }
            }
        }
        return ret;
    }


    public static ScannedEnvironmentBeanBuilder builder() {
        return new ScannedEnvironmentBeanBuilder();
    }

    public static final class ScannedEnvironmentBeanBuilder {
        private final Set<String> classpathList;
        private final Set<String> packageList;
        private final Set<String> classNameList;

        private ScannedEnvironmentBeanBuilder() {
            classpathList = new HashSet<>();
            packageList = new HashSet<>();
            classNameList = new HashSet<>();
        }

        public ScannedEnvironmentBeanBuilder addClassPathList(List<String> classpathList) {
            this.classpathList.addAll(classpathList);
            return this;
        }

        public ScannedEnvironmentBeanBuilder addClassPath(String classpath){
            this.classNameList.add(classpath);
            return this;
        }

        public ScannedEnvironmentBeanBuilder addClassPaths(String... classPaths){
            for (String classpath : classPaths){
                addClassPath(classpath);
            }
            return this;
        }

        public ScannedEnvironmentBeanBuilder addPackageList(List<String> packageList) {
            this.packageList.addAll(packageList);
            return this;
        }

        public ScannedEnvironmentBeanBuilder addPackage(String packageName){
            this.packageList.add(packageName);
            return this;
        }

        public ScannedEnvironmentBeanBuilder addClassNameList(List<String> classNameList) {
            this.classNameList.addAll(classNameList);
            return this;
        }

        public ScannedEnvironmentBeanBuilder addClassName(String className){
            this.classNameList.add(className);
            return this;
        }

        public ScannedEnvironmentBean build() {
            return new ScannedEnvironmentBean(new ArrayList<>(classpathList), new ArrayList<>(packageList), new ArrayList<>(classNameList));
        }
    }
}
