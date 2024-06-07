package com.refactoringhabit.common.interceptor.view;

import static com.refactoringhabit.common.enums.AttributeNames.MEMBER_ALT_ID;
import static com.refactoringhabit.common.enums.UrlMappings.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.refactoringhabit.common.utils.interceptor.InterceptorUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class HostAccessInterceptor implements HandlerInterceptor {

    private final InterceptorUtils interceptorUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws IOException {

        boolean isApiUrl = interceptorUtils.isApiUrl(request);
        try {
            String altId = (String) request.getAttribute(MEMBER_ALT_ID.getName());

            // 호스트인 경우 controller 진입
            if (interceptorUtils.isMemberHostById(altId)) {
                return true;
            }

            // 일반 회원인 경우
            if (isApiUrl) { // api 호출
                response.setStatus(FORBIDDEN.value());
                return false;
            }

            // view 호출
            return interceptorUtils.redirectToUrl(response, VIEW_HOST_JOIN.getUrl());

        } catch (Exception e) {
            log.error("[{}] ex ", e.getClass().getSimpleName(), e);

            // api 호출
            if (isApiUrl) {
                response.setStatus(UNAUTHORIZED.value());
                return false;
            }

            // view 호출
            return interceptorUtils.redirectToLogin(request, response);
        }
    }
}
