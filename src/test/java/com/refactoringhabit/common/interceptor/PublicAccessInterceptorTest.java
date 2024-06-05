package com.refactoringhabit.common.interceptor;

import static com.refactoringhabit.common.enums.AttributeNames.*;
import static com.refactoringhabit.common.utils.cookies.CookieAttributes.ACCESS_TOKEN_COOKIE_NAME;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.refactoringhabit.auth.domain.exception.NullTokenException;
import com.refactoringhabit.common.utils.TokenUtil;
import com.refactoringhabit.common.utils.cookies.CookieUtil;
import com.refactoringhabit.common.utils.interceptor.InterceptorUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PublicAccessInterceptorTest {

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
    private PublicAccessInterceptor publicAccessInterceptor;

    private final Object handler = new Object();
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String ACCESS_TOKEN_NUMBER = "accessTokenNumber";

    @DisplayName("PublicAccessInterceptor 접근 - token(null)")
    @Test
    void testInterceptorAccess_TokenIsNull() {
        when(cookieUtil.getTokenInCookie(request, ACCESS_TOKEN_COOKIE_NAME))
            .thenReturn(null);
        when(tokenUtil.verifyToken(null)).thenThrow(NullTokenException.class);

        assertTrue(publicAccessInterceptor.preHandle(request, response, handler));
    }

    @DisplayName("PublicAccessInterceptor 접근 - token(not null)")
    @Test
    void testInterceptorAccess_TokenIsNotNull() {
        when(cookieUtil.getTokenInCookie(request, ACCESS_TOKEN_COOKIE_NAME))
            .thenReturn(ACCESS_TOKEN);
        when(tokenUtil.verifyToken(ACCESS_TOKEN)).thenReturn(ALT_ID.getName());

        assertTrue(publicAccessInterceptor.preHandle(request, response, handler));
        verify(interceptorUtils).validateUserInDatabase(request, ALT_ID.getName());
    }

    @DisplayName("PublicAccessInterceptor 접근 - token(not null, expired)")
    @Test
    void testInterceptorAccess_ExpiredToken() {
        when(cookieUtil.getTokenInCookie(request, ACCESS_TOKEN_COOKIE_NAME))
            .thenReturn(ACCESS_TOKEN);
        when(tokenUtil.verifyToken(ACCESS_TOKEN)).thenThrow(TokenExpiredException.class);
        when(tokenUtil.getTokenNumber(ACCESS_TOKEN)).thenReturn(ACCESS_TOKEN_NUMBER);
        when(tokenUtil.getClaimMemberId(ACCESS_TOKEN_NUMBER)).thenReturn(ALT_ID.getName());

        assertTrue(publicAccessInterceptor.preHandle(request, response, handler));
        verify(interceptorUtils).handleExpiredToken(request, response, ALT_ID.getName());
    }

    @DisplayName("PublicAccessInterceptor 접근 - token(not null, invalid)")
    @Test
    void testInterceptorAccess_InvalidToken() {
        when(cookieUtil.getTokenInCookie(request, ACCESS_TOKEN_COOKIE_NAME))
            .thenReturn(ACCESS_TOKEN);
        when(tokenUtil.verifyToken(ACCESS_TOKEN)).thenThrow(RuntimeException.class);

        assertTrue(publicAccessInterceptor.preHandle(request, response, handler));
    }
}
