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
                .addPathPatterns("/api/**")
                .addPathPatterns("/member/profile/**")
                .excludePathPatterns( // 아래 주소들은 토큰 검증에서 제외(비회원 로그인 관련)
                        "/api/festivals",       // 축제 전체 목록 조회 API
                        "/api/festivals/sido"	// 지역 시도 필터 조회 API;
                );   

    }
}
