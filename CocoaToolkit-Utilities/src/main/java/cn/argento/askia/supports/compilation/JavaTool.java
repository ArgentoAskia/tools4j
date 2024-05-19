package cn.argento.askia.supports.compilation;

import cn.argento.askia.utilities.ArrayUtility;
import cn.argento.askia.utilities.IOStreamUtility;
import sun.awt.CharsetString;

import javax.lang.model.SourceVersion;
import javax.tools.Tool;
import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.*;
import java.util.function.Consumer;

/**
 * Java.exe Tool simple Wrapper
 */
public class JavaTool implements Tool{

    protected ProcessBuilder processBuilder;
    protected List<JavaOption> options;
    protected String classOrJarName;
    protected List<JavaArg> javaArgs;
    protected boolean runClass;
    protected File javaPath;

    private static final int FAIL_TO_START = Integer.MIN_VALUE;
    private static final int OK = 0;

    private List<String> buildJavaCommand(String... args){
        List<String> cmd = new ArrayList<>();
        cmd.add(javaPath.toPath().normalize().toString());
        options.forEach(option -> {
            Collections.addAll(cmd, option.normalize());
        });
        if (!runClass){
            cmd.add("-jar");
        }
        cmd.add(classOrJarName);
        javaArgs.forEach(arg -> {
            Collections.addAll(cmd, arg.normalize());
        });
        ArrayUtility.foreach(args, s -> {
            Collections.addAll(cmd, s);
        });
        return cmd;
    }

    protected int start(OutputStream out, OutputStream err, String[] inputs){
        try {
            final Process process = processBuilder.start();
            if (inputs != null){
                final String inputStr = varargsToStringLine(inputs);
                final OutputStream outputStream = process.getOutputStream();
                outputStream.write(inputStr.getBytes());
                outputStream.flush();
            }

            final InputStream errorStream = process.getErrorStream();
            final byte[] errBytes = IOStreamUtility.readAllBytes(errorStream);
            err.write(errBytes);
            err.flush();
            final InputStream inputStream = process.getInputStream();
            final byte[] inputBytes = IOStreamUtility.readAllBytes(inputStream);
            out.write(inputBytes);
            out.flush();
            return OK;
        } catch (IOException e) {
            e.printStackTrace();
            return FAIL_TO_START;
        }
    }
    private String varargsToStringLine(String[] inputs){
        StringBuilder stringBuilder = new StringBuilder();
        for (String input :
                inputs) {
            stringBuilder.append(input).append("\n");
        }
        return stringBuilder.toString();
    }

    public JavaTool(){
        processBuilder = new ProcessBuilder();
        options = new ArrayList<>();
        javaArgs = new ArrayList<>();

        // use JAVA_HOME
        final String javaHome = System.getenv("JAVA_HOME");
        javaPath = new File(javaHome + "/bin/java.exe");
        if (!javaPath.isAbsolute()) {
            javaPath = javaPath.getAbsoluteFile();
        }
    }

    public JavaTool(File javaPath){
        processBuilder = new ProcessBuilder();
        options = new ArrayList<>();
        javaArgs = new ArrayList<>();
        if (javaPath.toString().endsWith("java.exe")){
            if (!javaPath.isAbsolute()) {
                javaPath = javaPath.getAbsoluteFile();
            }
            this.javaPath = javaPath;
        }
        // javaPath is not java.exe
        else{
            throw new InstantiationError("not a java.exe");
        }
    }

    public JavaTool(File javaPath, File directory){
        processBuilder = new ProcessBuilder();
        options = new ArrayList<>();
        javaArgs = new ArrayList<>();
        processBuilder.directory(directory);
        if (javaPath.toString().endsWith("java.exe")){
            if (!javaPath.isAbsolute()) {
                javaPath = javaPath.getAbsoluteFile();
            }
            this.javaPath = javaPath;
        }
        // javaPath is not java.exe
        else{
            throw new InstantiationError("not a java.exe");
        }
    }

    public JavaTool(File javaPath, File directory, Properties envExtend){
        this(javaPath, directory);
        final Map<String, String> environment = processBuilder.environment();
        final Enumeration<Object> keys = envExtend.keys();
        while(keys.hasMoreElements()){
            final String key = (String) keys.nextElement();
            final String value = envExtend.getProperty(key);
            environment.put(key, value);
        }
    }

    public JavaTool option(JavaOption option){
        options.add(option);
        return this;
    }

    public JavaTool options(JavaOption... options){
        Collections.addAll(this.options, options);
        return this;
    }

    public JavaTool className(String className){
        classOrJarName = className;
        runClass = true;
        return this;
    }

    public JavaTool jar(String jarPathName){
        classOrJarName = jarPathName;
        runClass = false;
        return this;
    }

    public JavaTool arg(JavaArg arg){
        javaArgs.add(arg);
        return this;
    }

    public JavaTool args(JavaArg... args){
        Collections.addAll(javaArgs, args);
        return this;
    }


    @Override
    public int run(InputStream in, OutputStream out, OutputStream err, String... arguments) {
        String[] inputLine = null;
        if (in != null && in != System.in){
            final byte[] input = IOStreamUtility.readAllBytes(in);
            StringTokenizer inputToken = new StringTokenizer(new String(input));
            inputLine = new String[inputToken.countTokens()];
            int index = 0;
            while (inputToken.hasMoreTokens()) {
                final String s = inputToken.nextToken();
                inputLine[index++] = s;
            }
        }
        // System.in Input
        // end with empty line
        if (in != null && in == System.in){
            Scanner scanner = new Scanner(in);
            String lineStr;
            List<String> inputLineList = new ArrayList<>();
            while(scanner.hasNextLine()){
                lineStr = scanner.nextLine();
                if (lineStr.isEmpty()){
                    break;
                }
                inputLineList.add(lineStr);
            }
            inputLine = inputLineList.toArray(new String[0]);
        }

        if (out == null){
            out = System.out;
        }
        if (err == null){
            err = System.err;
        }
        final List<String> javaCmd = buildJavaCommand(arguments);
        processBuilder.command(javaCmd);
        return start(out, err, inputLine);
    }

    @Override
    public Set<SourceVersion> getSourceVersions() {
        final SourceVersion[] values = SourceVersion.values();
        Set<SourceVersion> sourceVersions = new HashSet<>();
        ArrayUtility.toSet(values, sourceVersions);
        return Collections.unmodifiableSet(sourceVersions);
    }

}
