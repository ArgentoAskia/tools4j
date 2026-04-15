package cn.argento.askia.utilities.system;

import cn.argento.askia.annotations.Utility;

@Utility("桌面端操作工具类")
public class DesktopUtility {
    private DesktopUtility() {
        throw new IllegalAccessError("DesktopUtility为工具类, 无法创建该类的对象");
    }
}
