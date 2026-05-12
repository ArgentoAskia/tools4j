package cn.argento.askia.annotations;

import cn.argento.askia.annotations.annotation.AnnotationProcessor;
import cn.argento.askia.annotations.annotation.Param;
import cn.argento.askia.annotations.annotation.phase.*;
import cn.argento.askia.annotations.context.MutableAnnotationProcessorContext;
import cn.argento.askia.annotations.scanner.DefaultFileScanBasedEnvironmentScanner;
import cn.argento.askia.annotations.support.LifeCyclePhase;
import cn.argento.askia.annotations.support.environment.FileScanBasedEnvironmentBean;
import cn.argento.askia.utilities.collection.CollectionUtility;

import java.lang.annotation.Annotation;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

@AnnotationProcessor
public class UtilityAnnotationProcessor {

    @ScanPhase
    public Map<String, Map<String, List<Path>>> scanPhase(MutableAnnotationProcessorContext context){

        // 代理+调用链框架
        final FileScanBasedEnvironmentBean fileScanBasedEnvironmentBean = FileScanBasedEnvironmentBean.fileScannedEnvironmentBeanStepBuilder()
                .addProcessAnnotation(Utility.class)
                .addAnnotationProcessor(this)
                .addClassPath("E:\\OpenSourceProjects\\tools4j\\cocoa-utilities\\target\\classes")
                .addPackage("cn.argento.askia.utilities.*")
                .addClassName("*");
        DefaultFileScanBasedEnvironmentScanner scanner = new DefaultFileScanBasedEnvironmentScanner();
        final Map<String, Map<String, List<Path>>> scan = scanner.scan(fileScanBasedEnvironmentBean);
        System.out.println(CollectionUtility.toBeautifulString(scan));
        return scan;
    }


    @CheckPhase
    public void checkPhase(MutableAnnotationProcessorContext context,
                           Set<Class<? extends Annotation>> annotationResolveSet){
        System.out.println("CheckPhase 阶段...");
        System.out.println(context);
        System.out.println(annotationResolveSet);
    }


    @AnnotationProcessingPhase
    public void annotationProcessingPhase(MutableAnnotationProcessorContext context,
                                          @Param(phaseRet = LifeCyclePhase.SCANNING) Map<String, Map<String, List<Path>>> scanResult){
        System.out.println("AnnotationProcessingPhase 阶段...");
        System.out.println(context);
        System.out.println(scanResult);
    }

    @MainProcessingPhase
    public void mainProcessingPhase(){
        System.out.println("MainProcessingPhase 阶段...");
    }

    @PostProcessingPhase
    public void postProcessingPhase(){
        System.out.println("PostProcessingPhase 阶段...");
    }


    @CompletionPhase
    public void complete(){
        System.out.println("complete 阶段...");
    }
}
