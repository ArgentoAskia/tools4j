package cn.argento.askia.utilities.database;

import cn.argento.askia.annotations.Utility;

/**
 * 和JDBC相关的工具类
 */
@Utility("JDBC工具类")
public class JdbcUtility {

    private JdbcUtility(){
        throw new IllegalAccessError("ArrayUtility为工具类, 无法创建该类的对象");
    }
}
