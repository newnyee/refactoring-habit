package com.refactoringhabit.common.config.web;

import com.refactoringhabit.common.interceptor.api.ApiAuthNInterceptor;
import com.refactoringhabit.common.interceptor.api.ApiAuthZInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class ApiConfig implements WebMvcConfigurer {

    private final ApiAuthNInterceptor apiAuthNInterceptor;
    private final ApiAuthZInterceptor apiAuthZInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 인증
        registry.addInterceptor(apiAuthNInterceptor)
            .addPathPatterns("/api/v2/**")
            .excludePathPatterns(
                "/api/v2/auth/**",
                "/api/v2/products/**",
                "/api/v2/large-categories/**",
                "/api/v2/members");

        // 인가
        registry.addInterceptor(apiAuthZInterceptor)
            .addPathPatterns("/api/v2/hosts/**")
            .excludePathPatterns("/api/v2/hosts");
    }
}
