package cn.argento.askia.networks.miniFeign.caller;

import cn.argento.askia.networks.miniFeign.supports.http.HttpClient;
import cn.argento.askia.networks.miniFeign.supports.http.HttpRequestMethod;

public interface HttpCaller<HEADERS, BODY> extends Caller{


    HttpClient<HttpCaller<HEADERS, BODY>> getHttpClient();

    Object http(String url, HttpRequestMethod method, HEADERS headers, BODY body);
}
