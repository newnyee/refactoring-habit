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
import org.springframework.web.servlet.ModelAndView;

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

                // 호스트 - 호스트 가입 페이지 진입
                if (request.getRequestURI().equals(VIEW_HOST_JOIN.getUri())) {
                    return interceptorUtils.redirectToUrl(response, VIEW_HOST_HOME.getUri());
                }
                return true;
            }

            // 일반 회원 - 호스트 가입 페이지 진입
            if (request.getRequestURI().equals(VIEW_HOST_JOIN.getUri())) {
                return true;
            }

            // 일반 회원 - 인가되지 않은 페이지 진입
            return interceptorUtils.redirectToUrl(response, VIEW_HOST_JOIN.getUri());

        } catch (Exception e) {
            log.error("[{}] ex ", e.getClass().getSimpleName(), e);
            return interceptorUtils.redirectToLogin(request, response);
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
        ModelAndView modelAndView) {

        String memberAltId = (String) request.getAttribute(MEMBER_ALT_ID.getName());
        interceptorUtils.addHostInfoToModel(memberAltId, modelAndView);
    }
}
