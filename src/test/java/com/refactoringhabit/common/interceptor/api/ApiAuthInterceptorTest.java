package com.refactoringhabit.common.interceptor.api;

import static com.refactoringhabit.common.enums.AttributeNames.MEMBER_ALT_ID;
import static com.refactoringhabit.common.enums.AttributeNames.SESSION_COOKIE_NAME;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.refactoringhabit.auth.domain.exception.InvalidTokenException;
import com.refactoringhabit.common.enums.AttributeNames;
import com.refactoringhabit.common.response.Session;
import com.refactoringhabit.common.utils.TokenUtil;
import com.refactoringhabit.common.utils.cookies.CookieUtil;
import com.refactoringhabit.common.utils.interceptor.InterceptorUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;

@ExtendWith(MockitoExtension.class)
class ApiAuthInterceptorTest {

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

    @Mock
    private HandlerMethod handler;

    @InjectMocks
    private ApiAuthInterceptor apiAuthInterceptor;

    private Session session;
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String REFRESH_TOKEN = "refreshToken";

    @BeforeEach
    void setUp() {
        session = Session.builder()
            .accessToken(ACCESS_TOKEN)
            .refreshToken(REFRESH_TOKEN)
            .build();
    }

    @DisplayName("ApiAuthInterceptor 접근 - token(null)")
    @Test
    void testInterceptorAccess_NullToken() throws IOException {
        when(cookieUtil.getValueInCookie(request, SESSION_COOKIE_NAME.getName(), Session.class))
            .thenReturn(null);

        assertFalse(apiAuthInterceptor.preHandle(request, response, handler));
        verify(response).setStatus(UNAUTHORIZED.value());
    }

    @DisplayName("ApiAuthInterceptor 접근 - token(not null)")
    @Test
    void testInterceptorAccess_NotNullToken() throws IOException {
        when(cookieUtil.getValueInCookie(request, SESSION_COOKIE_NAME.getName(), Session.class))
            .thenReturn(session);
        when(tokenUtil.verifyToken(session.accessToken())).thenReturn(MEMBER_ALT_ID.getName());

        assertTrue(apiAuthInterceptor.preHandle(request, response, handler));
        verify(interceptorUtils).validateUserInDatabase(request, MEMBER_ALT_ID.getName());
    }

    @DisplayName("ApiAuthInterceptor 접근 - token(expired)")
    @Test
    void testInterceptorAccess_ExpiredToken() throws IOException {
        when(cookieUtil.getValueInCookie(request, SESSION_COOKIE_NAME.getName(), Session.class))
            .thenReturn(session);
        when(tokenUtil.verifyToken(session.accessToken())).thenThrow(TokenExpiredException.class);
        when(tokenUtil.getClaimMemberId(session.accessToken()))
            .thenReturn(MEMBER_ALT_ID.getName());

        assertTrue(apiAuthInterceptor.preHandle(request, response, handler));
        verify(interceptorUtils).handleExpiredToken(request, response, MEMBER_ALT_ID.getName());
    }

    @DisplayName("ApiAuthInterceptor 접근 - token(invalid)")
    @Test
    void testInterceptorAccess_InvalidToken() throws IOException {
        when(cookieUtil.getValueInCookie(request, SESSION_COOKIE_NAME.getName(), Session.class))
            .thenReturn(session);
        when(tokenUtil.verifyToken(session.accessToken())).thenThrow(InvalidTokenException.class);

        assertFalse(apiAuthInterceptor.preHandle(request, response, handler));
        verify(response).setStatus(UNAUTHORIZED.value());
    }
}
