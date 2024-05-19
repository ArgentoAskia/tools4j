package cn.argento.askia.utilities;

import java.io.File;
import java.util.Objects;

public class AssertionUtility {
   public  static <T> void requireNotNull(T t){
       Objects.requireNonNull(t);
   }
   public static <T> void requireNotNull(T t, String message){
       Objects.requireNonNull(t, message);
   }

   public static void requireNotPrimitive(Object o){
       if (o.getClass().isPrimitive()) {
           throw new IllegalArgumentException();
       }
   }

   public static void requireTypeMatched(Object o, Class<?> matchType){

   }

   public static void requireFileExisted(File file){

   }
}
