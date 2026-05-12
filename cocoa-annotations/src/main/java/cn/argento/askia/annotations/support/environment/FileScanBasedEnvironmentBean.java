package cn.argento.askia.annotations.support.environment;

import cn.argento.askia.utilities.lang.StringUtility;

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

    FileScanBasedEnvironmentBean(Class<? extends Annotation> solveAnnotation, Map<Class<?>, Object> annotationProcessorMap, List<String> classpathList, List<String> packageList, List<String> classNameList) {
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

    public static FileScannedEnvironmentBeanBuilder builder() {
        return new FileScannedEnvironmentBeanBuilder();
    }

    public static FileScannedEnvironmentBeanSolveAnnotationBuilder fileScannedEnvironmentBeanStepBuilder(){
        return new FileScannedEnvironmentBeanSolveAnnotationBuilder();
    }

    // 普通Builder
    public static final class FileScannedEnvironmentBeanBuilder extends BaseEnvironmentBeanBuilder{
        private final Set<String> classpathList;
        private final Set<String> packageList;
        private final Set<String> classNameList;

        FileScannedEnvironmentBeanBuilder() {
            super();
            classpathList = new HashSet<>();
            packageList = new HashSet<>();
            classNameList = new HashSet<>();
        }

        public FileScannedEnvironmentBeanBuilder addClassPathList(List<String> classpathList) {
            this.classpathList.addAll(classpathList);
            return this;
        }

        public FileScannedEnvironmentBeanBuilder addClassPath(String classpath){
            this.classpathList.add(classpath);
            return this;
        }

        public FileScannedEnvironmentBeanBuilder addClassPaths(String... classPaths){
            for (String classpath : classPaths){
                addClassPath(classpath);
            }
            return this;
        }

        public FileScannedEnvironmentBeanBuilder addPackageList(List<String> packageList) {
            this.packageList.addAll(packageList);
            return this;
        }

        public FileScannedEnvironmentBeanBuilder addPackage(String packageName){
            this.packageList.add(packageName);
            return this;
        }

        public FileScannedEnvironmentBeanBuilder addClassNameList(List<String> classNameList) {
            this.classNameList.addAll(classNameList);
            return this;
        }

        public FileScannedEnvironmentBeanBuilder addClassName(String className){
            this.classNameList.add(className);
            return this;
        }

        @Override
        public FileScannedEnvironmentBeanBuilder addProcessAnnotation(Class<? extends Annotation> solveAnnotation) {
            super.addProcessAnnotation(solveAnnotation);
            return this;
        }

        @Override
        public FileScannedEnvironmentBeanBuilder addAnnotationProcessorMap(Map<Class<?>, Object> annotationProcessorMap) {
            super.addAnnotationProcessorMap(annotationProcessorMap);
            return this;
        }

        @Override
        public FileScannedEnvironmentBeanBuilder addAnnotationProcessor(Object annotationProcessor) {
            super.addAnnotationProcessor(annotationProcessor);
            return this;
        }

        @Override
        public FileScanBasedEnvironmentBean build() {
            return new FileScanBasedEnvironmentBean(super.solveAnnotation, super.annotationProcessorMap, new ArrayList<>(classpathList), new ArrayList<>(packageList), new ArrayList<>(classNameList));
        }
    }

    // step builder
    public static final class FileScannedEnvironmentBeanSolveAnnotationBuilder extends AbstractBaseEnvironmentBeanSolveAnnotationBuilder<FileScannedEnvironmentBeanAnnotationProcessorBuilder> {

        public FileScannedEnvironmentBeanSolveAnnotationBuilder(){
            innerBuilder = new FileScannedEnvironmentBeanBuilder();
        }

        @Override
        protected FileScannedEnvironmentBeanAnnotationProcessorBuilder createNextStepObject(BaseEnvironmentBeanBuilder innerBuilder) {
            return new FileScannedEnvironmentBeanAnnotationProcessorBuilder(innerBuilder);
        }
    }
    public static final class FileScannedEnvironmentBeanAnnotationProcessorBuilder extends AbstractBaseEnvironmentBeanAnnotationProcessorBuilder<FileScannedEnvironmentBeanClasspathListBuilder> {

        FileScannedEnvironmentBeanAnnotationProcessorBuilder(BaseEnvironmentBeanBuilder innerBuilderProxy) {
            super(innerBuilderProxy);
        }

        @Override
        protected FileScannedEnvironmentBeanClasspathListBuilder createNextStepObject(BaseEnvironmentBeanBuilder innerBuilder) {
            final FileScannedEnvironmentBeanBuilder innerBuilder1 = (FileScannedEnvironmentBeanBuilder) innerBuilder;
            return new FileScannedEnvironmentBeanClasspathListBuilder(innerBuilder1);
        }
    }
    protected static abstract class FileScannedEnvironmentBeanStepBuilder<N> extends StepBuilder<FileScannedEnvironmentBeanBuilder, N>{

        FileScannedEnvironmentBeanStepBuilder(FileScannedEnvironmentBeanBuilder innerBuilderProxy) {
            super(innerBuilderProxy);
        }

        FileScannedEnvironmentBeanStepBuilder(){
            super(new FileScannedEnvironmentBeanBuilder());
        }
    }
    public static final class FileScannedEnvironmentBeanClasspathListBuilder extends FileScannedEnvironmentBeanStepBuilder<FileScannedEnvironmentBeanPackageListBuilder>{

        FileScannedEnvironmentBeanClasspathListBuilder(FileScannedEnvironmentBeanBuilder innerBuilderProxy) {
            super(innerBuilderProxy);
        }


        public FileScannedEnvironmentBeanPackageListBuilder addClassPathList(List<String> classpathList) {
            if (classpathList == null){
                classpathList = new ArrayList<>();
            }
            innerBuilder.addClassPathList(classpathList);
            return build();
        }

        public FileScannedEnvironmentBeanPackageListBuilder addClassPath(String classpath){
            if (StringUtility.isBlank(classpath)){
                return build();
            }
            innerBuilder.addClassPath(classpath);
            return build();
        }

        public FileScannedEnvironmentBeanPackageListBuilder addClassPaths(String... classPaths){
            for (String classpath : classPaths){
                if (StringUtility.isBlank(classpath)){
                    continue;
                }
                innerBuilder.addClassPath(classpath);
            }
            return build();
        }

        @Override
        protected FileScannedEnvironmentBeanPackageListBuilder build() {
            return new FileScannedEnvironmentBeanPackageListBuilder(innerBuilder);
        }
    }
    public static final class FileScannedEnvironmentBeanPackageListBuilder extends FileScannedEnvironmentBeanStepBuilder<FileScannedEnvironmentBeanClassNameListBuilder>{
        public FileScannedEnvironmentBeanPackageListBuilder(FileScannedEnvironmentBeanBuilder innerBuilderProxy) {
            super(innerBuilderProxy);
        }

        public FileScannedEnvironmentBeanClassNameListBuilder addPackageList(List<String> packageList) {
            if (packageList == null){
                packageList = new ArrayList<>();
            }
            innerBuilder.addPackageList(packageList);
            return build();
        }

        public FileScannedEnvironmentBeanClassNameListBuilder addPackage(String packageName){
            innerBuilder.addPackage(packageName);
            return build();
        }

        public FileScannedEnvironmentBeanClassNameListBuilder addPackages(String... packageNames){
            for (String packageName : packageNames){
                if (!StringUtility.isBlank(packageName)){
                    innerBuilder.addPackage(packageName);
                }
            }
            return build();
        }

        @Override
        protected FileScannedEnvironmentBeanClassNameListBuilder build() {
            return new FileScannedEnvironmentBeanClassNameListBuilder(innerBuilder);
        }


        public FileScanBasedEnvironmentBean buildFileScanBasedEnvironmentBean(){
            return innerBuilder.build();
        }

        public BaseEnvironmentBean buildBaseEnvironmentBean(){
            return buildFileScanBasedEnvironmentBean();
        }
    }
    public static final class FileScannedEnvironmentBeanClassNameListBuilder extends FileScannedEnvironmentBeanStepBuilder<FileScanBasedEnvironmentBean>{
        public FileScannedEnvironmentBeanClassNameListBuilder(FileScannedEnvironmentBeanBuilder innerBuilderProxy) {
            super(innerBuilderProxy);
        }

        public FileScanBasedEnvironmentBean addClassNameList(List<String> classNameList) {
            if (classNameList == null){
                classNameList = new ArrayList<>();
            }
            innerBuilder.addClassNameList(classNameList);
            return build();
        }

        public FileScanBasedEnvironmentBean addClassName(String className){
            innerBuilder.addClassName(className);
            return build();
        }

        public FileScanBasedEnvironmentBean addClassNames(String... classNames){
            for (String className : classNames){
                if (!StringUtility.isBlank(className)){
                    innerBuilder.addClassName(className);
                }
            }
            return build();
        }

        public FileScanBasedEnvironmentBean buildFileScanBasedEnvironmentBean(){
            return build();
        }

        public BaseEnvironmentBean buildBaseEnvironmentBean(){
            return build();
        }

        @Override
        protected FileScanBasedEnvironmentBean build() {
            return innerBuilder.build();
        }
    }


}
