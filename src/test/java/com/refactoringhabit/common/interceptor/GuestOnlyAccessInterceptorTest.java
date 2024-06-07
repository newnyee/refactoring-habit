package com.refactoringhabit.common.interceptor;

import static com.refactoringhabit.common.enums.UrlMappings.VIEW_HOME;
import static com.refactoringhabit.common.utils.cookies.CookieAttributes.ACCESS_TOKEN_COOKIE_NAME;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.refactoringhabit.common.interceptor.view.GuestOnlyAccessInterceptor;
import com.refactoringhabit.common.utils.cookies.CookieUtil;
import com.refactoringhabit.common.utils.interceptor.InterceptorUtils;
import java.io.IOException;
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

    @InjectMocks
    private GuestOnlyAccessInterceptor guestOnlyAccessInterceptor;

    private final Object handler = new Object();
    private static final String ACCESS_TOKEN = "accessToken";

    @DisplayName("GuestOnlyAccessInterceptor 접근 - token(null)")
    @Test
    void testInterceptorAccess_TokenIsNull() throws IOException {
        when(cookieUtil.getTokenInCookie(request, ACCESS_TOKEN_COOKIE_NAME))
            .thenReturn(null);

        assertTrue(guestOnlyAccessInterceptor.preHandle(request, response, handler));
    }

    @DisplayName("GuestOnlyAccessInterceptor 접근 - token(not null), api 호출")
    @Test
    void testInterceptorAccess_TokenIsNotNullAndCallApi() throws IOException {
        when(cookieUtil.getTokenInCookie(request, ACCESS_TOKEN_COOKIE_NAME))
            .thenReturn(ACCESS_TOKEN);
        when(interceptorUtils.isApiUrl(request)).thenReturn(true);

        assertFalse(guestOnlyAccessInterceptor.preHandle(request, response, handler));

        verify(response).setStatus(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("GuestOnlyAccessInterceptor 접근 - token(not null), view 호출")
    @Test
    void testInterceptorAccess_TokenIsNotNullAndCallView() throws IOException {
        when(cookieUtil.getTokenInCookie(request, ACCESS_TOKEN_COOKIE_NAME))
            .thenReturn(ACCESS_TOKEN);
        when(interceptorUtils.isApiUrl(request)).thenReturn(false);

        assertFalse(guestOnlyAccessInterceptor.preHandle(request, response, handler));

        verify(response).sendRedirect(VIEW_HOME.getUrl());
    }
}
