package cn.argento.askia.networks.miniFeign.proxy;//package cn.argento.askia.networks.miniFeign.proxy;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.node.ArrayNode;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.*;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.client.RestTemplate;
//
//import java.lang.reflect.*;
//import java.util.*;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
///**
// * 接口处理器(一个接口一个Handler！多Handler处理！)
// */
//@Slf4j
//public class ModuleApiProxy implements InvocationHandler {
//
//    private String host;
//    //  兜底使用的impl(如果方法上没有标记@HttpCaller时, 所有的方法都要转发到这个实现上, 通常他是各个模块上不公开的Controller实例)
//    private Object impl;
//
//    // 解析结果
//    private Class<?> apiImplClass;
//
//    public <Api> ModuleApiProxy(String host, Object impl, Class<Api> apiClass) {
//        if (host.endsWith("/")){
//            // 去除URL最后的/,防止重复的/
//            this.host = host.substring(0, host.length() - 1);
//        }
//        else{
//            this.host = host;
//        }
//        if (impl != null){
//            this.impl = impl;
//            final Class<?>[] interfaces = this.impl.getClass().getInterfaces();
//            // 如果impl中继承了代理接口且impl中标记了@Controller或者@RestController
//            if (ArrayUtility.contain(interfaces, apiClass) && this.impl.getClass().isAnnotationPresent(Controller.class) || this.impl.getClass().isAnnotationPresent(RestController.class)){
//                // 直接获取接口方法上的@PostMapping等注解
//                apiImplClass = impl.getClass();
//            }
//            else{
//                // 不设置apiClass, 默认获取@HttpCaller注解中的内容进行发送请求
//                apiImplClass = null;
//            }
//            // 我们把判断提前到代理创建时
//        }
//        else{
//            // 没有实现
//            apiImplClass = null;
//            this.impl = null;
//        }
//
//    }
//
//    // 此时该类的被代理对象应该一定是带@HttpApi的
//    @Override
//    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        // 1. 我们首先获取Proxy对象上继承的单个接口类
//        final Class<?>[] interfaces = proxy.getClass().getInterfaces();
//        if (interfaces.length != 1){
//            // 代理对象代理了多个接口
//            throw new IllegalArgumentException("Proxy has implement more than 1 interface, Proxy can not solve with Multiple @HttpApi Interfaces" + Arrays.toString(interfaces));
//        }
//        // 请求方法
//        HttpMethod httpMethod = null;
//        String url = null;
//        if (apiImplClass != null){
//            // 如果apiImplClass存在, 则根据继承规则来处理
//            // 我们首先你尝试判断Controller类上是否标记了@RequstMapping
//            String urlPrefix = "";
//            if (apiImplClass.isAnnotationPresent(RequestMapping.class)){
//                final RequestMapping annotation = apiImplClass.getAnnotation(RequestMapping.class);
//                // 从Path中获取
//                final String[] path = annotation.path();
//                if (path.length > 0){
//                    urlPrefix = path[0];
//                }
//                else {
//                    // 否则从默认中获取
//                    final String[] value = annotation.value();
//                    if (value.length > 0){
//                        urlPrefix = value[0];
//                    }
//                }
//            }
//            // 判断是否标记了@HttpCallerRef
//            try{
//                if (method.isAnnotationPresent(HttpCallerRef.class)){
//                    // 标记了@HttpCallerRef, 则尝试获取方法上的各种注解
//                    // 首先获取实现类上的同名方法
//                    final Method methodInImpl = this.apiImplClass.getMethod(method.getName(), method.getParameterTypes());
//                    if (methodInImpl.isAnnotationPresent(RequestMapping.class)){
//                        final RequestMapping annotation = methodInImpl.getAnnotation(RequestMapping.class);
//                        url = urlPrefix + (annotation.value().length > 0? annotation.value()[0] : annotation.path().length > 0? annotation.path()[0] : "");
//                        httpMethod = annotation.method().length > 0? HttpMethod.valueOf(annotation.method()[0].name()): null;
//                    }
//                    else if (methodInImpl.isAnnotationPresent(PostMapping.class)){
//                        final PostMapping annotation = methodInImpl.getAnnotation(PostMapping.class);
//                        url = urlPrefix + (annotation.value().length > 0? annotation.value()[0] : annotation.path().length > 0? annotation.path()[0] : "");
//                        httpMethod = HttpMethod.POST;
//                    }
//                    else if (methodInImpl.isAnnotationPresent(GetMapping.class)){
//                        final GetMapping annotation = methodInImpl.getAnnotation(GetMapping.class);
//                        url = urlPrefix + (annotation.value().length > 0? annotation.value()[0] : annotation.path().length > 0? annotation.path()[0] : "");
//                        httpMethod = HttpMethod.GET;
//                    }
//                    else if (methodInImpl.isAnnotationPresent(PutMapping.class)){
//                        final PutMapping annotation = methodInImpl.getAnnotation(PutMapping.class);
//                        url = urlPrefix + (annotation.value().length > 0? annotation.value()[0] : annotation.path().length > 0? annotation.path()[0] : "");
//                        httpMethod = HttpMethod.PUT;
//                    }
//                    else if (methodInImpl.isAnnotationPresent(PatchMapping.class)){
//                        final PatchMapping annotation = methodInImpl.getAnnotation(PatchMapping.class);
//                        url = urlPrefix + (annotation.value().length > 0? annotation.value()[0] : annotation.path().length > 0? annotation.path()[0] : "");
//                        httpMethod = HttpMethod.PATCH;
//                    }
//                    else if (methodInImpl.isAnnotationPresent(DeleteMapping.class)){
//                        final DeleteMapping annotation = methodInImpl.getAnnotation(DeleteMapping.class);
//                        url = urlPrefix + (annotation.value().length > 0? annotation.value()[0] : annotation.path().length > 0? annotation.path()[0] : "");
//                        httpMethod = HttpMethod.DELETE;
//                    }
//                    // call http
//                    log.debug("----------------- HTTP CALL REPORT -----------------");
//                    return httpCall(url, method, args, httpMethod);
//                }
//            } catch (Exception e){
//                // 发生任何的异常我们都开始尝试@HttpCaller
//                e.printStackTrace();
//                log.debug("自动化内部调用发生异常, 开始扫描@HttpCaller进行二次调用...");
//            }
//        }
//        // 如果apiImplClass失败或者在处理apiImplClass时失败
//        // 然后我们判断在该接口上是否标记了@HttpCaller, 如果标记了, 则我们走HTTP, 否则我们走原始调用！
//        if (method.isAnnotationPresent(HttpCaller.class)){
//            // 获取请求URL和请求方式
//            final HttpCaller httpCaller = method.getAnnotation(HttpCaller.class);
//            // 请求方法
//            httpMethod = httpCaller.method();
//            // url
//            url = httpCaller.value();
//            log.debug("----------------- HTTP CALL REPORT -----------------");
//            return httpCall(url, method, args, httpMethod);
//        }
//        else{
//            // 啥也没标记，则走原始调用
//            log.debug("方法未标记@HttpCaller, 因此走原始代理实现类, 方法名：{}, 参数：{}", method.getName(), Arrays.toString(args));
//            return method.invoke(impl, args);
//        }
//    }
//
//    private Object httpCall(String url, Method method, Object[] args, HttpMethod httpMethod) throws JsonProcessingException, YumitoyException {
//        // 开始处理Http调用
//        final List<String> pathVariables = getPathVariables(url);
//        //获取方法参数和HTTP参数
//        Map<String, Object> pathVariableMap = new HashMap<>();
//        Map<String, Object> httpQueryParamMap = new HashMap<>();
//        Map<String, Object> httpRequestHeaderMap = new HashMap<>();
//        // 解析参数
//        Object requestBody = doHttpRequestMethodParams(method, args, pathVariableMap, httpQueryParamMap, httpRequestHeaderMap);
//        // 检查URL中的pathVariables是否和方法参数中的相同
//        checkUrlAndParameterPathVariables(pathVariables, pathVariableMap);
//
//        // 3. 加工URL
//        // 替换pathVariable
//        url = doPathVariables(url, pathVariableMap, pathVariables);
//        // 拼接QueryParam
//        url = doQueryParams(url, httpQueryParamMap);
//
//        // 4. 发送HTTP请求
//        final ResponseEntity<String> httpResult = http(httpMethod, host, url, httpRequestHeaderMap, requestBody);
//
//        // 5. 解析Response
//        return solveWithResponse(method, httpResult);
//    }
//
//    private String doPathVariables(String url, Map<String, Object> pathVariableMap, List<String> pathVariables) {
//
//        for (String pathVariable : pathVariables){
//            final Object o = pathVariableMap.get(pathVariable);
//            // fixme: 用户可能提供了null参数, (此处我们应该尽可能对标@PathVariable(request = false))
//            if (o == null){
//                url = url.replace("/{" + pathVariable + "}", "");
//            }
//            else {
//                url = url.replace("{" + pathVariable + "}", o.toString());
//            }
//
//        }
//        return url;
//    }
//    // 发送HTTP
//    private ResponseEntity<String> http(HttpMethod httpMethod, String host, String url, Map<String, Object> httpRequestHeaderMap, Object body){
//        final RestTemplate restTemplate = RestTemplateUtility.getInstance().getRestTemplate();
//        HttpEntity<?> httpEntity = null;
//        if (body != null && httpRequestHeaderMap != null && httpRequestHeaderMap.size() > 0){
//            HttpHeaders headers = new HttpHeaders();
//            final Set<String> keySet = httpRequestHeaderMap.keySet();
//            for (String key: keySet
//                 ) {
//                //  尽量传toString的值
//                final String s = httpRequestHeaderMap.get(key).toString();
//                headers.add(key, s);
//            }
//            httpEntity = new HttpEntity<>(body, headers);
//        }
//        else if (body != null){
//            httpEntity = new HttpEntity<>(body);
//        }
//        else if (httpRequestHeaderMap != null && httpRequestHeaderMap.size() > 0){
//            HttpHeaders headers = new HttpHeaders();
//            final Set<String> keySet = httpRequestHeaderMap.keySet();
//            for (String key: keySet
//            ) {
//                //  尽量传toString的值
//                final String s = httpRequestHeaderMap.get(key).toString();
//                headers.add(key, s);
//            }
//            httpEntity = new HttpEntity<>(headers);
//        }
//        log.debug("httpEntry = {}", httpEntity);
//        return restTemplate.exchange(host + url, httpMethod, httpEntity, String.class);
//    }
//    private Object solveWithResponse(Method method, ResponseEntity<String> httpResult) throws JsonProcessingException, YumitoyException {
//        // 获取返回值
//        final Type genericReturnType = method.getGenericReturnType();
//        log.debug("响应报文 = {}, 方法返回值 = {}", StringUtility.makeEllipsisString(httpResult.toString(), "...>", 60), genericReturnType);
//        log.debug("------------------------------------------------------");
//        // 解析返回值
//        Class<?> returnType = null;
//        boolean isGenericType = false;
//        Class<?> genericType = null;
//        log.debug("genericReturnType = {}, name = {}", genericReturnType, genericReturnType.getTypeName());
//        if (httpResult.getBody() == null){
//            // 空请求体
//            return null;
//        }
//        // 返回基本类型
//        if (genericReturnType instanceof Class<?>){
//            returnType = (Class<?>) genericReturnType;
//            if (Response.class == returnType){
//                if (httpResult.getStatusCode().is2xxSuccessful()) {
//                    final String responseEntityBody = httpResult.getBody();
//                    // 一些报错信息可能会返回在这里，但没关系，只要是响应码为200都ok，插入不了历史数据我们大不了回滚或者报错！
//                    return Response.valueOfJson(responseEntityBody);
//                }
//                else {
//                    // 组装调用失败消息
//                    // 内部调用失败, 可能服务没开, 或者网络原因, 此时但也没关系, 因为最多我们不插入历史即可（少一两条历史记录无所谓）
//                    return Response.fail(Response.FAIL, "内部调用失败");
//                }
//            }
//            else if (returnType.isArray()){
//                //  数组类型返回值
//                final Class<?> componentType = returnType.getComponentType();
//                //  解析(ObjectMapper)
//                if (httpResult.getStatusCode().is2xxSuccessful()) {
//                    final String responseEntityBody = httpResult.getBody();
//                    // 一些报错信息可能会返回在这里，但没关系，只要是响应码为200都ok，插入不了历史数据我们大不了回滚或者报错！
//                    Object[] array = readValueAsArray(responseEntityBody, componentType);
//                    return array;
//                }
//                else {
//                    // 如果响应失败则返回默认值
//                    final Object o = ArrayUtility.newArray(componentType, 0);
//                    log.warn("响应失败: {}, 返回默认值 = {}", httpResult, o);
//                    return o;
//                }
//            }
//            else{
//                // JavaBean对象(直接解析)
//                if (httpResult.getStatusCode().is2xxSuccessful()){
//                    // 直接转
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    final Object o = objectMapper.readValue(httpResult.getBody(), returnType);
//                    return o;
//                }
//                else {
//                    // 组装调用失败消息
//                    // 内部调用失败, 可能服务没开, 或者网络原因, 此时但也没关系, 因为最多我们不插入历史即可（少一两条历史记录无所谓）
//                    return Response.fail(Response.FAIL, "内部调用失败");
//                }
//            }
//        }
//        //  返回泛型返回值
//        else if (genericReturnType instanceof ParameterizedType){
//            // 泛型类型
//            ParameterizedType parameterizedType = (ParameterizedType) genericReturnType;
//            // 处理Map
//            if (parameterizedType.getRawType() == Map.class) {
//                final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
//                // fixme: 我们暂时只解析简单的Map泛型,比如: Map<String, Object>
//                if (actualTypeArguments.length == 2 && actualTypeArguments[0] == String.class && actualTypeArguments[1] == Object.class){
//                    if (httpResult.getStatusCode().is2xxSuccessful()){
//                        // 另外一种解析方式！
//                        ObjectMapper objectMapper = new ObjectMapper();
//                        final Map map = objectMapper.readValue(httpResult.getBody(), Map.class);
//                        return map;
//                    }
//                    else {
//                        // 组装调用失败消息
//                        // 内部调用失败, 可能服务没开, 或者网络原因, 此时但也没关系, 因为最多我们不插入历史即可（少一两条历史记录无所谓）
//                        return Response.fail(Response.FAIL, "内部调用失败");
//                    }
//                }
//                else{
//                    throw YumitoyException.throwYumitoyException(HttpStatus.NOT_IMPLEMENTED.value(),
//                            ModuleApiProxy.class.getSimpleName(), "solveWithResponse", 318, "暂不支持解析复杂的Map，我们仅支持Map<String, Object>, 而您方法的类型为" + Arrays.toString(actualTypeArguments));
//                }
//            }
//            // List
//            else if (parameterizedType.getRawType() == List.class){
//                final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
//                // fixme: 我们暂时只解析简单的Map泛型,比如: List<具体的类型>
//                if (actualTypeArguments.length == 1 && actualTypeArguments[0] instanceof Class<?>){
//                    if (httpResult.getStatusCode().is2xxSuccessful()) {
//                        final String responseEntityBody = httpResult.getBody();
//                        // 一些报错信息可能会返回在这里，但没关系，只要是响应码为200都ok，插入不了历史数据我们大不了回滚或者报错！
//                        Object[] array = readValueAsArray(responseEntityBody, (Class<?>) actualTypeArguments[0]);
//                        return new ArrayList<>(Arrays.asList(array));
//                    }
//                    else {
//                        // 如果响应失败则返回默认值
//                        final Object o = ArrayUtility.newArray((Class<?>) actualTypeArguments[0], 0);
//                        log.warn("响应失败: {}, 返回默认值 = {}", httpResult, o);
//                        return new ArrayList<>(Arrays.asList(ArrayUtility.getAll(o)));
//                    }
//                }
//                else{
//                    throw YumitoyException.throwYumitoyException(HttpStatus.NOT_IMPLEMENTED.value(),
//                            ModuleApiProxy.class.getSimpleName(), "solveWithResponse", 341, "仅支持解析List<具体类型>, 而您的类型为" + Arrays.toString(actualTypeArguments));
//                }
//            }
//            else{
//                // fixme:泛型类型(暂时不支持)
//                throw YumitoyException.throwYumitoyException(HttpStatus.NOT_IMPLEMENTED.value(),
//                        ModuleApiProxy.class.getSimpleName(), "solveWithResponse", 348, "暂不支持解析非List、Set、Map的泛型参数, 而您的类型为" + parameterizedType);
//            }
//
//        }
//        else if (genericReturnType instanceof GenericArrayType){
//            throw YumitoyException.throwYumitoyException(HttpStatus.NOT_IMPLEMENTED.value(),
//                    ModuleApiProxy.class.getSimpleName(), "solveWithResponse", 355, "暂不支持解析泛型数组参数" + genericReturnType);
//        }
//        else if (genericReturnType instanceof WildcardType){
//            // 通配符泛型
//            throw YumitoyException.throwYumitoyException(HttpStatus.NOT_IMPLEMENTED.value(),
//                    ModuleApiProxy.class.getSimpleName(), "solveWithResponse", 359, "暂不支持解析通配符泛型参数" + genericReturnType);
//        }
//        else{
//            TypeVariable<?> typeVariable = (TypeVariable<?>) genericReturnType;
//            throw YumitoyException.throwYumitoyException(HttpStatus.NOT_IMPLEMENTED.value(),
//                    ModuleApiProxy.class.getSimpleName(), "solveWithResponse", 363, "暂不支持解析类型变量参数" + genericReturnType);
//        }
//    }
//
//    // 将数据转为数组
//    private Object[] readValueAsArray(String responseEntityBody, Class<?> componentType) throws JsonProcessingException, YumitoyException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        final JsonNode jsonNode = objectMapper.readTree(responseEntityBody);
//        if (jsonNode instanceof ArrayNode){
//            ArrayNode arrayNode = (ArrayNode) jsonNode;
//            Object[] ret = new Object[arrayNode.size()];
//            for (int i = 0; i < arrayNode.size(); i++) {
//                final JsonNode componentJsonNode = arrayNode.get(i);
//                final Object o = objectMapper.treeToValue(componentJsonNode, componentType);
//                ret[i] = o;
//            }
//            return ret;
//        }
//        throw YumitoyException.throwYumitoyException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                ModuleApiProxy.class.getSimpleName(), "readValueAsArray", 382, "解析失败, jsonNode的实际类型是" + jsonNode.getNodeType());
//    }
//
//    private String doQueryParams(String url, Map<String, Object> httpQueryParamMap){
//        StringBuilder urlBuilder = new StringBuilder(url).append("?");
//        final Set<String> keySet = httpQueryParamMap.keySet();
//        for (String key : keySet){
//            final Object value = httpQueryParamMap.get(key);
//            // 基本类型、字符串、或者包装器类则直接包装到字符串即可
//            final String s = String.valueOf(value);
//            urlBuilder.append(key).append("=").append(s).append("&");
//        }
//        // 去除最后的&或者如果没有参数,则去除最后的?
//        url = urlBuilder.replace(urlBuilder.length() - 1, urlBuilder.length(), "").toString();
//        return url;
//    }
//    private void checkUrlAndParameterPathVariables(List<String> pathVariables, Map<String, Object> pathVariableMap) {
//        // 路径中的pathVariable必须和方法中的一致
//        if (pathVariables.size() != pathVariableMap.size()){
//            throw new IllegalArgumentException("PathVariables中URL的和参数的不匹配");
//        }
//    }
//
//    private Object doHttpRequestMethodParams(Method method, Object[] args,
//                                             Map<String, Object> pathVariableMap,
//                                             Map<String, Object> httpQueryParamMap,
//                                             Map<String, Object> httpRequestHeaderMap){
//        final Parameter[] parameters = method.getParameters();
//        // 参数必须相等
//        assert parameters.length == args.length;
//        Object requestBody = null;
//        boolean requestBodyLock = false;
//        for (int i = 0; i < parameters.length; i++){
//            // 解析参数
//            if (parameters[i].isAnnotationPresent(HttpPathVariable.class)){
//                // 该参数是@HttpPathVariable
//                final HttpPathVariable httpPathVariable = parameters[i].getAnnotation(HttpPathVariable.class);
//                final Object arg = args[i];
//                // 进行参数融合
//                pathVariableMap.put(httpPathVariable.value(), arg);
//            }
//            else if (parameters[i].isAnnotationPresent(HttpQueryParam.class)){
//                // 该参数是@HttpQueryParam
//                final HttpQueryParam httpQueryParam = parameters[i].getAnnotation(HttpQueryParam.class);
//                final Object arg = args[i];
//                if (LangUtility.isBaseTypeObjectWidely(arg)){
//                    // 基本类型, 一定要写名称
//                    if (!"".equals(httpQueryParam.value())){
//                        httpQueryParamMap.put(httpQueryParam.value(), arg);
//                    }
//                    else{
//                        throw new IllegalArgumentException("基础类型一定要编写value()属性...");
//                    }
//                }
//                else{
//                    // JavaBean
//                    // 获取字段名和值
//                    final Map<String, Object> map = BeanUtility.toMap(arg);
//                    if (!"".equals(httpQueryParam.value())){
//                        // 带前缀, 我们就加上前缀放进去
//                        final String prefix = httpQueryParam.value();
//                        final Set<String> keySet = map.keySet();
//                        for (String key : keySet){
//                            final Object value = map.get(key);
//                            httpQueryParamMap.put(prefix + "." + key, value);
//                        }
//                    }
//                    else{
//                        // 否则我们就将JavaBean的属性名作为key
//                        httpQueryParamMap.putAll(map);
//                    }
//                }
//            }
//            else if (parameters[i].isAnnotationPresent(HttpRequestHeader.class)){
//                // HttpRequestHeader
//                final HttpRequestHeader httpRequestHeader = parameters[i].getAnnotation(HttpRequestHeader.class);
//                final Object arg = args[i];
//                httpQueryParamMap.put(httpRequestHeader.value(), arg);
//            }
//            else if (!requestBodyLock && parameters[i].isAnnotationPresent(HttpRequestBody.class)){
//                // 请求体
//                requestBody = args[i];
//                final HttpRequestBody httpRequestBody = parameters[i].getAnnotation(HttpRequestBody.class);
//                // 添加Content-Type
//                if (!"".equals(httpRequestBody.value())) {
//                    httpRequestHeaderMap.put(HttpHeaders.CONTENT_TYPE, httpRequestBody.value());
//                }
//                // 处理过一次请求体之后就可以锁住其他的@HttpRequestBody参数
//                requestBodyLock = true;
//            }
//            // 其他情况我们忽略此参数
//        }
//        return requestBody;
//    }
//    private List<String> getPathVariables(String url){
//        Pattern pattern = Pattern.compile("\\{([^{}]*)\\}");
//        Matcher matcher = pattern.matcher(url);
//        List<String> list = new ArrayList<>();
//        while (matcher.find()) {
//            list.add(matcher.group(1));  // group(1) 拿到捕获组
//        }
//        return list;
//    }
//}
