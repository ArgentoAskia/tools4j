package cn.argento.askia.utilities;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;

/**
 * java.bean.*封装, 提供JavaBean属性复制, JavaBean序列化等功能, propertiesManager等功能
 */
public final class BeanUtility {

    // 获取JavaBean property的值！
    public static <V> V getProperty(Object bean, String propertyName, Class<V> valueClass){
        final Object propertyValue = getProperty(bean, propertyName);
        return valueClass.cast(propertyValue);
    }

    // 获取JavaBean的值，
    public static Object getProperty(Object bean, String propertyName){
        return null;
    }

    public static Object getPropertyOrDefault(Object bean, String propertyName, Object defaultValue){
        return null;
    }

    public static boolean isJavaBean(Class<?> beanClass){
        return false;
    }

}
