package cn.argento.askia.exceptions;

import java.io.File;

/**
 *
 */
// TODO: 2024/5/4 重定义异常以支持显示编译信息
public class CompilationException extends Exception{
    private String error;
    public CompilationException(){
        super();
    }
    public CompilationException(File file, String msg){
        super("无法编译文件：[" + file.getAbsolutePath() + "]，存在语法错误！" + "\n" + msg);
        error = msg;
    }


    @Override
    public String getLocalizedMessage() {
        return super.getLocalizedMessage();
    }

    public String getError() {
        return error;
    }
}
