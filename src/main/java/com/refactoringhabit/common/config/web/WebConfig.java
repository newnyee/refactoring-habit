package com.refactoringhabit.common.config.web;

import com.refactoringhabit.common.interceptor.view.GuestOnlyAccessInterceptor;
import com.refactoringhabit.common.interceptor.view.HostAccessInterceptor;
import com.refactoringhabit.common.interceptor.view.MemberAccessInterceptor;
import com.refactoringhabit.common.interceptor.view.MemberInfoInterceptor;
import com.refactoringhabit.common.interceptor.view.PublicAccessInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final GuestOnlyAccessInterceptor guestOnlyAccessInterceptor;
    private final PublicAccessInterceptor publicAccessInterceptor;
    private final MemberAccessInterceptor memberAccessInterceptor;
    private final HostAccessInterceptor hostAccessInterceptor;
    private final MemberInfoInterceptor memberInfoInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(guestOnlyAccessInterceptor)
            .order(1)
            .addPathPatterns(
                // api
                "/api/v2/auth/**",
                "/api/v2/members",
                "/api/v2/members/check-email",
                // view
                "/join", "/find-member/**", "/login")
            .excludePathPatterns("/api/v2/auth/sign-out");

        registry.addInterceptor(publicAccessInterceptor)
            .order(2)
            .addPathPatterns(
                // api
                "/api/v2/products/**", "/api/v2/large-categories/**",
                // view
                "/", "/category/**", "/product/**", "/search");

        registry.addInterceptor(memberAccessInterceptor)
            .order(3)
            .addPathPatterns(
                // api
                "/api/v2/**",
                // view
                "/mypage/**", "/wish/list", "/cart", "/order/**", "/host/**")
            .excludePathPatterns(
                "/api/v2/auth/sign-in",
                "/api/v2/auth/find-email",
                "/api/v2/auth/reset-password",
                "/api/v2/members",
                "/api/v2/members/check-email",
                "/api/v2/products/**",
                "/api/v2/large-categories/**");

        registry.addInterceptor(hostAccessInterceptor)
            .order(4)
            .addPathPatterns(
                "/api/v2/hosts/**",
                "/host/**")
            .excludePathPatterns(
                "/api/v2/hosts",
                "/host/join");

        registry.addInterceptor(memberInfoInterceptor)
            .order(5)
            .addPathPatterns("/**")
            .excludePathPatterns("/api/v2/**");
    }
}
