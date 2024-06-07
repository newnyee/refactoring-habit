package com.refactoringhabit.common.interceptor.view;

import static com.refactoringhabit.common.utils.cookies.CookieAttributes.ACCESS_TOKEN_COOKIE_NAME;

import com.auth0.jwt.exceptions.TokenExpiredException;
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
        Object handler) {

        String accessToken = cookieUtil.getTokenInCookie(request, ACCESS_TOKEN_COOKIE_NAME);
        try { // 토큰 인증
            interceptorUtils.validateUserInDatabase(request, tokenUtil.verifyToken(accessToken));

        } catch (TokenExpiredException e) { // 만료된 토큰 - 토큰 재발급 후 컨트롤러 진입
            log.error("[{}] ex", e.getClass().getSimpleName(), e);
            interceptorUtils.handleExpiredToken(request, response, tokenUtil.getClaimMemberId(
                tokenUtil.getTokenNumber(accessToken)));

        } catch (Exception e) { // 유효하지 않은 토큰
            log.error("[{}] ex ", e.getClass().getSimpleName(), e);
        }
        return true;
    }
}
