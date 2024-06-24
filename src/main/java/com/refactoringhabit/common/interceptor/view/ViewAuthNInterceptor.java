package com.refactoringhabit.common.interceptor.view;

import static com.refactoringhabit.common.enums.AttributeNames.MEMBER_ALT_ID;
import static com.refactoringhabit.common.enums.AttributeNames.SESSION_COOKIE_NAME;
import static com.refactoringhabit.common.enums.UriMappings.VIEW_HOME;
import static org.springframework.http.HttpStatus.NOT_FOUND;

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
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
@RequiredArgsConstructor
public class ViewAuthNInterceptor implements HandlerInterceptor {

    private final CookieUtil cookieUtil;
    private final TokenUtil tokenUtil;
    private final InterceptorUtils interceptorUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws IOException {

        if (handler instanceof HandlerMethod) {
            String nowUri = request.getRequestURI();
            Session sessionCookie = null;

            try {
                sessionCookie = cookieUtil.getValueInCookie(
                    request, SESSION_COOKIE_NAME.getName(), Session.class);

            // 이상이 있는 세션일 경우 해당 세션 네임의 쿠키를 리셋 시킴
            } catch (JsonMappingException e) {
                log.error("[{}] ex", e.getClass().getSimpleName(), e);
                cookieUtil.removeSessionCookie(response);
            }

            try {
                // null session only URIs
                if (interceptorUtils.isNullSessionOnlyUri(nowUri)) {
                    if (sessionCookie == null) {
                        return true;
                    }
                    response.sendRedirect(VIEW_HOME.getUri());
                    return false;
                }

                // public URIs, required authentication URIs
                if (sessionCookie != null) {
                    interceptorUtils.validateUserInDatabase(request,
                        tokenUtil.verifyToken(sessionCookie.accessToken()));
                    return true;
                }
                throw new NullTokenException();

            // public URIs, required authentication URIs
            } catch (TokenExpiredException e) { // expired token
                log.error("[{}] ex", e.getClass().getSimpleName(), e);
                interceptorUtils.handleExpiredToken(request, response,
                    tokenUtil.getClaimMemberId(sessionCookie.accessToken()));
                return true;

            } catch (Exception e) { // invalid token
                log.error("[{}] ex ", e.getClass().getSimpleName(), e);

                // public URIs
                if (interceptorUtils.isPublicUri(nowUri)) {
                    return true;
                }

                // required authentication URIs
                return interceptorUtils.redirectToLogin(request, response);
            }
        }
        response.setStatus(NOT_FOUND.value());
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler, ModelAndView modelAndView) {

        // 회원인 경우 request attribute에 아이디가 저장 되어있음
        String altId = (String) request.getAttribute(MEMBER_ALT_ID.getName());
        interceptorUtils.addMemberInfoToModel(modelAndView, altId);
    }
}
