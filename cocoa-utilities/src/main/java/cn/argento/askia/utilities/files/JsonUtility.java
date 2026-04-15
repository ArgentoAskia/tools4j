package cn.argento.askia.utilities.files;


import cn.argento.askia.annotations.Utility;

@Utility("Json工具类")
public class JsonUtility {

    private JsonUtility(){
        throw new IllegalAccessError("JsonUtility为工具类, 无法创建该类的对象");
    }
}
