package cn.argento.askia;

import cn.argento.askia.supports.compilation.JavaOption;
import cn.argento.askia.supports.compilation.JavaTool;
import org.junit.Test;

public class JavaToolTest {

    @Test
    public void testJavaTool(){
        final JavaTool javaTool = new JavaTool();
        final int run = javaTool.option(JavaOption.CLASSPATH)
                .className("cn.argento.askia.HelloWorld")
                .run(System.in, null, null);
        System.out.println(run);
    }

    public static void main(String[] args) {
        final JavaTool javaTool = new JavaTool();
        final int run = javaTool.option(JavaOption.CLASSPATH)
                .className("cn.argento.askia.HelloWorld")
                .run(System.in, null, null);
        System.out.println(run);
    }
}
