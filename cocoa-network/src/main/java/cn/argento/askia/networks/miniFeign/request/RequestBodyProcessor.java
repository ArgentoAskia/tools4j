package cn.argento.askia.networks.miniFeign.request;

import cn.argento.askia.networks.miniFeign.Processor;

import java.lang.annotation.Annotation;

public interface RequestBodyProcessor extends Processor {

    Object processRequestBody(Annotation requestBodyAnnotation, Object param);
}
