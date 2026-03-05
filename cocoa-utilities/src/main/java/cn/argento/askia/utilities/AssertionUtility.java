package cn.argento.askia.utilities;

import cn.argento.askia.exceptions.runtime.lang.ComparableVarsRequiredRuntimeException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.Objects;

public class AssertionUtility {
   public  static <T> void requireNotNull(T t){
       Objects.requireNonNull(t);
   }
   public static <T> void requireNotNull(T t, String message){
       Objects.requireNonNull(t, message);
   }

   public static <T, THROW extends Throwable> T requireNotNull(T t, THROW throwableIfNull) throws THROW{
       if (t == null){
           throw throwableIfNull;
       }
       return t;
   }

   public static void requireNotPrimitive(Object o){
       if (o.getClass().isPrimitive()) {
           throw new IllegalArgumentException();
       }
   }

   public static void requireTypeMatched(Object o, Class<?> matchType){
       if (o.getClass() != matchType){
           throw new AssertionError("type not match, param1 is " + o.getClass() + ", param2 is " + matchType);
       }
   }

   public static void requireFileExisted(File file){
       if (!file.exists()){
           // TODO: 2024/12/6 FileNotExistException(RuntimeType)
           throw new IllegalArgumentException();
       }
   }

   public static void requireObjectParamAsArrayType(Object obj){
       if (!ArrayUtility.isArray(obj)){
           throw new IllegalArgumentException("require array type param, but provides：" + obj.getClass() + " is not a array type");
       }
   }

    /**
     * 需要提供可比较类型
     * @param obj
     */
   public static void requireComparableVariable(Object obj){
       Class<?> clazz = obj.getClass();
       if (obj.getClass().isArray()){
           clazz = obj.getClass().getComponentType();
       }
       if (clazz.isPrimitive()){
           // boolean 类型不能进行比较
           if (clazz.getName().equalsIgnoreCase("z")){
               throw new ComparableVarsRequiredRuntimeException(obj);
           }
       }
       if (Comparable.class.isAssignableFrom(clazz)){
           return;
       }
       if (Comparator.class.isAssignableFrom(clazz)){
           return;
       }
       throw new ComparableVarsRequiredRuntimeException(obj);
   }

   public static <T> void AssertIndexInBound(T[] array, int currentIndex){
       if (currentIndex >= array.length){
           throw new ArrayIndexOutOfBoundsException("Array index out of range: " + currentIndex + ", [array max length: " + array.length + "]");
       }
   }

   public static void AssertMaxIndex(int current, int maxIndex){
       if (current >= maxIndex){
           throw new IndexOutOfBoundsException("max = " + maxIndex + ", but current =" + current);
       }
   }

   public static void requireEquals(Object o1, Object o2){
       if (o1 != o2 && !o1.equals(o2)){
           throw new RuntimeException("o1 != o2: [" + o1 + ", " + o2 + "]");
       }
   }

    /**
     * 断言 o1 必定不等于 o2
     * @param o1
     * @param o2
     */
   public static void requireNotEquals(Object o1, Object o2){
       // 引用相同，则肯定相同
       if (o1 == o2){
           // TODO: 2024/12/6 换成具体的异常类型
           throw new AssertionError("o1 == o2: [" + o1 + ", " + o2 + "]");
       }
       if (o1.equals(o2)){
           // TODO: 2024/12/6 换成具体的异常类型
           throw new AssertionError("o1 == o2: [" + o1 + ", " + o2 + "]");
       }
   }



}
