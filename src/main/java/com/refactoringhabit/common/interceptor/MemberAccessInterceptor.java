package com.refactoringhabit.common.interceptor;

import static com.refactoringhabit.common.utils.cookies.CookieAttributes.ACCESS_TOKEN_COOKIE_NAME;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.refactoringhabit.common.utils.TokenUtil;
import com.refactoringhabit.common.utils.cookies.CookieUtil;
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
public class MemberAccessInterceptor implements HandlerInterceptor {

    private final CookieUtil cookieUtil;
    private final TokenUtil tokenUtil;
    private final InterceptorUtils interceptorUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws IOException {

        String accessToken = cookieUtil.getTokenInCookie(request, ACCESS_TOKEN_COOKIE_NAME);
        try { // 토큰 검증 성공 - api or view 컨트롤러 진입
            interceptorUtils.validateUserInDatabase(request, tokenUtil.verifyToken(accessToken));
            return true;

        } catch (TokenExpiredException e) { // 만료된 토큰 - 토큰 재발급 후 api or view 컨트롤러 진입
            log.error("[{}] ex", e.getClass().getSimpleName(), e);
            interceptorUtils.handleExpiredToken(request, response, tokenUtil.getClaimMemberId(
                    tokenUtil.getTokenNumber(accessToken)));
            return true;

        } catch (Exception e) { // 유효하지 않은 토큰
            log.error("[{}] ex ", e.getClass().getSimpleName(), e);

            // api 호출
            if (interceptorUtils.isApiUrl(request)) {
                response.setStatus(UNAUTHORIZED.value());
                return false;
            }

            // view 호출
            return interceptorUtils.redirectToLogin(request, response);
        }
    }
}
