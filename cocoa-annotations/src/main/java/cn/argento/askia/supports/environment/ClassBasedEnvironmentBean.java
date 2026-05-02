package cn.argento.askia.supports.environment;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 类基础扫描环境信息
 */
public class ClassBasedEnvironmentBean extends BaseEnvironmentBean{

    private List<Class<?>> baseClasses;
    /**
     * Builder专用构造器
     *
     * @param solveAnnotation
     * @param annotationProcessorMap
     */
    ClassBasedEnvironmentBean(Class<Annotation> solveAnnotation, Map<Class<?>, Object> annotationProcessorMap,  List<Class<?>> baseClasses) {
        super(solveAnnotation, annotationProcessorMap);
        this.baseClasses = baseClasses;
    }

    public List<Class<?>> getBaseClass() {
        return baseClasses;
    }

    @Override
    public ClassBasedEnvironmentBean getBean(BaseEnvironmentBean environmentBean) {
        if (environmentBean instanceof ClassBasedEnvironmentBean){
            return (ClassBasedEnvironmentBean)environmentBean;
        }
        // 返回空的ClassBasedEnvironmentBean
        super.getBean(environmentBean);
        return this;
    }

    public static ClassBasedEnvironmentBeanBuilder builder() {
        return new ClassBasedEnvironmentBeanBuilder();
    }

    public static final class ClassBasedEnvironmentBeanBuilder extends BaseEnvironmentBeanBuilder{
        private final List<Class<?>> baseClasses;

        ClassBasedEnvironmentBeanBuilder() {
            baseClasses = new ArrayList<>();
        }

        public ClassBasedEnvironmentBeanBuilder addBaseClass(Class<?> baseClass) {
            baseClasses.add(baseClass);
            return this;
        }

        public ClassBasedEnvironmentBeanBuilder addBaseClasses(Class<?>... baseClasses) {
            Collections.addAll(this.baseClasses, baseClasses);
            return this;
        }

        public ClassBasedEnvironmentBeanBuilder addBaseClassList(List<Class<?>> baseClassList) {
            baseClasses.addAll(baseClassList);
            return this;
        }


        @Override
        public ClassBasedEnvironmentBean build() {
            return new ClassBasedEnvironmentBean(super.solveAnnotation, super.annotationProcessorMap, baseClasses);
        }
    }
}
