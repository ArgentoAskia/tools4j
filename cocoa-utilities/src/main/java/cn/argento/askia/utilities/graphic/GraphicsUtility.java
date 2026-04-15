package cn.argento.askia.utilities.graphic;

import cn.argento.askia.annotations.Utility;

/**
 * Java绘画相关API封装, 包含图形绘画、动画、图像、字体等API
 */
@Utility("绘图工具类")
public class GraphicsUtility {
    private GraphicsUtility(){
        throw new IllegalAccessError("GraphicsUtility为工具类, 无法创建该类的对象");
    }
}
