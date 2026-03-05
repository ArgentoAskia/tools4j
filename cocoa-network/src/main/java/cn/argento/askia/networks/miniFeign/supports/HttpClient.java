package cn.argento.askia.networks.miniFeign.supports;


import cn.argento.askia.networks.miniFeign.caller.HttpCaller;
import lombok.Getter;

import java.util.Map;

@Getter
public class HttpClient<IMPL extends HttpCaller<?, ?>> {

    private static Map<Class<?>, HttpClient<?>> httpClients;
    @SuppressWarnings("rawtypes")
    public static <IMPL extends HttpCaller<?, ?>> void register(IMPL impl, String clientType, String version){
        Class<? extends HttpCaller> httpCallerClass = impl.getClass();
        HttpClient<IMPL> implHttpClient = new HttpClient<>(clientType, version, impl);
        httpClients.put(httpCallerClass, implHttpClient);
    }

//    static {
//        register();
//    }


//    public static final HttpClient RestTemplate = new HttpClient("RestTemplate", "5", null);
//    ApacheHttpClient()
//    Jdk11HttpClient()
//    HttpUrlConnection()
//    OkHttp()
//    Unirest()

    private String clientType;
    private String version;
    private IMPL impl;

    // 不能new
    private HttpClient(String clientType, String version, IMPL impl){}

}
