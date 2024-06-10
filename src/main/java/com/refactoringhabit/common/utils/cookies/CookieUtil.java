package com.refactoringhabit.common.utils.cookies;

import static com.refactoringhabit.common.enums.AttributeNames.SESSION_COOKIE_NAME;
import static com.refactoringhabit.common.utils.cookies.CookieAttributes.COOKIE_PATH;
import static com.refactoringhabit.common.utils.cookies.CookieAttributes.SESSION_EXPIRE_TIME_ZERO;
import static com.refactoringhabit.common.utils.cookies.CookieAttributes.SET_COOKIE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CookieUtil {

    @Value("${token.expire-time.refresh}")
    private long expireSecRefreshToken;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public void createSessionCookie(HttpServletResponse response, Object value)
        throws JsonProcessingException {

        log.debug("serialization To Json = {}", serializeToJson(value));

        response.addHeader(SET_COOKIE,
            ResponseCookie.from(SESSION_COOKIE_NAME.getName(), serializeToJson(value))
                .path(COOKIE_PATH)
                .sameSite("None")
                .httpOnly(true)
                .secure(true)
                .maxAge(expireSecRefreshToken)
                .build()
                .toString());
    }

    public <T> T getValueInCookie(HttpServletRequest request, String cookieName,
        Class<T> valueType) throws JsonProcessingException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return deserializeFromJson(cookie.getValue(), valueType);
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

    public static String serializeToJson(Object value) throws JsonProcessingException {
        return Base64.getEncoder().encodeToString(
            objectMapper.writeValueAsString(value).getBytes());
    }

    public static <T> T deserializeFromJson(String json, Class<T> valueType)
        throws JsonProcessingException {
        return objectMapper.readValue(new String(Base64.getDecoder().decode(json)), valueType);
    }
}
