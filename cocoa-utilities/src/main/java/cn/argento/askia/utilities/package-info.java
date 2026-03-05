/**
 All Useful Utilities, including annotation, reflection, compilation, beans, keys, random, IOStream and so on.
 <p>
 类列表如下：
     <ol>
         <li>{@link cn.argento.askia.utilities.AnnotationUtility}: 注解工具类, 提供了获取注解的通用方法</li>
         <li>{@link cn.argento.askia.utilities.ArrayUtility}: 数组工具类, 用于处理数组</li>
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


