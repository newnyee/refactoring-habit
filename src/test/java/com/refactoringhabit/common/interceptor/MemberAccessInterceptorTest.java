package com.refactoringhabit.common.interceptor;

import static com.refactoringhabit.common.enums.AttributeNames.MEMBER_ALT_ID;
import static com.refactoringhabit.common.enums.AttributeNames.SESSION_COOKIE_NAME;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.refactoringhabit.auth.domain.exception.InvalidTokenException;
import com.refactoringhabit.common.interceptor.view.MemberAccessInterceptor;
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

    @Mock
    private Object handler;

    @InjectMocks
    private MemberAccessInterceptor memberAccessInterceptor;

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

    @DisplayName("MemberAccessInterceptor 접근 - token(null), api 호출")
    @Test
    void testInterceptorAccess_TokenIsNullAndCallApi() throws IOException {
        when(cookieUtil.getValueInCookie(request, SESSION_COOKIE_NAME.getName(), Session.class))
            .thenReturn(null);
        when(interceptorUtils.isApiUrl(request)).thenReturn(true);

        assertFalse(memberAccessInterceptor.preHandle(request, response, handler));
        verify(response).setStatus(UNAUTHORIZED.value());
    }

    @DisplayName("MemberAccessInterceptor 접근 - token(null), view 호출")
    @Test
    void testInterceptorAccess_TokenIsNullAndCallView() throws IOException {
        when(cookieUtil.getValueInCookie(request, SESSION_COOKIE_NAME.getName(), Session.class))
            .thenReturn(null);
        when(interceptorUtils.isApiUrl(request)).thenReturn(false);
        when(interceptorUtils.redirectToLogin(request, response)).thenReturn(false);

        assertFalse(memberAccessInterceptor.preHandle(request, response, handler));
    }

    @DisplayName("MemberAccessInterceptor 접근 - token(not null)")
    @Test
    void testInterceptorAccess_TokenIsNotNull() throws IOException {
        when(cookieUtil.getValueInCookie(request, SESSION_COOKIE_NAME.getName(), Session.class))
            .thenReturn(session);
        when(tokenUtil.verifyToken(session.accessToken())).thenReturn(MEMBER_ALT_ID.getName());

        assertTrue(memberAccessInterceptor.preHandle(request, response, handler));
        verify(interceptorUtils).validateUserInDatabase(request, MEMBER_ALT_ID.getName());
    }

    @DisplayName("MemberAccessInterceptor 접근 - token(not null, expired)")
    @Test
    void testInterceptorAccess_ExpiredToken() throws IOException {
        when(cookieUtil.getValueInCookie(request, SESSION_COOKIE_NAME.getName(), Session.class))
            .thenReturn(session);
        when(tokenUtil.verifyToken(session.accessToken())).thenThrow(TokenExpiredException.class);
        when(tokenUtil.getTokenNumber(session.accessToken())).thenReturn(ACCESS_TOKEN_NUMBER);
        when(tokenUtil.getClaimMemberId(ACCESS_TOKEN_NUMBER))
            .thenReturn(MEMBER_ALT_ID.getName());

        assertTrue(memberAccessInterceptor.preHandle(request, response, handler));
        verify(interceptorUtils).handleExpiredToken(request, response, MEMBER_ALT_ID.getName());
    }

    @DisplayName("MemberAccessInterceptor 접근 - token(not null, invalid), api 호출")
    @Test
    void testInterceptorAccess_InvalidTokenAndCallApi() throws IOException {
        when(cookieUtil.getValueInCookie(request, SESSION_COOKIE_NAME.getName(), Session.class))
            .thenReturn(session);
        when(tokenUtil.verifyToken(session.accessToken())).thenThrow(InvalidTokenException.class);
        when(interceptorUtils.isApiUrl(request)).thenReturn(true);

        assertFalse(memberAccessInterceptor.preHandle(request, response, handler));
        verify(response).setStatus(UNAUTHORIZED.value());
    }

    @DisplayName("MemberAccessInterceptor 접근 - token(not null, invalid), view 호출")
    @Test
    void testInterceptorAccess_InvalidTokenAndCallView() throws IOException {
        when(cookieUtil.getValueInCookie(request, SESSION_COOKIE_NAME.getName(), Session.class))
            .thenReturn(session);
        when(tokenUtil.verifyToken(session.accessToken())).thenThrow(InvalidTokenException.class);
        when(interceptorUtils.isApiUrl(request)).thenReturn(false);
        when(interceptorUtils.redirectToLogin(request, response)).thenReturn(false);

        assertFalse(memberAccessInterceptor.preHandle(request, response, handler));
    }
}
