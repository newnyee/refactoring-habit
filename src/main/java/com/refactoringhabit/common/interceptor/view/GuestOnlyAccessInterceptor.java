package com.refactoringhabit.common.interceptor.view;

import static com.refactoringhabit.common.enums.UrlMappings.VIEW_HOME;
import static com.refactoringhabit.common.utils.cookies.CookieAttributes.ACCESS_TOKEN_COOKIE_NAME;
import static org.springframework.http.HttpStatus.FORBIDDEN;

import com.refactoringhabit.common.utils.cookies.CookieUtil;
import com.refactoringhabit.common.utils.interceptor.InterceptorUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class GuestOnlyAccessInterceptor implements HandlerInterceptor {

    private final CookieUtil cookieUtil;
    private final InterceptorUtils interceptorUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws IOException {

        String getAccessToken = cookieUtil.getTokenInCookie(request, ACCESS_TOKEN_COOKIE_NAME);
        if (getAccessToken == null) {
            return true;
        }

        if (interceptorUtils.isApiUrl(request)) { // api 호출
            response.setStatus(FORBIDDEN.value());
        } else { // view 호출
            response.sendRedirect(VIEW_HOME.getUrl());
        }
        return false;
    }
}
