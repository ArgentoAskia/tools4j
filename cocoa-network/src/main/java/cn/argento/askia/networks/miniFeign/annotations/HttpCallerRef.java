package cn.argento.askia.networks.miniFeign.annotations;

import java.lang.annotation.*;

/**
 * 在方法上标记该注解会让该方法变成Http请求调用, 和@HttpCaller的区别是，使用该注解您无需编写任何内容
 * 默认会在子@Controller实现上获取接口方法的@PostMapping、@GetMapping、@PatchMapping、@RequestMapping等注解来拼接URL
 * 理论上我们建议在使用此注解时, 配合@HttpCaller注解使用, 以达到兜底的作用(当然如果您非常自信能够获取得到也可以单独使用！)
 *
 * 在分层式模块设计时，多数情况下模块会被分为service层、controller层【http链接】、dao层
 * 如果用户此时这三层架构已打包部署，并且希望提供声明式API，完全可以将controller层的控制器剥离开，只需稍微修改（去除逻辑，保留现有Spring注解），再加上此注解即可得到一份完整的声明式调用
 * 提供了新的代码层级的第四层API
 * 此注解的另外一大特色式提供了控制器原版的声明， 因此完全可以当作熔断来使用, 接口层【标记此注解】  熔断层实现【和实际控制器层完全一致】[---继承-->]控制器层
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface HttpCallerRef {

    Class<?> ref() default Void.class;
}
