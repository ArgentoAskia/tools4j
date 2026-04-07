/**
 * <h3>cocoa-core package statement</h3>
 *
 * <p>copyright part</p>
 *
 * <p>这是核心包中异常部分的说明:</p>
 * <p>众所周知，{@code Java}中的所有异常都可以分为三大类：</p>
 * <ul>
 *     <li>错误({@link java.lang.Error})：系统发生严重错误导致不得不终止运行</li>
 *     <li>运行时异常({@link java.lang.RuntimeException})：系统在运行过程中遇到一些可以被检查的逻辑错误，比如除0等</li>
 *     <li>捕获型异常({@link java.lang.Exception})：这类异常被设计为兜底使用，即当某些运行条件不满足时，不希望返回内容而是以抛出异常的形式作为兜底策略使用</li>
 * </ul>
 * <p>而在{@code Java}体系中, 异常类相当之多, 种类也广, 实际编码中常常会遇到希望抛出某种场景的异常，但是找不到JDK中提供这种场景的异常类的情况,这种情况发生的原因三，要么就是不清楚JDK中的异常类，要么就是JDK中没有此类异常，或者就是只有运行时（捕获型），但你刚好需要捕获型（运行时）。因此，此核心包的目的有两：</p>
 * <ol>
 *     <li>希望通过此包的文档, 能够帮助包使用者梳理JDK中的异常系统</li>
 *     <li>为一些扩展的需求补足其捕获型或者是运行时的异常</li>
 * </ol>
 *
 * <h4>JDK 异常系统</h4>
 * <p>todo 说明JDK中各类异常信息分类</p>
 *
 * @author Admin
 */
package cn.argento.askia.exceptions;