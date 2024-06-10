package com.refactoringhabit.common.interceptor.view;

import static com.refactoringhabit.common.enums.AttributeNames.SESSION_COOKIE_NAME;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.refactoringhabit.auth.domain.exception.NullTokenException;
import com.refactoringhabit.common.response.Session;
import com.refactoringhabit.common.utils.TokenUtil;
import com.refactoringhabit.common.utils.cookies.CookieUtil;
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
public class PublicAccessInterceptor implements HandlerInterceptor {

    private final CookieUtil cookieUtil;
    private final TokenUtil tokenUtil;
    private final InterceptorUtils interceptorUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws JsonProcessingException {

        Session sessionCookie = cookieUtil
            .getValueInCookie(request, SESSION_COOKIE_NAME.getName(), Session.class);

        try { // 토큰 인증
            if (sessionCookie != null) {
                interceptorUtils
                    .validateUserInDatabase(request,
                        tokenUtil.verifyToken(sessionCookie.accessToken()));
            } else {
                throw new NullTokenException();
            }

        } catch (TokenExpiredException e) { // 만료된 토큰 - 토큰 재발급 후 컨트롤러 진입
            log.error("[{}] ex", e.getClass().getSimpleName(), e);
            interceptorUtils.handleExpiredToken(request, response,
                tokenUtil.getClaimMemberId(sessionCookie.accessToken()));

        } catch (Exception e) { // 유효하지 않은 토큰
            log.error("[{}] ex ", e.getClass().getSimpleName(), e);
        }
        return true;
    }
}
