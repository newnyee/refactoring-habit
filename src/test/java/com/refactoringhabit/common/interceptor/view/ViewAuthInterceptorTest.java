package com.refactoringhabit.common.interceptor.view;

import static com.refactoringhabit.common.enums.AttributeNames.MEMBER_ALT_ID;
import static com.refactoringhabit.common.enums.AttributeNames.SESSION_COOKIE_NAME;
import static com.refactoringhabit.common.enums.UriMappings.VIEW_HOME;
import static com.refactoringhabit.common.enums.UriMappings.VIEW_HOST_JOIN;
import static com.refactoringhabit.common.enums.UriMappings.VIEW_JOIN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.refactoringhabit.auth.domain.exception.InvalidTokenException;
import com.refactoringhabit.common.response.Session;
import com.refactoringhabit.common.utils.TokenUtil;
import com.refactoringhabit.common.utils.cookies.CookieUtil;
import com.refactoringhabit.common.utils.interceptor.InterceptorUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.method.HandlerMethod;

@ExtendWith(MockitoExtension.class)
class ViewAuthInterceptorTest {

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
    private ViewAuthInterceptor viewAuthInterceptor;

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

    @DisplayName("ViewAuthInterceptor 접근 - token(null), uri(null session only)")
    @Test
    void testInterceptorAccess_NullTokenAndNullSessionOnlyUri() throws IOException {
        when(cookieUtil.getValueInCookie(request, SESSION_COOKIE_NAME.getName(), Session.class))
            .thenReturn(null);
        when(request.getRequestURI()).thenReturn(VIEW_JOIN.getUri());

        assertTrue(viewAuthInterceptor.preHandle(request, response, handler));
    }

    @DisplayName("ViewAuthInterceptor 접근 - token(not null), uri(null session only)")
    @Test
    void testInterceptorAccess_NotNullTokenAndNullSessionOnlyUri() throws IOException {
        when(cookieUtil.getValueInCookie(request, SESSION_COOKIE_NAME.getName(), Session.class))
            .thenReturn(session);
        when(request.getRequestURI()).thenReturn(VIEW_JOIN.getUri());

        assertFalse(viewAuthInterceptor.preHandle(request, response, handler));
        verify(response).sendRedirect(VIEW_HOME.getUri());
    }

    @DisplayName("ViewAuthInterceptor 접근 - token(null), uri(public)")
    @Test
    void testInterceptorAccess_NullTokenAndPublicUri() throws IOException {
        when(cookieUtil.getValueInCookie(request, SESSION_COOKIE_NAME.getName(), Session.class))
            .thenReturn(null);
        when(request.getRequestURI()).thenReturn(VIEW_HOME.getUri());

        assertTrue(viewAuthInterceptor.preHandle(request, response, handler));
    }

    @DisplayName("ViewAuthInterceptor 접근 - token(not null), uri(public)")
    @Test
    void testInterceptorAccess_NotNullTokenAndPublicUri() throws IOException {
        when(cookieUtil.getValueInCookie(request, SESSION_COOKIE_NAME.getName(), Session.class))
            .thenReturn(session);
        when(request.getRequestURI()).thenReturn(VIEW_HOME.getUri());
        when(tokenUtil.verifyToken(session.accessToken())).thenReturn(MEMBER_ALT_ID.getName());

        assertTrue(viewAuthInterceptor.preHandle(request, response, handler));
        verify(interceptorUtils).validateUserInDatabase(request, MEMBER_ALT_ID.getName());
    }

    @DisplayName("ViewAuthInterceptor 접근 - token(expired), uri(public)")
    @Test
    void testInterceptorAccess_ExpiredTokenAndPublicUri() throws IOException {
        when(cookieUtil.getValueInCookie(request, SESSION_COOKIE_NAME.getName(), Session.class))
            .thenReturn(session);
        when(request.getRequestURI()).thenReturn(VIEW_HOME.getUri());
        when(tokenUtil.verifyToken(session.accessToken())).thenThrow(TokenExpiredException.class);
        when(tokenUtil.getClaimMemberId(session.accessToken()))
            .thenReturn(MEMBER_ALT_ID.getName());

        assertTrue(viewAuthInterceptor.preHandle(request, response, handler));
        verify(interceptorUtils).handleExpiredToken(request, response, MEMBER_ALT_ID.getName());
    }

