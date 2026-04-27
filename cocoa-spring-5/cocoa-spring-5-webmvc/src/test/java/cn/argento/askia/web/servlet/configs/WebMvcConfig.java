package cn.argento.askia.web.servlet.configs;

import cn.argento.askia.web.servlet.interceptors.InterceptorAnnotationProcessor;
import cn.argento.askia.web.servlet.interceptors.ParentController;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebMvcConfig implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorAnnotationProcessor.process(ParentController.class, registry);
    }
}
