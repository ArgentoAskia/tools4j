package cn.argento.askia.supports.environment;

import cn.argento.askia.utilities.lang.AssertionUtility;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * 基础环境信息支持
 */
public class BaseEnvironmentBean implements EnvironmentBean<BaseEnvironmentBean>{

    private Class<? extends Annotation> solveAnnotation;

    private final Map<Class<?>, Object> annotationProcessorMap;

    /**
     * valueOf专用构造器
     * @param solveAnnotation
     * @param annotationProcessors
     */
    private BaseEnvironmentBean(Class<? extends Annotation> solveAnnotation, Object[] annotationProcessors){
        AssertionUtility.requireArrayAtLeastOneMember(annotationProcessors);
        this.solveAnnotation = solveAnnotation;
        annotationProcessorMap = new HashMap<>();
        for (Object annotationProcessor : annotationProcessors){
            annotationProcessorMap.put(annotationProcessor.getClass(), annotationProcessor);
        }
    }

    public Class<? extends Annotation> getSolveAnnotation() {
        return solveAnnotation;
    }

    public Map<Class<?>, Object> getAnnotationProcessorMap() {
        return annotationProcessorMap;
    }

    public <T> T getAnnotationProcessor(Class<T> tClass){
        final Object o = annotationProcessorMap.get(tClass);
        if (o != null){
            return tClass.cast(o);
        }
        else{
            return null;
        }
    }

    public static BaseEnvironmentBean valueOf(Class<? extends Annotation> solveAnnotation, Object... annotationProcessors){
        if (annotationProcessors == null){
            annotationProcessors = new Object[0];
        }
        return new BaseEnvironmentBean(solveAnnotation, annotationProcessors);
    }

    /**
     * Builder专用构造器
     * @param solveAnnotation
     * @param annotationProcessorMap
     */
    BaseEnvironmentBean(Class<? extends Annotation> solveAnnotation, Map<Class<?>, Object> annotationProcessorMap) {
        this.solveAnnotation = solveAnnotation;
        this.annotationProcessorMap = annotationProcessorMap;
    }



    public static BaseEnvironmentBeanBuilder builder() {
        return new BaseEnvironmentBeanBuilder();
    }

    public static DefaultAbstractBaseEnvironmentBeanSolveAnnotationBuilder baseEnvironmentBeanStepBuilder(){
        return new DefaultAbstractBaseEnvironmentBeanSolveAnnotationBuilder();
    }

    @Override
    public BaseEnvironmentBean getBean(BaseEnvironmentBean environmentBean) {
        this.annotationProcessorMap.putAll(environmentBean.getAnnotationProcessorMap());
        this.solveAnnotation = environmentBean.solveAnnotation;
        return this;
    }

    public static class BaseEnvironmentBeanBuilder {
        protected Class<? extends Annotation> solveAnnotation;
        protected final Map<Class<?>, Object> annotationProcessorMap;

        BaseEnvironmentBeanBuilder() {
            annotationProcessorMap = new HashMap<>();
        }

        public BaseEnvironmentBeanBuilder addProcessAnnotation(Class<? extends Annotation> solveAnnotation) {
            this.solveAnnotation = solveAnnotation;
            return this;
        }

        public BaseEnvironmentBeanBuilder addAnnotationProcessorMap(Map<Class<?>, Object> annotationProcessorMap) {
            this.annotationProcessorMap.putAll(annotationProcessorMap);
            return this;
        }

        public BaseEnvironmentBeanBuilder addAnnotationProcessor(Object annotationProcessor){
            this.annotationProcessorMap.put(annotationProcessor.getClass(), annotationProcessor);
            return this;
        }

        public BaseEnvironmentBean build() {
            return new BaseEnvironmentBean(solveAnnotation, annotationProcessorMap);
        }
    }



    protected abstract static class StepBuilder<B,N>{
        protected B innerBuilder;
        StepBuilder(B innerBuilderProxy){
            innerBuilder = innerBuilderProxy;
        }
        protected abstract N build();
    }

