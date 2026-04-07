package cn.argento.askia.exceptions.runtime.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * 不支持的 {@code MIME} 类型运行时异常.
 *
 * <p><b>此异常是运行时异常</b></p>
 *
 * <p>当你的代码中不支持操作某一MIME类型的文件，并且继续执行可能会导致不可预料的问题的时候，可以考虑抛出此异常</p>
 *
 * @author Askia
 */
public class UnsupportedMimeTypeRuntimeException extends RuntimeException {

    private static final String UNKNOWN_FILE_MIME_TYPE = "Unknown/Unknown";
    private static final String EXCEPTION_TXT_PART1 = "accept ";
    private static final String EXCEPTION_TXT_PART2 = "support ";

    private static boolean isWindowsNTOS(){
        // bug here!
        // if “OS” is not exist ,this method will throw NPE
        // this is not we want!
        final String os = System.getenv("OS");
        if (os != null && os.equalsIgnoreCase("Windows_NT")){
            return true;
        }
        // "os.name" creates by jvm!
        return System.getProperty("os.name").toLowerCase().contains("window") ||
                System.getProperty("os.name").toLowerCase().contains("win");
    }
    private static String getFileType(File file){
        if (isWindowsNTOS()) {
            final String fileType = file.getAbsolutePath().split("\\.")[1];
            return "NTSuffix/" + fileType;
        }
        try {
            // get file type
            final Path filePath = Paths.get(file.toURI());
            return Files.probeContentType(filePath);
        } catch (IOException e) {
            return UNKNOWN_FILE_MIME_TYPE;
        }
    }

    public UnsupportedMimeTypeRuntimeException(File file, String[] needMIMETypes){
        super(EXCEPTION_TXT_PART1 + "[" + getFileType(file) + "], " + EXCEPTION_TXT_PART2 + Arrays.toString(needMIMETypes));
    }
    public UnsupportedMimeTypeRuntimeException(String support, String accept) {
        super(EXCEPTION_TXT_PART1 + "[" + support + "], " + EXCEPTION_TXT_PART2 + "[" + accept + "]");
    }
    public UnsupportedMimeTypeRuntimeException(String support, String accept, Throwable cause){
        super(EXCEPTION_TXT_PART1 + "[" + support + "], " + EXCEPTION_TXT_PART2 + "[" + accept + "]", cause);
    }
    public UnsupportedMimeTypeRuntimeException(File file, String[] needMIMETypes, Throwable cause){
        super(EXCEPTION_TXT_PART1 + "[" + getFileType(file) + "], " + EXCEPTION_TXT_PART2 + Arrays.toString(needMIMETypes), cause);
    }
    public UnsupportedMimeTypeRuntimeException(String support, String[] needMINETypes){
        super(EXCEPTION_TXT_PART1 + "[" + support + "], " + EXCEPTION_TXT_PART2 + Arrays.toString(needMINETypes));
    }
    public UnsupportedMimeTypeRuntimeException(String support, String[] needMINETypes, Throwable cause){
        super(EXCEPTION_TXT_PART1 + "[" + support + "], " + EXCEPTION_TXT_PART2 + Arrays.toString(needMINETypes), cause);
    }
}
