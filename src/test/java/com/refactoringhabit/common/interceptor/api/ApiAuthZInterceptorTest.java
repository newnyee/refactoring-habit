package com.refactoringhabit.common.interceptor.api;

import static com.refactoringhabit.common.enums.AttributeNames.MEMBER_ALT_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.refactoringhabit.common.utils.interceptor.InterceptorUtils;
import com.refactoringhabit.member.domain.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ApiAuthZInterceptorTest {

    @Mock
    private InterceptorUtils interceptorUtils;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Object handler;

    @InjectMocks
    private ApiAuthZInterceptor apiAuthZInterceptor;

    @DisplayName("ApiAuthZInterceptor 접근 - 호스트")
    @Test
    void testInterceptorAccess_Host() {
        when(request.getAttribute(MEMBER_ALT_ID.getName())).thenReturn(MEMBER_ALT_ID.getName());
        when(interceptorUtils.isMemberHostById(MEMBER_ALT_ID.getName())).thenReturn(true);

        assertTrue(apiAuthZInterceptor.preHandle(request, response, handler));
    }

    @DisplayName("ApiAuthZInterceptor 접근 - 회원")
    @Test
    void testInterceptorAccess_MemberAndCallView() {
        when(request.getAttribute(MEMBER_ALT_ID.getName())).thenReturn(MEMBER_ALT_ID.getName());
        when(interceptorUtils.isMemberHostById(MEMBER_ALT_ID.getName())).thenReturn(false);

        assertFalse(apiAuthZInterceptor.preHandle(request, response, handler));
        verify(response).setStatus(FORBIDDEN.value());
    }

    @DisplayName("ApiAuthZInterceptor 접근 - 찾을 수 없는 회원")
    @Test
    void testInterceptorAccess_MemberNotFoundAndCallView() {
        when(request.getAttribute(MEMBER_ALT_ID.getName())).thenReturn(MEMBER_ALT_ID.getName());
        when(interceptorUtils.isMemberHostById(MEMBER_ALT_ID.getName()))
            .thenThrow(UserNotFoundException.class);

        assertFalse(apiAuthZInterceptor.preHandle(request, response, handler));
        verify(response).setStatus(UNAUTHORIZED.value());
    }
}
