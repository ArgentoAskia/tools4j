package cn.argento.askia.networks.miniFeign.response;

import cn.argento.askia.networks.miniFeign.Processor;

public interface ResponseProcessor<RAW, RESULT> extends Processor {

    RESULT doResponse(RAW raw);
}
