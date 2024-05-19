package cn.argento.askia.utilities;

import cn.argento.askia.annotations.Utility;
import org.junit.Test;

import java.lang.annotation.ElementType;
import java.util.Arrays;

public class AnnotationUtilityTest {

    @Test
    public void testGetAnnotationAttributeType() throws NoSuchMethodException {
        final Class<?> version = AnnotationUtility.getAnnotationAttributeType(Utility.class, "version");
        System.out.println(version);
    }

    @Test
    public void testGetAnnotationTarget(){
        final ElementType[] annotationTarget = AnnotationUtility.getAnnotationTarget(Utility.class);
        System.out.println(Arrays.toString(annotationTarget));
    }


    @Test
    public void testGetAnnotationAllAttributes(){
        final String[] annotationAllAttributes = AnnotationUtility.getAnnotationAllAttributes(Utility.class);
        System.out.println(Arrays.toString(annotationAllAttributes));
    }
}
