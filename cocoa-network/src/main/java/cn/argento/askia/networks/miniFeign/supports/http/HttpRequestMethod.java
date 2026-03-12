package cn.argento.askia.networks.miniFeign.supports.http;

import com.sun.istack.internal.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * HTTP 请求枚举类
 */
public enum HttpRequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;


    private static final Map<String, HttpRequestMethod> mappings = new HashMap<>(16);

    static {
        for (HttpRequestMethod httpMethod : values()) {
            mappings.put(httpMethod.name(), httpMethod);
        }
    }


    public static HttpRequestMethod resolve(@Nullable String method) {
        return (method != null ? mappings.get(method) : null);
    }

    public boolean matches(String method) {
        return name().equals(method);
    }
}