    /**
     * 规定了创建步骤的Builder
     */
    protected abstract static class BaseEnvironmentBeanStepBuilder<N> extends StepBuilder<BaseEnvironmentBeanBuilder, N>{

        BaseEnvironmentBeanStepBuilder(){
            super(new BaseEnvironmentBeanBuilder());
        }

        BaseEnvironmentBeanStepBuilder(BaseEnvironmentBeanBuilder innerBuilderProxy){
            super(innerBuilderProxy);
        }
    }

    protected abstract static class AbstractBaseEnvironmentBeanSolveAnnotationBuilder<N> extends BaseEnvironmentBeanStepBuilder<N>{

        AbstractBaseEnvironmentBeanSolveAnnotationBuilder(){
            super();
        }

        public N addProcessAnnotation(Class<? extends Annotation> solveAnnotation) {
            innerBuilder.addProcessAnnotation(solveAnnotation);
            return build();
        }

        @Override
        protected N build() {
            return createNextStepObject(innerBuilder);
        }

        protected abstract N createNextStepObject(BaseEnvironmentBeanBuilder innerBuilder);
    }

    public static final class DefaultAbstractBaseEnvironmentBeanSolveAnnotationBuilder extends AbstractBaseEnvironmentBeanSolveAnnotationBuilder<DefaultAbstractBaseEnvironmentBeanAnnotationProcessorBuilder> {

        @Override
        protected DefaultAbstractBaseEnvironmentBeanAnnotationProcessorBuilder createNextStepObject(BaseEnvironmentBeanBuilder innerBuilder) {
            return new DefaultAbstractBaseEnvironmentBeanAnnotationProcessorBuilder(innerBuilder);
        }
    }

    protected abstract static class AbstractBaseEnvironmentBeanAnnotationProcessorBuilder<N> extends BaseEnvironmentBeanStepBuilder<N>{

        AbstractBaseEnvironmentBeanAnnotationProcessorBuilder(BaseEnvironmentBeanBuilder innerBuilderProxy){
            super(innerBuilderProxy);
        }

        public N addAnnotationProcessorMap(Map<Class<?>, Object>... annotationProcessorMaps){
            if (annotationProcessorMaps.length == 0){
                return addAnnotationProcessorMap(new HashMap<>());
            }
            for (Map<Class<?>, Object> annotationProcessorMap: annotationProcessorMaps){
                innerBuilder.addAnnotationProcessorMap(annotationProcessorMap);
            }
            return build();
        }

        public N addAnnotationProcessorMap(Map<Class<?>, Object> annotationProcessorMap) {
            if (annotationProcessorMap == null){
                annotationProcessorMap = new HashMap<>();       // null参数填入空Map
            }
            innerBuilder.addAnnotationProcessorMap(annotationProcessorMap);
            return build();
        }

        public N addAnnotationProcessors(Object... annotationProcessors){
            if (annotationProcessors.length == 0){
                return addAnnotationProcessorMap(new HashMap<>());
            }
            Map<Class<?>, Object> map = new HashMap<>();
            for (Object annotationProcessor : annotationProcessors){
                map.put(annotationProcessor.getClass(), annotationProcessor);
            }
            return addAnnotationProcessorMap(map);
        }

        public N addAnnotationProcessor(Object annotationProcessor){
            innerBuilder.addAnnotationProcessor(annotationProcessor);
            return build();
        }


        @Override
        protected N build() {
            return createNextStepObject(innerBuilder);
        }

        protected abstract N createNextStepObject(BaseEnvironmentBeanBuilder innerBuilder);
    }

    public static final class DefaultAbstractBaseEnvironmentBeanAnnotationProcessorBuilder extends AbstractBaseEnvironmentBeanAnnotationProcessorBuilder<BaseEnvironmentBean> {

        DefaultAbstractBaseEnvironmentBeanAnnotationProcessorBuilder(BaseEnvironmentBeanBuilder innerBuilderProxy) {
            super(innerBuilderProxy);
        }

        @Override
        protected BaseEnvironmentBean createNextStepObject(BaseEnvironmentBeanBuilder innerBuilder) {
            return innerBuilder.build();
        }
    }
}
