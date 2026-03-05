package cn.argento.askia.networks.miniFeign.request;

import cn.argento.askia.networks.miniFeign.Processor;

import java.lang.annotation.Annotation;
import java.util.Map;

public interface RequestPathVariableProcessor extends Processor {

    /**
     * PathVariable处理接口, 用于处理PathVariable
     *
     * @param urlOriginal 原始的，包含PathVariable{}格式的URL
     * @param pathVariablesFromUrl 捕获方法上标记了@PathVariable、@HttpPathVariable等注解的注解参数
     * @param pathVariablesFromMethodParams 捕获了实际调用声明式API时和PathVariable对应的参数
     * @return 经过处理之后的, 融合PathVariable之后的API
     */
    String processPathVariables(String urlOriginal, Map<String, Annotation> pathVariablesFromUrl, Map<String, Object> pathVariablesFromMethodParams);
}
