package cn.argento.askia.utilities.files;


import cn.argento.askia.annotations.Utility;

@Utility("压缩文件工具类")
public class ArchiveFileUtility {

    private ArchiveFileUtility(){
        throw new IllegalAccessError("ArchiveFileUtility为工具类, 无法创建该类的对象");
    }
}
