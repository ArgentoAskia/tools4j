package cn.argento.askia.supports;

import cn.argento.askia.AnnotationProcessingHelper;
import cn.argento.askia.annotations.Utility;
import cn.argento.askia.utilities.AnnotationUtility;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.function.BiConsumer;

public class AnnotationProcessingHelperTest {

    @Test
    public void testModifyAnnotation() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final Utility annotation = AnnotationProcessingHelper.class.getDeclaredAnnotation(Utility.class);
        final Utility annotation2 = AnnotationProcessingHelper.class.getDeclaredAnnotation(Utility.class);

        // true!
        System.out.println(annotation == annotation2);

        System.out.println(annotation.hashCode());
        System.out.println(annotation.toString());
        final Annotation newUtilityAnnotation = AnnotationProcessingHelper.modifyAnnotation(annotation, AnnotationKeyValue.newAnnotationKeyValue("version", "2.0")
                , AnnotationKeyValue.newAnnotationKeyValue("name", "AnnotationProcessingHelper2"), AnnotationKeyValue.newAnnotationKeyValue("description", "description"));
        System.out.println(AnnotationUtility.getAnnotationAttributeValue(newUtilityAnnotation, "version"));
        System.out.println(AnnotationUtility.getAnnotationAttributeValue(newUtilityAnnotation, "toString"));
        System.out.println(AnnotationUtility.getAnnotationAttributeValue(newUtilityAnnotation, "hashCode"));
        System.getProperties().forEach(new BiConsumer<Object, Object>() {
            @Override
            public void accept(Object o, Object o2) {
                System.out.println(o + "=" + o2);
            }
        });
    }
}
