package com.refactoringhabit.common.interceptor;

import static com.refactoringhabit.common.enums.AttributeNames.MEMBER_ALT_ID;
import static com.refactoringhabit.common.enums.UrlMappings.VIEW_HOST_JOIN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.refactoringhabit.common.interceptor.view.HostAccessInterceptor;
import com.refactoringhabit.common.utils.interceptor.InterceptorUtils;
import com.refactoringhabit.member.domain.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class HostAccessInterceptorTest {

    @Mock
    private InterceptorUtils interceptorUtils;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Object handler;

    @InjectMocks
    private HostAccessInterceptor hostAccessInterceptor;

    @DisplayName("HostAccessInterceptor 접근 - 호스트")
    @Test
    void testInterceptorAccess_Host() throws IOException {
        when(request.getAttribute(MEMBER_ALT_ID.getName())).thenReturn(MEMBER_ALT_ID.getName());
        when(interceptorUtils.isMemberHostById(MEMBER_ALT_ID.getName())).thenReturn(true);

        assertTrue(hostAccessInterceptor.preHandle(request, response, handler));
        verify(interceptorUtils).isApiUrl(request);
    }

    @DisplayName("HostAccessInterceptor 접근 - 회원, api 호출")
    @Test
    void testInterceptorAccess_MemberAndCallApi() throws IOException {
        when(interceptorUtils.isApiUrl(request)).thenReturn(true);
        when(request.getAttribute(MEMBER_ALT_ID.getName())).thenReturn(MEMBER_ALT_ID.getName());
        when(interceptorUtils.isMemberHostById(MEMBER_ALT_ID.getName())).thenReturn(false);

        assertFalse(hostAccessInterceptor.preHandle(request, response, handler));
        verify(response).setStatus(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("HostAccessInterceptor 접근 - 회원, view 호출")
    @Test
    void testInterceptorAccess_MemberAndCallView() throws IOException {
        when(interceptorUtils.isApiUrl(request)).thenReturn(false);
        when(request.getAttribute(MEMBER_ALT_ID.getName())).thenReturn(MEMBER_ALT_ID.getName());
        when(interceptorUtils.isMemberHostById(MEMBER_ALT_ID.getName())).thenReturn(false);
        when(interceptorUtils.redirectToUrl(response, VIEW_HOST_JOIN.getUrl()))
            .thenReturn(false);

        assertFalse(hostAccessInterceptor.preHandle(request, response, handler));
    }

    @DisplayName("HostAccessInterceptor 접근 - 찾을 수 없는 회원, api 호출")
    @Test
    void testInterceptorAccess_MemberNotFoundAndCallApi() throws IOException {
        when(request.getAttribute(MEMBER_ALT_ID.getName())).thenReturn(MEMBER_ALT_ID.getName());
        when(interceptorUtils.isMemberHostById(MEMBER_ALT_ID.getName()))
            .thenThrow(UserNotFoundException.class);
        when(interceptorUtils.isApiUrl(request)).thenReturn(true);

        assertFalse(hostAccessInterceptor.preHandle(request, response, handler));
        verify(response).setStatus(UNAUTHORIZED.value());
    }

    @DisplayName("HostAccessInterceptor 접근 - 찾을 수 없는 회원, view 호출")
    @Test
    void testInterceptorAccess_MemberNotFoundAndCallView() throws IOException {
        when(request.getAttribute(MEMBER_ALT_ID.getName())).thenReturn(MEMBER_ALT_ID.getName());
        when(interceptorUtils.isMemberHostById(MEMBER_ALT_ID.getName()))
            .thenThrow(UserNotFoundException.class);
        when(interceptorUtils.isApiUrl(request)).thenReturn(false);
        when(interceptorUtils.redirectToLogin(request, response)).thenReturn(false);

        assertFalse(hostAccessInterceptor.preHandle(request, response, handler));
    }
}
