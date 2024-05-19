package cn.argento.askia.utilities;

import cn.argento.askia.exceptions.CompilationException;

import java.lang.reflect.Array;
import java.util.Arrays;

public class CompilationUtilityTest {
    public static void main(String[] args) throws CompilationException {
        final byte[] compile = CompilationUtility.compile(
                "D:\\OpenSourceProject\\CocoaToolkit\\CocoaToolkit-Utilities\\src\\test\\resources\\cn\\argento\\askia\\HelloWorld.java",
                "-g", "-verbose", "-version", "-help");
        System.out.println(Arrays.toString(compile));
    }
}
