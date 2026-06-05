package com.study.app.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.study.app.interceptors.TokenValidator;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private TokenValidator tokenValidator;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenValidator)
                .addPathPatterns("/activities/**")
                .addPathPatterns("/ai/**")
                .addPathPatterns("/member/profile/**");
    }
}
