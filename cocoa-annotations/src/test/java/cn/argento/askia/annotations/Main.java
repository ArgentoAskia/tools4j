package cn.argento.askia.annotations;

import cn.argento.askia.annotations.context.AnnotationProcessorContext;
import cn.argento.askia.annotations.context.BeanNotFoundException;
import cn.argento.askia.annotations.context.DefaultAnnotationProcessorContext;

import java.lang.reflect.InvocationTargetException;

public class Main {

    public static void main(String[] args) throws BeanNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        AnnotationProcessorContext annotationProcessorContext = new DefaultAnnotationProcessorContext();
        AnnotationProcessors.process(Utility.class, new UtilityAnnotationProcessor(), null, annotationProcessorContext);
    }
}
