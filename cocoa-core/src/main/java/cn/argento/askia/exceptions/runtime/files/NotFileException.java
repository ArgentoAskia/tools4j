package cn.argento.askia.exceptions.runtime.files;

import java.nio.file.NoSuchFileException;

public class NotFileException extends NoSuchFileException {
    public NotFileException(String file, String other, String reason) {
        super(file, other, reason);
    }

    public NotFileException(String file){
        super(file, null, "不是一个有效的文件类型, 他很有可能是一个目录");
    }

    public NotFileException(String file, String reason){
        super(file, null, reason);
    }

}
