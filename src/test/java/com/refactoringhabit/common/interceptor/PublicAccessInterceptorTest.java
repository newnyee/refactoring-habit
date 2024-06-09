package com.refactoringhabit.common.interceptor;

import static com.refactoringhabit.common.enums.AttributeNames.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.refactoringhabit.common.interceptor.view.PublicAccessInterceptor;
import com.refactoringhabit.common.response.Session;
import com.refactoringhabit.common.utils.TokenUtil;
import com.refactoringhabit.common.utils.cookies.CookieUtil;
import com.refactoringhabit.common.utils.interceptor.InterceptorUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
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

    @Mock
    private Object handler;

    @InjectMocks
    private PublicAccessInterceptor publicAccessInterceptor;

    private Session session;

    private static final String ACCESS_TOKEN = "accessToken";
    private static final String REFRESH_TOKEN = "refreshToken";
    private static final String ACCESS_TOKEN_NUMBER = "accessTokenNumber";

    @BeforeEach
    void setUp() {
        session = Session.builder()
            .accessToken(ACCESS_TOKEN)
            .refreshToken(REFRESH_TOKEN)
            .build();
    }

   @DisplayName("PublicAccessInterceptor 접근 - token(null)")
    @Test
    void testInterceptorAccess_TokenIsNull() throws JsonProcessingException {
        when(cookieUtil.getValueInCookie(request, SESSION_COOKIE_NAME.getName(), Session.class))
            .thenReturn(null);

        assertTrue(publicAccessInterceptor.preHandle(request, response, handler));
    }

    @DisplayName("PublicAccessInterceptor 접근 - token(not null)")
    @Test
    void testInterceptorAccess_TokenIsNotNull() throws JsonProcessingException {
        when(cookieUtil.getValueInCookie(request, SESSION_COOKIE_NAME.getName(), Session.class))
            .thenReturn(session);
        when(tokenUtil.verifyToken(session.accessToken())).thenReturn(MEMBER_ALT_ID.getName());

        assertTrue(publicAccessInterceptor.preHandle(request, response, handler));
        verify(interceptorUtils).validateUserInDatabase(request, MEMBER_ALT_ID.getName());
    }

    @DisplayName("PublicAccessInterceptor 접근 - token(not null, expired)")
    @Test
    void testInterceptorAccess_ExpiredToken() throws JsonProcessingException {
        when(cookieUtil.getValueInCookie(request, SESSION_COOKIE_NAME.getName(), Session.class))
            .thenReturn(session);
        when(tokenUtil.verifyToken(session.accessToken())).thenThrow(TokenExpiredException.class);
        when(tokenUtil.getTokenNumber(ACCESS_TOKEN)).thenReturn(ACCESS_TOKEN_NUMBER);
        when(tokenUtil.getClaimMemberId(ACCESS_TOKEN_NUMBER))
            .thenReturn(MEMBER_ALT_ID.getName());

        assertTrue(publicAccessInterceptor.preHandle(request, response, handler));
        verify(interceptorUtils).handleExpiredToken(request, response, MEMBER_ALT_ID.getName());
    }

    @DisplayName("PublicAccessInterceptor 접근 - token(not null, invalid)")
    @Test
    void testInterceptorAccess_InvalidToken() throws JsonProcessingException {
        when(cookieUtil.getValueInCookie(request, SESSION_COOKIE_NAME.getName(), Session.class))
            .thenReturn(session);
        when(tokenUtil.verifyToken(session.accessToken())).thenThrow(RuntimeException.class);

        assertTrue(publicAccessInterceptor.preHandle(request, response, handler));
    }
}
