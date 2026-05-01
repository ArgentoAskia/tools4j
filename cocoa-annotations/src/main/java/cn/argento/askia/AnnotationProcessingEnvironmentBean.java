package cn.argento.askia;

import cn.argento.askia.annotations.AnnotationProcessor;
import cn.argento.askia.annotations.ApiStatus;
import cn.argento.askia.annotations.phase.*;
import cn.argento.askia.supports.LifeCyclePhase;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

@ApiStatus.Internal
class AnnotationProcessingEnvironmentBean<T> {

    /** private-package */
    AnnotationProcessingEnvironmentBean(){
        lifeCyclePhaseMap = new HashMap<>();
    }
    /**
     * 注解处理器Class
     */
    private Class<T> annotationProcessorClass;
    /**
     * 注解处理器
     */
    private AnnotationProcessor annotationProcessor;

    private final Map<LifeCyclePhase, Map<Method, List<Annotation>>> lifeCyclePhaseMap;


    void setAnnotationProcessorClass(Class<T> annotationProcessorClass) {
        this.annotationProcessorClass = annotationProcessorClass;
    }

    void setAnnotationProcessor(AnnotationProcessor annotationProcessor) {
        this.annotationProcessor = annotationProcessor;
    }

    void addMethodPhase(LifeCyclePhase lifeCyclePhase, Method method, Annotation phaseAnnotation){
        final Map<Method, List<Annotation>> annotationProcessingPhaseMethods = lifeCyclePhaseMap.computeIfAbsent(lifeCyclePhase, lifeCyclePhase2 -> new HashMap<>());
        // 添加注解
        final List<Annotation> phaseList = annotationProcessingPhaseMethods.computeIfAbsent(method, method1 -> new ArrayList<>());
        phaseList.add(phaseAnnotation);
    }
    void addMethodPhases(LifeCyclePhase lifeCyclePhase, Method method, Annotation[] phaseAnnotation){
        final Map<Method, List<Annotation>> annotationProcessingPhaseMethods = lifeCyclePhaseMap.computeIfAbsent(lifeCyclePhase, lifeCyclePhase2 -> new HashMap<>());
        // 添加注解
        final List<Annotation> phaseList = annotationProcessingPhaseMethods.computeIfAbsent(method, method1 -> new ArrayList<>());
        phaseList.addAll(Arrays.asList(phaseAnnotation));
    }

    void addMethodAnnotationProcessingPhase(Method method, AnnotationProcessingPhase annotationProcessingPhase){
        addMethodPhase(LifeCyclePhase.ANNOTATION_PROCESSING, method, annotationProcessingPhase);
    }
    void addMethodAnnotationProcessingPhases(Method method, AnnotationProcessingPhase[] annotationProcessingPhases){
        addMethodPhases(LifeCyclePhase.ANNOTATION_PROCESSING, method, annotationProcessingPhases);
    }

    void addMethodCheckPhase(Method method, CheckPhase checkPhase){
        addMethodPhase(LifeCyclePhase.CHECKING, method, checkPhase);
    }
    void addMethodCheckPhases(Method method, CheckPhase[] checkPhases){
        addMethodPhases(LifeCyclePhase.CHECKING, method, checkPhases);
    }

    void addMethodCompletionPhase(Method method, CompletionPhase completionPhase){
        addMethodPhase(LifeCyclePhase.FINISHING, method, completionPhase);
    }
    void addMethodCompletionPhases(Method method, CompletionPhase[] completionPhases){
        addMethodPhases(LifeCyclePhase.FINISHING, method, completionPhases);
    }

    void addMethodMainProcessingPhase(Method method, MainProcessingPhase mainProcessingPhase){
        addMethodPhase(LifeCyclePhase.MAIN_PROCESSING, method, mainProcessingPhase);
    }
    void addMethodMainProcessingPhases(Method method, MainProcessingPhase[] mainProcessingPhases){
        addMethodPhases(LifeCyclePhase.MAIN_PROCESSING, method, mainProcessingPhases);
    }

    void addMethodPostProcessingPhase(Method method, PostProcessingPhase postProcessingPhase){
        addMethodPhase(LifeCyclePhase.POST_PROCESSING, method, postProcessingPhase);
    }
    void addMethodPostProcessingPhases(Method method, PostProcessingPhase[] postProcessingPhases){
        addMethodPhases(LifeCyclePhase.POST_PROCESSING, method, postProcessingPhases);
    }

    void addMethodScanPhase(Method method, ScanPhase scanPhase){
        addMethodPhase(LifeCyclePhase.SCANNING, method, scanPhase);
    }
    void addMethodScanPhases(Method method, ScanPhase[] scanPhases){
        addMethodPhases(LifeCyclePhase.SCANNING, method, scanPhases);
    }



    // 获取方法
    public Class<T> getAnnotationProcessorClass() {
        return annotationProcessorClass;
    }

    public AnnotationProcessor getAnnotationProcessor() {
        return annotationProcessor;
    }


    public Map<Method, List<Annotation>> getPhaseMap(LifeCyclePhase lifeCyclePhase){
        return lifeCyclePhaseMap.get(lifeCyclePhase);
    }
}
