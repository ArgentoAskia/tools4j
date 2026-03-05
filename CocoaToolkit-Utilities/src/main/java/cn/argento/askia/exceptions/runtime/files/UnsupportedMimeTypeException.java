package cn.argento.askia.exceptions.runtime.files;

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
public class UnsupportedMimeTypeException extends RuntimeException {

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

    public UnsupportedMimeTypeException(File file, String[] needMIMETypes){
        super(EXCEPTION_TXT_PART1 + "[" + getFileType(file) + "], " + EXCEPTION_TXT_PART2 + Arrays.toString(needMIMETypes));
    }

    public UnsupportedMimeTypeException(String support, String accept) {
        super(EXCEPTION_TXT_PART1 + "[" + support + "], " + EXCEPTION_TXT_PART2 + "[" + accept + "]");
    }

    public UnsupportedMimeTypeException(String support, String accept, Throwable cause){
        super(EXCEPTION_TXT_PART1 + "[" + support + "], " + EXCEPTION_TXT_PART2 + "[" + accept + "]", cause);
    }

    public UnsupportedMimeTypeException(File file, String[] needMIMETypes, Throwable cause){
        super(EXCEPTION_TXT_PART1 + "[" + getFileType(file) + "], " + EXCEPTION_TXT_PART2 + Arrays.toString(needMIMETypes), cause);
    }
    public UnsupportedMimeTypeException(String support, String[] needMINETypes){
        super(EXCEPTION_TXT_PART1 + "[" + support + "], " + EXCEPTION_TXT_PART2 + Arrays.toString(needMINETypes));
    }
    public UnsupportedMimeTypeException(String support, String[] needMINETypes, Throwable cause){
        super(EXCEPTION_TXT_PART1 + "[" + support + "], " + EXCEPTION_TXT_PART2 + Arrays.toString(needMINETypes), cause);
    }
}
