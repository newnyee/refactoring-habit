package com.refactoringhabit.common.config.web;

import com.refactoringhabit.common.interceptor.view.ViewAuthZInterceptor;
import com.refactoringhabit.common.interceptor.view.ViewAuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class ViewConfig implements WebMvcConfigurer {

    private final ViewAuthInterceptor viewAuthInterceptor;
    private final ViewAuthZInterceptor viewAuthZInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 인증
        registry.addInterceptor(viewAuthInterceptor)
            .addPathPatterns(
                "/", "/join", "/find-member/**", "/login", "/category/**", "/product/**",
                "/search-list", "/mypage/**", "/wish/list", "/cart", "/order/**", "/host/**");

        // 인가
        registry.addInterceptor(viewAuthZInterceptor)
            .addPathPatterns("/host/**")
            .excludePathPatterns("/host/join");
    }
}
