package com.refactoringhabit.common.interceptor.view;

import static com.refactoringhabit.common.enums.AttributeNames.MEMBER_ALT_ID;
import static com.refactoringhabit.common.enums.UriMappings.*;

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
public class ViewAuthZInterceptor implements HandlerInterceptor {

    private final InterceptorUtils interceptorUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws IOException {

        try {
            String memberAltId = (String) request.getAttribute(MEMBER_ALT_ID.getName());

            // 호스트 -  controller 진입
            if (interceptorUtils.isMemberHostById(memberAltId)) {
                return true;
            }

            // 일반 회원 - 호스트 가입 페이지로 리다이렉트
            return interceptorUtils.redirectToUrl(response, VIEW_HOST_JOIN.getUri());

        } catch (Exception e) {
            log.error("[{}] ex ", e.getClass().getSimpleName(), e);

            return interceptorUtils.redirectToLogin(request, response);
        }
    }
}
