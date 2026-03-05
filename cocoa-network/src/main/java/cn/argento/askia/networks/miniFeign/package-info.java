package cn.argento.askia.networks.miniFeign;


/*
组件功能：
    1. (用户没有控制器层代码)声明式接口调用
    使用步骤：用户定义一个Http请求接口 --> 标记相关请求注解 --> 使用MiniFeigns生成代理对象 --> 调用方法即调用Http请求

    步骤1： 要确定了使用接口代理模式【整体架构】 ==>
    确定使用代理模式来实现Http请求！
    是否支持类级别代理 ==> 支持
    是否支持继承？ ==> 支持
    是否支持默认方法 ==> 支持
    是否支持静态方法 ==> 支持
    如何编写？
        方法：
            返回值作为响应体【定义两类常用的特殊响应体，CommonResponse, HttpResponse】
            方法参数：请求头，请求体，QueryParam，PathVariable ==> 【仅针对变量形式，即要填的参数，固定参数开放注解】
            异常：绑定特定的Response状态
            方法名：http请求的唯一key
        类名/ 接口名：全限定 ==> http请求的唯一key


    步骤2：确定有哪些Http请求参数，如何标记注解，标记哪些注解【功能确定】
    // 根据Http分类，得出：GET POST... 7种
    // 请求：Body、Header、QueryParam、PathVariable
    // 响应：响应头、响应体、响应行

    // 结合步骤1 和步骤2进行设计 注解设计
    类名：标记@HttpApi ==> Api名称, host, port, protocol[协议]
            @HttpApi.Status ==> 当前状态，当前版本，发布时间
            @HttpApi.Update ==> 版本，时间，更新内容

    方法标记：标记@HttpCaller ==> 此方法是一个Http请求，请求URL, 请求方式
            返回值：
                   特殊响应体：CommonResponse ==> 携带，响应码，响应信息，数据，时间戳
                             HttpResponse   ==> 包含响应头部分
                   非特殊响应头：
                             响应码为200:    ==> 响应体
                             响应码为非200    ==> 如果配置了异常<==>响应状态码，抛出异常
                                                如果没有对应异常或者没有配置异常 ==> 触发熔断?Breaker设计
            扩展注解：@Get、@Post等等

    参数标记：@HttpRequestPathVariable @HttpRequestQueryParam @HttpRequestBody @HttpRequestHeader
            除了@HttpRequestBody之外，其他的都支持常量自定义


    步骤3：确定调用过程中的额外功能参数配置：切面记录？请求请求前后事件响应，异常处理，使用哪些Http框架、熔断参数配置等

    事件驱动切面 (对于声明式调用, 调用前, 调用时, 调用后) ==> 扩展更多选择【组装参数时】
    通用响应处理 ==> 异常处理
               ==> Breaker设计
    混合Http框架实现【support包中提供发Http支持】 ==> 熟悉各个Http框架


    步骤4：确定调用调用过程中的熔断处理，调用缓存设计、代理缓存设计
    接口扫描 ==> 兼容Spring ==>
    生成mapper             ==> Mapper缓存设计

    响应结果缓存设计 ==> 代理缓存 @Cacheable


    架构设计：



    2. (用户拥有控制器层代码)声明式接口代码生成[代码生成], 添加熔断功能
    // 代码生成工具类，即使编译工具类


    3. ()声明式调用文档生成

 */