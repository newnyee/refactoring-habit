package com.refactoringhabit.common.interceptor.view;

import static com.refactoringhabit.common.enums.AttributeNames.MEMBER_ALT_ID;
import static com.refactoringhabit.common.enums.UriMappings.VIEW_HOST_HOME;
import static com.refactoringhabit.common.enums.UriMappings.VIEW_HOST_JOIN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.ModelAndView;

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
        when(request.getRequestURI()).thenReturn(VIEW_HOST_HOME.getUri());

        assertTrue(viewAuthZInterceptor.preHandle(request, response, handler));
    }

    @DisplayName("ViewAuthZInterceptor 접근 - 회원")
    @Test
    void testInterceptorAccess_Member() throws IOException {
        when(request.getAttribute(MEMBER_ALT_ID.getName())).thenReturn(MEMBER_ALT_ID.getName());
        when(interceptorUtils.isMemberHostById(MEMBER_ALT_ID.getName())).thenReturn(false);
        when(request.getRequestURI()).thenReturn(VIEW_HOST_HOME.getUri());
        when(interceptorUtils.redirectToUrl(response, VIEW_HOST_JOIN.getUri()))
            .thenReturn(false);

        assertFalse(viewAuthZInterceptor.preHandle(request, response, handler));
    }

    @DisplayName("ViewAuthZInterceptor 접근 - 호스트 : 호스트 가입 페이지 접근")
    @Test
    void testInterceptorAccess_HostAndCallJoinView() throws IOException {
        when(request.getAttribute(MEMBER_ALT_ID.getName())).thenReturn(MEMBER_ALT_ID.getName());
        when(interceptorUtils.isMemberHostById(MEMBER_ALT_ID.getName())).thenReturn(true);
        when(request.getRequestURI()).thenReturn(VIEW_HOST_JOIN.getUri());
        when(interceptorUtils.redirectToUrl(response, VIEW_HOST_HOME.getUri()))
            .thenReturn(false);

        assertFalse(viewAuthZInterceptor.preHandle(request, response, handler));
    }

    @DisplayName("ViewAuthZInterceptor 접근 - 회원 : 호스트 가입 페이지 접근")
    @Test
    void testInterceptorAccess_MemberAndCallJoinView() throws IOException {
        when(request.getAttribute(MEMBER_ALT_ID.getName())).thenReturn(MEMBER_ALT_ID.getName());
        when(interceptorUtils.isMemberHostById(MEMBER_ALT_ID.getName())).thenReturn(false);
        when(request.getRequestURI()).thenReturn(VIEW_HOST_JOIN.getUri());

        assertTrue(viewAuthZInterceptor.preHandle(request, response, handler));
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

    @DisplayName("ViewAuthZInterceptor postHandle 접근")
    @Test
    void testInterceptorAccess_postHandleMethod() {
        ModelAndView modelAndView = Mockito.mock(ModelAndView.class);
        when(request.getAttribute(MEMBER_ALT_ID.getName())).thenReturn(MEMBER_ALT_ID.getName());

        viewAuthZInterceptor.postHandle(request, response, handler, modelAndView);
        verify(interceptorUtils).addHostInfoToModel(MEMBER_ALT_ID.getName(), modelAndView);
    }
}
