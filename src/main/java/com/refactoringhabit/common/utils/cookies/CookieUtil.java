package com.refactoringhabit.common.utils.cookies;

import static com.refactoringhabit.common.utils.cookies.CookieAttributes.COOKIE_PATH;
import static com.refactoringhabit.common.utils.cookies.CookieAttributes.REFRESH_TOKEN_COOKIE_NAME;
import static com.refactoringhabit.common.utils.cookies.CookieAttributes.SESSION_EXPIRE_TIME_ZERO;
import static com.refactoringhabit.common.utils.cookies.CookieAttributes.SET_COOKIE;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    @Value("${token.expire-time.refresh}")
    private long expireSecRefreshToken;

    @Value("${token.expire-time.access}")
    private long expireMsAccessToken;

    public void createTokenCookie(HttpServletResponse response, String cookieName, String token) {

        long expireTime
            = cookieName.equals(REFRESH_TOKEN_COOKIE_NAME)
            ? expireSecRefreshToken
            : expireMsAccessToken / 1000;

        response.addHeader(SET_COOKIE,
            ResponseCookie.from(cookieName, token)
                .path(COOKIE_PATH)
                .sameSite("None")
                .httpOnly(true)
                .secure(true)
                .maxAge(expireTime)
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

    public void deleteRefreshTokenCookie(HttpServletResponse response, String cookieName) {
        response.addHeader(SET_COOKIE,
            ResponseCookie.from(cookieName, "")
                .path(COOKIE_PATH)
                .sameSite("None")
                .httpOnly(true)
                .secure(true)
                .maxAge(SESSION_EXPIRE_TIME_ZERO)
                .build()
                .toString());
    }
}
