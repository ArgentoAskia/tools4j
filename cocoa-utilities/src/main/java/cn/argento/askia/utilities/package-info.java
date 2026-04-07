/**
 All Useful Utilities, including annotation, reflection, compilation, beans, keys, random, IOStream and so on.
 <p>工具类列表如下：
     <ol>
         <li>{@code cn.argento.askia.utilities.algorithms.*} 此为算法包，包含各类算法总结，以及各类算法的原始方法</li>
         <li>{@code cn.argento.askia.utilities.annotation.*} 此为注解包，内含各种处理注解的工具类</li>
         <li>{@code cn.argento.askia.utilities.bean.*} 此为JavaBean包，内含JavaBean相关的内容如JavaBean内省，Bean反射，值访问，值编辑器等相关的工具类</li>
         <li>{@code cn.argento.askia.utilities.calc.*} 计算工具类包，包含各类数值计算工具类</li>
         <li>{@code cn.argento.askia.utilities.classes.*} 包含类文件，字节码，类加载，资源加载等工具类</li>
         <li>{@code cn.argento.askia.utilities.collection.*} 数据结构相关工具类，包含集合框架JCF, 数组工具类等</li>
         <li>{@code cn.argento.askia.utilities.database.*} 数据库相关工具类, 包含数据库元素据, Datasource, SQLBuilder等工具类</li>
         <li>{@code cn.argento.askia.utilities.datetime.*} 日期时间相关工具类, 包含时间对象创建、计算、判别、表达和转换，注意此包不包含任何时间格式化相关的内容, 如需格式化请查看text包</li>
         <li>{@code cn.argento.askia.utilities.files.*} 各类文件的相关处理工具类，比如Json, pdf等, 以及文件信息相关的工具类, 比如File, Files</li>
         <li>{@code cn.argento.askia.utilities.functional.*} 函数式编程相关的工具类, 包含操作流Stream, MapReduce封装, 函数式接口调用, 回调等相关工具类</li>
         <li>{@code cn.argento.askia.utilities.generate.*} 各类生成式内容的工具类, 比如UUID, CDK, 随机内容(文本, 数值)等等</li>
         <li>{@code cn.argento.askia.utilities.graphic.*} 图形动画相关的工具类, 包括Java2D的封装, 动画等等</li>
         <li>{@code cn.argento.askia.utilities.io.*} io和nio相关的所有工具类, 包括IOStream, Buffer, channel, tcp Socket, udp Socket等</li>
         <li>{@code cn.argento.askia.utilities.lang.*} 语言层面的工具类, 包括字符串, java数据类型转换, 断言等等</li>
         <li>{@code cn.argento.askia.utilities.network.*} 网络相关的工具类, 包括网络接口信息(网卡), 应用层协议如HTTP, FTP等等</li>
         <li>{@code cn.argento.askia.utilities.reflect.*} 反射相关的工具类</li>
         <li>{@code cn.argento.askia.utilities.script.*} 脚本相关的工具类，包括脚本执行和本地方法执行(JNI、JNA等)</li>
         <li>{@code cn.argento.askia.utilities.security.*} 安全相关工具类, 如加密解密，hash等</li>
         <li>{@code cn.argento.askia.utilities.system.*} 系统相关的工具类，包括系统信息, 平台信息, 桌面操作等等</li>
         <li>{@code cn.argento.askia.utilities.text.*} 文本处理和格式化相关的工具类, 包含各类信息的格式化方法</li>
     </ol>
 <hr>
 <h2>特别说明：</h2>
 <ol>
    <li><b>本说明随着时间的推移可能会增加或者删除(<del>应该很少吧QAQ</del>)</b></li>
    <li>所有的工具类以 <b>{@code XXXXUtility}</b> 命名, 原因是作者偏向于使用全称而非 {@code XXXUtil} 这样的短称</li>
    <li>每个工具类的文档说明硬性要求<b>写清楚功能简介</b>及其<b>对应的方法名</b>以及<b>版本信息, 作者信息, 外部链接</b>等</li>
    <li>每个工具类内的静态方法要求<b>写清楚功能简介</b>, <b>使用举例</b>, <b>注意事项</b>, <b>引入时间(since)</b>, 如果涉及到原理, 则需要<b>说明原理</b>！</li>
    <li>版本号<b style="color:red">前面两位数乱写的</b>, 一般第一位是大版本, 第二位是小版本, 第三位是<b>代码的最后修改时间</b>, 如果修改时间是 {@code X}则代表是历史遗留代码, 修改时间未知！</li>
    <li>所有的工具类的 {@code Update Log} 和 {@code BugFix} 都会写在这个 {@code package-info.java} 里</li>
    <li>好像暂时就这么多...</li>
 </ol>
 <hr>
 <h2>更新日志&amp;BugFix</h2>
 <ol>
    <li>2024.5.14 - 添加 {@code Utilities} 包 {@code package-info.java}</li>
    <li>2024.5.15 - 更新 {@code Utilities} 包 {@code package-info.java}: 添加工具类说明</li>
 </ol>
 <hr>
 <h2>碎碎念...</h2>
 <b>下面的内容都是作者在写文档和代码时的精神状态描述, 全程废话无一点营养, 觉得看的开心就给个 {@code Star} 吧, 呜呜呜~~~</b>

 @author Askia
 @since 1.0.2024-05-14
 @version 1.0.2024-05-14
 */
package cn.argento.askia.utilities;


