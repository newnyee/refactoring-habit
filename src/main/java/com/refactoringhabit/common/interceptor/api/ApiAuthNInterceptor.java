package com.refactoringhabit.common.interceptor.api;

import static com.refactoringhabit.common.enums.AttributeNames.SESSION_COOKIE_NAME;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.refactoringhabit.auth.domain.exception.NullTokenException;
import com.refactoringhabit.common.response.Session;
import com.refactoringhabit.common.utils.TokenUtil;
import com.refactoringhabit.common.utils.cookies.CookieUtil;
import com.refactoringhabit.common.utils.interceptor.InterceptorUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiAuthNInterceptor implements HandlerInterceptor {

    private final CookieUtil cookieUtil;
    private final TokenUtil tokenUtil;
    private final InterceptorUtils interceptorUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws IOException {

        if (handler instanceof HandlerMethod) {
            Session sessionCookie = null;

            try {
                sessionCookie = cookieUtil.getValueInCookie(
                    request, SESSION_COOKIE_NAME.getName(), Session.class);

            // 이상이 있는 세션일 경우 해당 세션 네임의 쿠키를 리셋 시킴
            } catch (JsonMappingException e) {
                log.error("[{}] ex", e.getClass().getSimpleName(), e);
                cookieUtil.removeSessionCookie(response);
            }

            try { // 토큰 검증 성공
                if (sessionCookie != null) {
                    interceptorUtils.validateUserInDatabase(request,
                        tokenUtil.verifyToken(sessionCookie.accessToken()));
                    return true;
                }
                throw new NullTokenException();

            } catch (TokenExpiredException e) { // 만료된 토큰
                log.error("[{}] ex", e.getClass().getSimpleName(), e);
                interceptorUtils.handleExpiredToken(request, response,
                    tokenUtil.getClaimMemberId(sessionCookie.accessToken()));
                return true;

            } catch (Exception e) { // 유효하지 않은 토큰
                log.error("[{}] ex ", e.getClass().getSimpleName(), e);
                response.setStatus(UNAUTHORIZED.value());
                return false;
            }
        }
        response.setStatus(NOT_FOUND.value());
        return false;
    }
}

