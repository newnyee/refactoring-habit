package com.refactoringhabit.common.interceptor.view;

import static com.refactoringhabit.common.enums.AttributeNames.MEMBER_ALT_ID;
import static com.refactoringhabit.common.enums.UriMappings.VIEW_HOST_JOIN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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

@ExtendWith(MockitoExtension.class)
class ViewAuthZInterceptorTest {

    @Mock
    private InterceptorUtils interceptorUtils;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Object handler;

    @InjectMocks
    private ViewAuthZInterceptor viewAuthZInterceptor;

    @DisplayName("ViewAuthZInterceptor 접근 - 호스트")
    @Test
    void testInterceptorAccess_Host() throws IOException {
        when(request.getAttribute(MEMBER_ALT_ID.getName())).thenReturn(MEMBER_ALT_ID.getName());
        when(interceptorUtils.isMemberHostById(MEMBER_ALT_ID.getName())).thenReturn(true);

        assertTrue(viewAuthZInterceptor.preHandle(request, response, handler));
    }

    @DisplayName("ViewAuthZInterceptor 접근 - 회원")
    @Test
    void testInterceptorAccess_MemberAndCallView() throws IOException {
        when(request.getAttribute(MEMBER_ALT_ID.getName())).thenReturn(MEMBER_ALT_ID.getName());
        when(interceptorUtils.isMemberHostById(MEMBER_ALT_ID.getName())).thenReturn(false);
        when(interceptorUtils.redirectToUrl(response, VIEW_HOST_JOIN.getUri()))
            .thenReturn(false);

        assertFalse(viewAuthZInterceptor.preHandle(request, response, handler));
    }

    @DisplayName("ViewAuthZInterceptor 접근 - 찾을 수 없는 회원")
    @Test
    void testInterceptorAccess_MemberNotFoundAndCallView() throws IOException {
        when(request.getAttribute(MEMBER_ALT_ID.getName())).thenReturn(MEMBER_ALT_ID.getName());
        when(interceptorUtils.isMemberHostById(MEMBER_ALT_ID.getName()))
            .thenThrow(UserNotFoundException.class);
        when(interceptorUtils.redirectToLogin(request, response)).thenReturn(false);

        assertFalse(viewAuthZInterceptor.preHandle(request, response, handler));
    }
}
