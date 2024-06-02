package com.refactoringhabit.common.config.web;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final HandlerInterceptor authenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
            .addPathPatterns(
                "/api/v2/auth/verify-token",
                "/api/v2/auth/tokens")

            .excludePathPatterns(
                "/css/**",
                "/js/**",
                "/img/**",
                "/storage/**",
                // view
                "/",
                "/join",
                "/find-member/**",
                "/login",
                //api
                "/api/v2/auth/find-email",
                "/api/v2/auth/reset-password",
                "/api/v2/auth/sign-in",
                "/api/v2/members",
                "/api/v2/members/check-email"
            );
    }
}
