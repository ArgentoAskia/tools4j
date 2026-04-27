package cn.argento.askia.web.servlet;

import cn.argento.askia.web.servlet.configs.WebMvcConfig;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

public class Main {
    public static void main(String[] args) {
        WebMvcConfig webMvcConfig = new WebMvcConfig();
        InterceptorRegistry interceptorRegistry = new InterceptorRegistry();
        webMvcConfig.addInterceptors(interceptorRegistry);
        System.out.println(interceptorRegistry);
    }
}
