package cn.argento.askia.utilities.files;


import cn.argento.askia.annotations.Utility;

@Utility("Xml工具类")
public class XmlUtility {

    private XmlUtility(){
        throw new IllegalAccessError("XmlUtility为工具类, 无法创建该类的对象");
    }
}
