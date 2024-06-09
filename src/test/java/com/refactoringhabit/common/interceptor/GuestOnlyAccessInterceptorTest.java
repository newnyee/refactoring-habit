package com.refactoringhabit.common.interceptor;

import static com.refactoringhabit.common.enums.AttributeNames.SESSION_COOKIE_NAME;
import static com.refactoringhabit.common.enums.UrlMappings.VIEW_HOME;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.refactoringhabit.common.interceptor.view.GuestOnlyAccessInterceptor;
import com.refactoringhabit.common.response.Session;
import com.refactoringhabit.common.utils.cookies.CookieUtil;
import com.refactoringhabit.common.utils.interceptor.InterceptorUtils;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

@ExtendWith(MockitoExtension.class)
class GuestOnlyAccessInterceptorTest {

    @Mock
    private CookieUtil cookieUtil;

    @Mock
    private InterceptorUtils interceptorUtils;

    @Mock
    private MockHttpServletRequest request;

    @Mock
    private MockHttpServletResponse response;

    @Mock
    private Object handler;

    @InjectMocks
    private GuestOnlyAccessInterceptor guestOnlyAccessInterceptor;

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

    @DisplayName("GuestOnlyAccessInterceptor 접근 - token(null)")
    @Test
    void testInterceptorAccess_TokenIsNull() throws IOException {
        when(cookieUtil.getValueInCookie(request, SESSION_COOKIE_NAME.getName(), Session.class))
            .thenReturn(null);

        assertTrue(guestOnlyAccessInterceptor.preHandle(request, response, handler));
    }

    @DisplayName("GuestOnlyAccessInterceptor 접근 - token(not null), api 호출")
    @Test
    void testInterceptorAccess_TokenIsNotNullAndCallApi() throws IOException {
        when(cookieUtil.getValueInCookie(request, SESSION_COOKIE_NAME.getName(), Session.class))
            .thenReturn(session);
        when(interceptorUtils.isApiUrl(request)).thenReturn(true);

        assertFalse(guestOnlyAccessInterceptor.preHandle(request, response, handler));

        verify(response).setStatus(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("GuestOnlyAccessInterceptor 접근 - token(not null), view 호출")
    @Test
    void testInterceptorAccess_TokenIsNotNullAndCallView() throws IOException {
        when(cookieUtil.getValueInCookie(request, SESSION_COOKIE_NAME.getName(), Session.class))
            .thenReturn(session);
        when(interceptorUtils.isApiUrl(request)).thenReturn(false);

        assertFalse(guestOnlyAccessInterceptor.preHandle(request, response, handler));

        verify(response).sendRedirect(VIEW_HOME.getUrl());
    }
}
