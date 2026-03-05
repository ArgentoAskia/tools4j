package cn.argento.askia.networks.miniFeign.caller;

import cn.argento.askia.networks.miniFeign.supports.HttpClient;
import cn.argento.askia.networks.miniFeign.supports.HttpRequestMethod;

public interface HttpCaller<HEADERS, BODY> extends Caller{


    HttpClient<HttpCaller<HEADERS, BODY>> getHttpClient();

    Object http(String url, HttpRequestMethod method, HEADERS headers, BODY body);
}
