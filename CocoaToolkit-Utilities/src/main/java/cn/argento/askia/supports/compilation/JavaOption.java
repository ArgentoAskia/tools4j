package cn.argento.askia.supports.compilation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * // TODO: 2024/5/4 到底怎么设计这类啊？头疼！参数不一致！但是我又想用枚举！
 */
public enum  JavaOption {
    CLASSPATH("-classpath");


    List<String> options;

    JavaOption(String... options){
        this.options = new ArrayList<>();
        Collections.addAll(this.options, options);
    }

    // TODO: 2024/5/4 normalize the Strings
    public String[] normalize(){
        return options.toArray(new String[0]);
    }
}
