package cn.argento.askia;


import cn.argento.askia.annotations.AnnotationProcessor;
import cn.argento.askia.annotations.Utility;
import cn.argento.askia.annotations.phase.*;
import cn.argento.askia.context.MutableAnnotationProcessorContext;
import cn.argento.askia.scanner.DefaultFileScanBasedEnvironmentScanner;
import cn.argento.askia.supports.environment.FileScanBasedEnvironmentBean;

import java.util.List;
import java.util.Set;

/**
 * {@code &#64;Utility} 处理器
 *
 */
@AnnotationProcessor(Utility.class)
public class UtilityAnnotationProcessor {


    @ScanPhase
    public int scanPhase(MutableAnnotationProcessorContext context){

        DefaultFileScanBasedEnvironmentScanner scanner = new DefaultFileScanBasedEnvironmentScanner();

        return 0;
    }


    @CheckPhase
    public void checkPhase(){

    }


    @AnnotationProcessingPhase
    public List<String> annotationProcessingPhase(){
        return null;
    }

    @MainProcessingPhase
    public String mainProcessingPhase(){
        return "";
    }


    @PostProcessingPhase
    public Set<Integer> postProcessingPhase(){
        return null;
    }


    @CompletionPhase
    public void complete(){

    }



}
