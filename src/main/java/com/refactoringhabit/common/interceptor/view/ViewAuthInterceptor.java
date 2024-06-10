package com.refactoringhabit.common.interceptor.view;

import static com.refactoringhabit.common.enums.AttributeNames.MEMBER_ALT_ID;
import static com.refactoringhabit.common.enums.AttributeNames.SESSION_COOKIE_NAME;
import static com.refactoringhabit.common.enums.UriAccessLevel.NULL_SESSION_ONLY_URI;
import static com.refactoringhabit.common.enums.UriAccessLevel.PUBLIC_URI;
import static com.refactoringhabit.common.enums.UriMappings.VIEW_HOME;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.auth0.jwt.exceptions.TokenExpiredException;
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
public class ViewAuthInterceptor implements HandlerInterceptor {

    private final CookieUtil cookieUtil;
    private final TokenUtil tokenUtil;
    private final InterceptorUtils interceptorUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws IOException {

        if (handler instanceof HandlerMethod) {
            Session sessionCookie =
                cookieUtil.getValueInCookie(request, SESSION_COOKIE_NAME.getName(), Session.class);
            String nowUrI = request.getRequestURI();

            try {
                // null session only URIs
                if (NULL_SESSION_ONLY_URI.getUriMappingsList().stream()
                    .anyMatch(uriMappings -> nowUrI.startsWith(uriMappings.getUri()))) {
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
                if (nowUrI.equals(VIEW_HOME.getUri()) || PUBLIC_URI.getUriMappingsList().stream()
                    .anyMatch(uriMappings -> nowUrI.startsWith(uriMappings.getUri()))) {
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
