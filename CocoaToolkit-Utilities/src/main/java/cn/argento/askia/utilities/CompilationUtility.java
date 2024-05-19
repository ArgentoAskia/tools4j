package cn.argento.askia.utilities;

import cn.argento.askia.exceptions.CompilationException;
import cn.argento.askia.exceptions.file.UnsupportedFileMimeTypeException;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;

/**
 * {@code javac.exe}、{@code java.exe}、{@code JavaScript脚本}相关编译API工具类
 *
 * @author Askia
 * @version 1.0
 */
public class CompilationUtility {



    // 关于编译器javac.exe的参数，参考类：com.sun.tools.javac.main.Option

    // javac <options> <source files>

    /**
     * 合并&lt;options&gt;参数和&lt;source files&gt;参数，组成完整的&lt;options&gt; &lt;source files&gt;
     * @param options &lt;options&gt;
     * @param codeFiles &lt;source files&gt;
     * @return 完整的&lt;options&gt; &lt;source files&gt;命令行
     */
    private static String[] combineOptionsAndSourcesFilesStringArgs(String[] options, String[] codeFiles){
        String[] result = new String[options.length + codeFiles.length];
        System.arraycopy(options, 0, result, 0, options.length);
        System.arraycopy(codeFiles, 0, result, options.length, codeFiles.length);
        return result;
    }

    // native compiler api packaging

    /**
     * 原生编译器API{@linkplain JavaCompiler#run(InputStream, OutputStream, OutputStream, String...)}的内部封装，提供in、out、err和参数&lt;options&gt;，还有&lt;source files&gt;即可编译
     * @param codeFiles 具体的java代码文件，如D:\\HelloWorld.java
     * @param in 一般不提供，填null即可，null=stdin
     * @param out null则采用stdout
     * @param err null则采用stderr
     * @param options javac参数，一个参数一个字符串对应，可变输入
     * @return 编译成功返回1，失败返回0
     */
    private static int compiler(String[] codeFiles, InputStream in, OutputStream out, OutputStream err, String... options){
        final JavaCompiler systemJavaCompiler = ToolProvider.getSystemJavaCompiler();
        String[] optionsAndSource = combineOptionsAndSourcesFilesStringArgs(options, codeFiles);
        return systemJavaCompiler.run(in, out, err, optionsAndSource);
    }


    // wrap Varargs
    private static String[] wrapStringVarargsAsArray(String... varargs){
        return varargs;
    }

    // think about [java.io.tmpdir]
    private static final File defaultDiagnosticFile = new File(System.getProperty("java.io.tmpdir") + "/defaultDiagnostic.txt");
    private static final File defaultOutputFile= new File(System.getProperty("java.io.tmpdir") + "/defaultOutput.txt");

