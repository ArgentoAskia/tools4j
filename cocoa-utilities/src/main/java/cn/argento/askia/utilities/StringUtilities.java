package cn.argento.askia.utilities;

import java.util.Objects;

public class StringUtilities {

    public static String upperFirstLetter(String s){
        Objects.requireNonNull(s);
        AssertionUtility.requireNotEquals(s.length(), 0);
        final char[] chars = s.toCharArray();
        if (Character.isLowerCase(chars[0])){
            final char upperCase = Character.toUpperCase(chars[0]);
            chars[0] = upperCase;
        }
        return String.valueOf(chars);
    }
}