    @DisplayName("ViewAuthInterceptor 접근 - token(invalid), uri(public)")
    @Test
    void testInterceptorAccess_InvalidTokenAndPublicUri() throws IOException {
        when(cookieUtil.getValueInCookie(request, SESSION_COOKIE_NAME.getName(), Session.class))
            .thenReturn(session);
        when(request.getRequestURI()).thenReturn(VIEW_HOME.getUri());
        when(tokenUtil.verifyToken(session.accessToken())).thenThrow(InvalidTokenException.class);

        assertTrue(viewAuthInterceptor.preHandle(request, response, handler));
    }

    @DisplayName("ViewAuthInterceptor 접근 - token(null), uri(required authentication)")
    @Test
    void testInterceptorAccess_NullTokenAndRequiredAuthenticationUri() throws IOException {
        when(cookieUtil.getValueInCookie(request, SESSION_COOKIE_NAME.getName(), Session.class))
            .thenReturn(null);
        when(request.getRequestURI()).thenReturn(VIEW_HOST_JOIN.getUri());
        when(interceptorUtils.redirectToLogin(request, response)).thenReturn(false);

        assertFalse(viewAuthInterceptor.preHandle(request, response, handler));
    }

    @DisplayName("ViewAuthInterceptor 접근 - token(not null), uri(required authentication)")
    @Test
    void testInterceptorAccess_NotNullTokenAndRequiredAuthenticationUri() throws IOException {
        when(cookieUtil.getValueInCookie(request, SESSION_COOKIE_NAME.getName(), Session.class))
            .thenReturn(session);
        when(request.getRequestURI()).thenReturn(VIEW_HOST_JOIN.getUri());
        when(tokenUtil.verifyToken(session.accessToken())).thenReturn(MEMBER_ALT_ID.getName());

        assertTrue(viewAuthInterceptor.preHandle(request, response, handler));
        verify(interceptorUtils).validateUserInDatabase(request, MEMBER_ALT_ID.getName());
    }

    @DisplayName("ViewAuthInterceptor 접근 - token(expired), uri(required authentication)")
    @Test
    void testInterceptorAccess_ExpiredTokenAndRequiredAuthenticationUri() throws IOException {
        when(cookieUtil.getValueInCookie(request, SESSION_COOKIE_NAME.getName(), Session.class))
            .thenReturn(session);
        when(request.getRequestURI()).thenReturn(VIEW_HOST_JOIN.getUri());
        when(tokenUtil.verifyToken(session.accessToken())).thenThrow(TokenExpiredException.class);
        when(tokenUtil.getClaimMemberId(session.accessToken()))
            .thenReturn(MEMBER_ALT_ID.getName());

        assertTrue(viewAuthInterceptor.preHandle(request, response, handler));
        verify(interceptorUtils).handleExpiredToken(request, response, MEMBER_ALT_ID.getName());
    }

    @DisplayName("ViewAuthInterceptor 접근 - token(invalid), uri(required authentication)")
    @Test
    void testInterceptorAccess_InvalidTokenAndRequiredAuthenticationUri() throws IOException {
        when(cookieUtil.getValueInCookie(request, SESSION_COOKIE_NAME.getName(), Session.class))
            .thenReturn(session);
        when(request.getRequestURI()).thenReturn(VIEW_HOST_JOIN.getUri());
        when(tokenUtil.verifyToken(session.accessToken())).thenThrow(InvalidTokenException.class);
        when(interceptorUtils.redirectToLogin(request, response)).thenReturn(false);

        assertFalse(viewAuthInterceptor.preHandle(request, response, handler));
    }
}
