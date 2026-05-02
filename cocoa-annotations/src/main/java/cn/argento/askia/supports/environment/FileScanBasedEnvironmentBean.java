package cn.argento.askia.supports.environment;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * 文件扫描基准环境信息
 */
public class FileScanBasedEnvironmentBean extends BaseEnvironmentBean {

    // 指定classpath, 支持通配符
    private List<String> classpathList;

    // 指定包名, 支持通配符
    private List<String> packageList;

    // 指定类名, 支持通配符
    private List<String> classNameList;

    FileScanBasedEnvironmentBean(Class<Annotation> solveAnnotation, Map<Class<?>, Object> annotationProcessorMap, List<String> classpathList, List<String> packageList, List<String> classNameList) {
        super(solveAnnotation, annotationProcessorMap);
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

    @Override
    public FileScanBasedEnvironmentBean getBean(BaseEnvironmentBean environmentBean) {
        if (environmentBean instanceof FileScanBasedEnvironmentBean){
            return (FileScanBasedEnvironmentBean)environmentBean;
        }
        // 返回空的FileScanBasedEnvironmentBean
        super.getBean(environmentBean);
        return this;
    }

    public static ScannedEnvironmentBeanBuilder builder() {
        return new ScannedEnvironmentBeanBuilder();
    }

    public static final class ScannedEnvironmentBeanBuilder extends BaseEnvironmentBeanBuilder{
        private final Set<String> classpathList;
        private final Set<String> packageList;
        private final Set<String> classNameList;

        ScannedEnvironmentBeanBuilder() {
            super();
            classpathList = new HashSet<>();
            packageList = new HashSet<>();
            classNameList = new HashSet<>();
        }

        public ScannedEnvironmentBeanBuilder addClassPathList(List<String> classpathList) {
            this.classpathList.addAll(classpathList);
            return this;
        }

        public ScannedEnvironmentBeanBuilder addClassPath(String classpath){
            this.classpathList.add(classpath);
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

        @Override
        public ScannedEnvironmentBeanBuilder addProcessAnnotation(Class<Annotation> solveAnnotation) {
            super.addProcessAnnotation(solveAnnotation);
            return this;
        }

        @Override
        public ScannedEnvironmentBeanBuilder addAnnotationProcessorMap(Map<Class<?>, Object> annotationProcessorMap) {
            super.addAnnotationProcessorMap(annotationProcessorMap);
            return this;
        }

        @Override
        public ScannedEnvironmentBeanBuilder addAnnotationProcessor(Object annotationProcessor) {
            super.addAnnotationProcessor(annotationProcessor);
            return this;
        }

        @Override
        public FileScanBasedEnvironmentBean build() {
            return new FileScanBasedEnvironmentBean(super.solveAnnotation, super.annotationProcessorMap, new ArrayList<>(classpathList), new ArrayList<>(packageList), new ArrayList<>(classNameList));
        }
    }
}
