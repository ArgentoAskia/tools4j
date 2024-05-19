package cn.argento.askia.exceptions.file;

import cn.argento.askia.utilities.SystemUtility;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Unsupported File MIME Type Exception
 * when you accept a wrong file type or a wrong file MIME type, you may throw this Exception!!
 *
 * @author Askia
 */
public class UnsupportedFileMimeTypeException extends RuntimeException {

    private static final String UNKNOWN_FILE_MIME_TYPE = "Unknown/Unknown";
    private static final String EXCEPTION_TXT_PART1 = "accept ";
    private static final String EXCEPTION_TXT_PART2 = "support ";


    private static String getFileType(File file){
        if (SystemUtility.isWindowsNTOS()) {
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

    public UnsupportedFileMimeTypeException(File file, String[] needMIMETypes){
        super(EXCEPTION_TXT_PART1 + "[" + getFileType(file) + "], " + EXCEPTION_TXT_PART2 + Arrays.toString(needMIMETypes));
    }

    public UnsupportedFileMimeTypeException(String support, String accept) {
        super(EXCEPTION_TXT_PART1 + "[" + support + "], " + EXCEPTION_TXT_PART2 + "[" + accept + "]");
    }

    public UnsupportedFileMimeTypeException(String support, String accept, Throwable cause){
        super(EXCEPTION_TXT_PART1 + "[" + support + "], " + EXCEPTION_TXT_PART2 + "[" + accept + "]", cause);
    }

    public UnsupportedFileMimeTypeException(File file, String[] needMIMETypes, Throwable cause){
        super(EXCEPTION_TXT_PART1 + "[" + getFileType(file) + "], " + EXCEPTION_TXT_PART2 + Arrays.toString(needMIMETypes), cause);
    }
    public UnsupportedFileMimeTypeException(String support, String[] needMINETypes){
        super(EXCEPTION_TXT_PART1 + "[" + support + "], " + EXCEPTION_TXT_PART2 + Arrays.toString(needMINETypes));
    }
    public UnsupportedFileMimeTypeException(String support, String[] needMINETypes, Throwable cause){
        super(EXCEPTION_TXT_PART1 + "[" + support + "], " + EXCEPTION_TXT_PART2 + Arrays.toString(needMINETypes), cause);
    }
}
