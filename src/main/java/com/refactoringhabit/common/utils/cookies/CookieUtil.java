package com.refactoringhabit.common.utils.cookies;

import static com.refactoringhabit.common.utils.cookies.CookieAttributes.COOKIE_PATH;
import static com.refactoringhabit.common.utils.cookies.CookieAttributes.REFRESH_TOKEN_COOKIE_NAME;
import static com.refactoringhabit.common.utils.cookies.CookieAttributes.SESSION_EXPIRE_TIME_MAX;
import static com.refactoringhabit.common.utils.cookies.CookieAttributes.SESSION_EXPIRE_TIME_ZERO;
import static com.refactoringhabit.common.utils.cookies.CookieAttributes.SET_COOKIE;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    public void createRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        response.addHeader(SET_COOKIE,
            ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, refreshToken)
                .path(COOKIE_PATH)
                .sameSite("None")
                .httpOnly(true)
                .secure(true)
                .maxAge(SESSION_EXPIRE_TIME_MAX)
                .build()
                .toString());
    }

    public String getRefreshTokenInCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(REFRESH_TOKEN_COOKIE_NAME)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public void deleteRefreshTokenCookie(HttpServletResponse response) {
        response.addHeader(SET_COOKIE,
            ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, "")
                .path(COOKIE_PATH)
                .sameSite("None")
                .httpOnly(true)
                .secure(true)
                .maxAge(SESSION_EXPIRE_TIME_ZERO)
                .build()
                .toString());
    }
}
