package cn.argento.askia.networks.miniFeign.request;

import cn.argento.askia.networks.miniFeign.Processor;

import java.lang.annotation.Annotation;
import java.util.Map;

public interface RequestHeaderProcessor extends Processor {

    /**
     *
     * @param requestHeaderAnnotations
     * @param requestHeaderParams
     * @return 实际的RequestHeader绑定值
     */
    Map<String, Object> processRequestHeaders(Map<String, Annotation> requestHeaderAnnotations, Map<String, Object> requestHeaderParams);
}
