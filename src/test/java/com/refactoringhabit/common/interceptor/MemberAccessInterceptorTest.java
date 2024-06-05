package com.refactoringhabit.common.interceptor;

import static com.refactoringhabit.common.enums.AttributeNames.ALT_ID;
import static com.refactoringhabit.common.utils.cookies.CookieAttributes.ACCESS_TOKEN_COOKIE_NAME;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.refactoringhabit.auth.domain.exception.InvalidTokenException;
import com.refactoringhabit.auth.domain.exception.NullTokenException;
import com.refactoringhabit.common.utils.TokenUtil;
import com.refactoringhabit.common.utils.cookies.CookieUtil;
import com.refactoringhabit.common.utils.interceptor.InterceptorUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberAccessInterceptorTest {

    @Mock
    private CookieUtil cookieUtil;

    @Mock
    private TokenUtil tokenUtil;

    @Mock
    private InterceptorUtils interceptorUtils;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private MemberAccessInterceptor memberAccessInterceptor;

    private final Object handler = new Object();
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String ANY_STRING = "anyString";
    private static final String ACCESS_TOKEN_NUMBER = "accessTokenNumber";

    @DisplayName("MemberAccessInterceptor 접근 - token(null), api 호출")
    @Test
    void testInterceptorAccess_TokenIsNullAndCallApi() throws IOException {
        when(cookieUtil.getTokenInCookie(request, ACCESS_TOKEN_COOKIE_NAME))
            .thenReturn(null);
        when(tokenUtil.verifyToken(null)).thenThrow(NullTokenException.class);
        when(interceptorUtils.isApiUrl(request)).thenReturn(true);

        assertFalse(memberAccessInterceptor.preHandle(request, response, handler));
        verify(response).setStatus(UNAUTHORIZED.value());
    }

    @DisplayName("MemberAccessInterceptor 접근 - token(null), view 호출")
    @Test
    void testInterceptorAccess_TokenIsNullAndCallView() throws IOException {
        when(cookieUtil.getTokenInCookie(request, ACCESS_TOKEN_COOKIE_NAME))
            .thenReturn(null);
        when(tokenUtil.verifyToken(null)).thenThrow(NullTokenException.class);
        when(interceptorUtils.isApiUrl(request)).thenReturn(false);
        when(interceptorUtils.redirectToLogin(request, response)).thenReturn(false);

        assertFalse(memberAccessInterceptor.preHandle(request, response, handler));
    }

    @DisplayName("MemberAccessInterceptor 접근 - token(not null)")
    @Test
    void testInterceptorAccess_TokenIsNotNull() throws IOException {
        when(cookieUtil.getTokenInCookie(request, ACCESS_TOKEN_COOKIE_NAME))
            .thenReturn(ACCESS_TOKEN);
        when(tokenUtil.verifyToken(ACCESS_TOKEN)).thenReturn(ALT_ID.getName());

        assertTrue(memberAccessInterceptor.preHandle(request, response, handler));
        verify(interceptorUtils).validateUserInDatabase(request, ALT_ID.getName());
    }

    @DisplayName("MemberAccessInterceptor 접근 - token(not null, expired)")
    @Test
    void testInterceptorAccess_ExpiredToken() throws IOException {
        when(cookieUtil.getTokenInCookie(request, ACCESS_TOKEN_COOKIE_NAME))
            .thenReturn(ACCESS_TOKEN);
        when(tokenUtil.verifyToken(ACCESS_TOKEN)).thenThrow(TokenExpiredException.class);
        when(tokenUtil.getTokenNumber(ACCESS_TOKEN)).thenReturn(ACCESS_TOKEN_NUMBER);
        when(tokenUtil.getClaimMemberId(ACCESS_TOKEN_NUMBER)).thenReturn(ALT_ID.getName());

        assertTrue(memberAccessInterceptor.preHandle(request, response, handler));
        verify(interceptorUtils).handleExpiredToken(request, response, ALT_ID.getName());
    }

    @DisplayName("MemberAccessInterceptor 접근 - token(not null, invalid), api 호출")
    @Test
    void testInterceptorAccess_InvalidTokenAndCallApi() throws IOException {
        when(cookieUtil.getTokenInCookie(request, ACCESS_TOKEN_COOKIE_NAME))
            .thenReturn(ACCESS_TOKEN);
        when(tokenUtil.verifyToken(ACCESS_TOKEN)).thenThrow(InvalidTokenException.class);
        when(interceptorUtils.isApiUrl(request)).thenReturn(true);

        assertFalse(memberAccessInterceptor.preHandle(request, response, handler));
        verify(response).setStatus(UNAUTHORIZED.value());
    }

    @DisplayName("MemberAccessInterceptor 접근 - token(not null, invalid), view 호출")
    @Test
    void testInterceptorAccess_InvalidTokenAndCallView() throws IOException {
        when(cookieUtil.getTokenInCookie(request, ACCESS_TOKEN_COOKIE_NAME))
            .thenReturn(ACCESS_TOKEN);
        when(tokenUtil.verifyToken(ACCESS_TOKEN)).thenThrow(InvalidTokenException.class);
        when(interceptorUtils.isApiUrl(request)).thenReturn(false);
        when(interceptorUtils.redirectToLogin(request, response)).thenReturn(false);

        assertFalse(memberAccessInterceptor.preHandle(request, response, handler));
    }
}
