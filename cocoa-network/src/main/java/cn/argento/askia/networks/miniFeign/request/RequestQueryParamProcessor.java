package cn.argento.askia.networks.miniFeign.request;

import cn.argento.askia.networks.miniFeign.Processor;

import java.lang.annotation.Annotation;
import java.util.Map;

public interface RequestQueryParamProcessor extends Processor {

    String processPathVariables(String urlOriginal, Map<String, Annotation> queryParamAnnotationMap, Map<String, Object> queryParamFromMethodParams);
}
