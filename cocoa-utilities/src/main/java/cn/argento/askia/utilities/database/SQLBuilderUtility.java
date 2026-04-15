package cn.argento.askia.utilities.database;

import cn.argento.askia.annotations.Utility;

/**
 * 此工具类用于构建Sql语句
 */
@Utility("SQL生成器工具类")
public class SQLBuilderUtility {
    private SQLBuilderUtility(){
        throw new IllegalAccessError("SQLBuilderUtility为工具类, 无法创建该类的对象");
    }
}
