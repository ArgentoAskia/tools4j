package cn.argento.askia;


import cn.argento.askia.annotations.AnnotationProcessor;
import cn.argento.askia.annotations.Utility;
import cn.argento.askia.annotations.phase.*;

import java.util.List;
import java.util.Set;

@AnnotationProcessor(Utility.class)
public class UtilityAnnotationProcessor {


    @ScanPhase
    public int scanPhase(){
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