    private static void ensureDefaultDiagnosticFileCreatable(){
        try {
            if (!defaultDiagnosticFile.createNewFile()){
                System.out.println("Default Diagnostic File was exist!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void ensureDefaultOutputFileCreatable(){
        try {
            if (!defaultOutputFile.createNewFile()){
                System.out.println("Default Output File was exist!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // end with .java
    private static void checkForCodeFileSuffix(String codeFile) {
        if (!codeFile.endsWith(".java")){
            final String fileType = codeFile.split("\\.")[1];
            throw new UnsupportedFileMimeTypeException("text/java", fileType);
        }
    }
    private static File getByteCodeFileObj(String[] options, String codeFile) {
        if (codeFile.endsWith(".java")){
            codeFile = codeFile.replace(".java", ".class");
        }
        String fullFile = null;
        for (int i = 0; i < options.length; i++) {
            // 当前参数是d，则他的下一个参数就是class存储的位置
            if (options[i].equalsIgnoreCase("-d")){
                fullFile = options[i + 1];
                break;
            }
            // 如果是 ”d /etc/classOutput“, 这样填参数, 则需要做文本分割，并取第二个！
            else if (options[i].contains("-d")){
                fullFile = options[i].split(" ")[1];
                break;
            }
            else if (options[i].equalsIgnoreCase("-sourcepath")){
                fullFile = options[i + 1];
            }
            else if (options[i].contains("-sourcepath")){
                fullFile = options[i].split(" ")[1];
            }
        }
        // no args
        if (fullFile == null){
            fullFile = codeFile;
        }
        else{
            if (fullFile.endsWith("/") || fullFile.endsWith("\\")) {
                fullFile = fullFile + codeFile;
            }
            else{
                fullFile = fullFile + "/" + codeFile;
            }
        }
        File javaFile = new File(fullFile);
        return javaFile;
    }
    private static byte[] readAllByteCodes(File byteCodesFile){
        try {
            FileInputStream byteCodeFileStream = new FileInputStream(byteCodesFile);
            return IOStreamUtility.readAllBytes(byteCodeFileStream, IOStreamUtility.BUFFER_2KB_SIZE);
        } catch (FileNotFoundException e) {
            // not happen！
            e.printStackTrace();
            throw new Error("byteCodesFile not found!");
        }
    }

    // 完善输出日志！
    // 完善方法返回值，异常设计?是否需要抛出异常？
    public static byte[] compile(String codeFile, String... options) throws CompilationException {
        checkForCodeFileSuffix(codeFile);
        // compiling java code does not need InputStream
        InputStream in = null;
        // out and err let null just for preventing
        OutputStream out = null;
        OutputStream err = null;
        File diagnosticFile = new File("./compile_error_message.txt");
        String diagnosticFileCreationWarning = null;
        File outputFile = new File("./compiler_output_message.txt");
        String outputFileCreationWarning = null;
        try {
            if (!diagnosticFile.createNewFile()){
                // file is already exist! no need to create,
                // but we need to write out a statement for this in "diagnosticFile"
                System.out.println("diagnosticFile：[" + diagnosticFile.getAbsolutePath() + "] was exist!!");
                diagnosticFileCreationWarning = "WARNING! ==> " + "diagnosticFile：[" + diagnosticFile.getAbsolutePath() + "] was exist!!\n";
            }
        } catch (IOException e) {
            // can not create diagnosticFile, use default and show massage!
            e.printStackTrace();
            System.out.println("can not create diagnosticFile：[" + diagnosticFile.getAbsolutePath() + "], use default: [" + defaultDiagnosticFile.getAbsolutePath() + "]");
            diagnosticFileCreationWarning = "WARNING! ==> " + "can not create diagnosticFile：[" + diagnosticFile.getAbsolutePath() + "], use default: [" + defaultDiagnosticFile.getAbsolutePath() + "]\n";
            diagnosticFile = defaultDiagnosticFile;
            ensureDefaultDiagnosticFileCreatable();
        }
        try {
            err = new BufferedOutputStream(new FileOutputStream(diagnosticFile));
            if (diagnosticFileCreationWarning != null){
                err.write(diagnosticFileCreationWarning.getBytes());
            }
        } catch (IOException exception) {
            // this may not happen!
            exception.printStackTrace();
        }

        // mirror code for output file
        try {
            if (!outputFile.createNewFile()){
                System.out.println("outputFile：[" + outputFile.getAbsolutePath() + "] was exist!!");
                outputFileCreationWarning = "WARNING! ==> " + "outputFile：[" + outputFile.getAbsolutePath() + "] was exist!!\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("can not create outputFile：[" + outputFile.getAbsolutePath() + "], use default: [" + defaultOutputFile.getAbsolutePath() + "]");
            outputFileCreationWarning = "WARNING! ==> " + "can not create outputFile：[" + outputFile.getAbsolutePath() + "], use default: [" + defaultOutputFile.getAbsolutePath() + "]\n";
            outputFile = defaultOutputFile;
            ensureDefaultOutputFileCreatable();
        }
        try {
            out = new BufferedOutputStream(new FileOutputStream(outputFile));
            if (outputFileCreationWarning != null){
                out.write(outputFileCreationWarning.getBytes());
            }
        } catch (IOException e) {
            // this may not happen!
            e.printStackTrace();
        }

        final int compileResult = compiler(wrapStringVarargsAsArray(codeFile), in, out, err, options);
        if (compileResult == 0){
            // 编译成功,开始读入字节码
            // 注意读取字节码的时候，要考虑-d -source 参数！
            File byteCodeFile = getByteCodeFileObj(options, codeFile);
            // 读取所有字节码返回
            return readAllByteCodes(byteCodeFile);
        }
        else{
            throw new CompilationException(new File(codeFile), "请检查错误日志文件: [" + diagnosticFile.getAbsoluteFile().toPath().normalize() + "]");
        }
    }


    // 1.是否保留编译后的class文件 开关
    // 2.是否编译java文件还是仅仅编译代码String对象！ File优先
    // 3.返回字节码还是返回Class文件的位置还是返回编译完成成功还是失败？ 返回字节码
    // 4.是否需要写出编译信息和日志文件？开关

    // 1.编译失败抛出异常,并放入编译错误日志！
    // 2.采用构建者模式处理 javac <options> <source files>[在CocoaToolkit-CommandToolsProviders模块中实现]
    public static byte[] compile(File[] javaCodeFiles, boolean deleteClassFile,
                                 OutputStream outLog,  OutputStream errLog,
                                 String... options){
        return null;
    }
}
