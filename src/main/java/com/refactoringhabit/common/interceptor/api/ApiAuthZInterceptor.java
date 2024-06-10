package com.refactoringhabit.common.interceptor.api;

import static com.refactoringhabit.common.enums.AttributeNames.MEMBER_ALT_ID;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.refactoringhabit.common.utils.interceptor.InterceptorUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiAuthZInterceptor implements HandlerInterceptor {

    private final InterceptorUtils interceptorUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) {

        try {
            String altId = (String) request.getAttribute(MEMBER_ALT_ID.getName());

            // 호스트 - controller 진입
            if (interceptorUtils.isMemberHostById(altId)) {
                return true;
            }

            // 일반 회원 - 권한 없음
            response.setStatus(FORBIDDEN.value());
            return false;

        } catch (Exception e) {
            log.error("[{}] ex ", e.getClass().getSimpleName(), e);

            response.setStatus(UNAUTHORIZED.value());
            return false;
        }
    }
}
