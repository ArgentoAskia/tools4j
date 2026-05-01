package cn.argento.askia.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.List;
import java.util.Properties;

// 定义专门的注解处理接口
// 专门用来处理单独的某个注解，无论该注解是哪种级别！
// 实现一种自动寻找注解处理器的机制？
@FunctionalInterface
public interface AnnotationProcessor<A extends Annotation> {
    // 注解处理器的顶层接口...
    default void setProperties(Properties properties){
    }
    Object process(List<A> annotation, AnnotatedElement markPlace);

}
