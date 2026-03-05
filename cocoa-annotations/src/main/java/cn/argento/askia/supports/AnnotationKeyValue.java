package cn.argento.askia.supports;

import cn.argento.askia.utilities.AnnotationUtility;

import java.lang.annotation.Annotation;
import java.util.Objects;

/**
 * 该类代表注解中一个属性和其值, 实际上就是属性和属性值的包装类, 内部提供了Getter方法供访问, 并提供了两个静态方法来创建其对象.
 * 该类和String一样，是不可变的，后续添加API时，任何新的操作都应该为其创建新的对象！
 * @param <V>
 */
public class AnnotationKeyValue<V> {
    private final String attributeName;
    private final V value;


    public static <V2> AnnotationKeyValue<V2> newAnnotationKeyValue(String attributeName, V2 newValue){
        return new AnnotationKeyValue<V2>(attributeName, newValue);
    }

    public static <V2> AnnotationKeyValue<V2> newAnnotationKeyValue(Class<? extends Annotation> refAnnotation, String attributeName, V2 newValue){
        final boolean hasAttribute = AnnotationUtility.hasAttribute(refAnnotation, attributeName);
        if (!hasAttribute){
            throw new IllegalArgumentException("注解：" + refAnnotation + "没有名为：" + attributeName + "的属性！");
        }
        try {
            final Class<?> annotationAttributeType = AnnotationUtility.getAnnotationAttributeType(refAnnotation, attributeName);
            final Class<?> newValueClass = newValue.getClass();
            if (newValueClass.equals(annotationAttributeType)){
                // 属性存在且新值类型正确！
                return new AnnotationKeyValue<>(attributeName, newValue);
            }
            else {
                throw new IllegalArgumentException("注解：" + refAnnotation + "的属性" + attributeName + "类型为：" + annotationAttributeType
                        + ", 而新值：" + newValue + "的类型为：" + newValueClass);
            }
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("注解：" + refAnnotation + "没有名为：" + attributeName + "的属性！");
        }
    }
    // protected
    private AnnotationKeyValue(String attributeName, V value){
        this.attributeName = attributeName;
        this.value = value;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public V getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnnotationKeyValue)) return false;
        AnnotationKeyValue<?> that = (AnnotationKeyValue<?>) o;
        return Objects.equals(attributeName, that.attributeName) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attributeName, value);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("cn.argento.askia.supports.AnnotationKeyValue{");
        sb.append("attributeName='").append(attributeName).append('\'');
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
