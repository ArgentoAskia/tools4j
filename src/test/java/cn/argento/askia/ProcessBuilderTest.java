package cn.argento.askia;


import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.*;
import java.util.function.BiConsumer;

public class ProcessBuilderTest {
    public static void main(String[] args) throws IOException {
        try {
            new ProcessBuilderTest().clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        ProcessBuilder processBuilder = new ProcessBuilder();
//        environment.forEach((s, s2) -> System.out.println(s + " = " + s2));
        // 注意，不要使用和当前项目相同的JDK来创建进程！！
        processBuilder.command("E:\\RTE\\JDK\\OracleJDK\\jdk-9.0.4\\bin\\java.exe", "-classpath", "E:\\RTE\\JDK\\OracleJDK\\jdk-8u311\\jre\\lib\\charsets.jar" ,"--version");
//        processBuilder.command("native2ascii");
//        processBuilder.command("D:\\OpenSourceProject\\CocoaToolkit\\src\\test\\java\\cn\\argento\\askia\\remote.bat");
        final Process start = processBuilder.start();
        final OutputStream outputStream = start.getOutputStream();
        outputStream.write("ABC\n123".getBytes());
        outputStream.flush();
        outputStream.close();
        final InputStream inputStream = start.getInputStream();
        final byte[] bytes = IOStreamUtility2.readAllBytes(inputStream);
        final String gbk = new String(bytes, "GBK");
        System.out.println(gbk);
        inputStream.close();

    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        System.out.println("123");
        return null;
    }
}
