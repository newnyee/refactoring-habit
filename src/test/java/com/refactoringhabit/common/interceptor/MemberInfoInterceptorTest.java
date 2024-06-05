package com.refactoringhabit.common.interceptor;

import static com.refactoringhabit.common.enums.AttributeNames.ALT_ID;
import static org.mockito.Mockito.*;

import com.refactoringhabit.common.utils.interceptor.InterceptorUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.ModelAndView;

@ExtendWith(MockitoExtension.class)
class MemberInfoInterceptorTest {

    @Mock
    private InterceptorUtils interceptorUtils;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ModelAndView modelAndView;

    @Mock
    private Object handler;

    @InjectMocks
    private MemberInfoInterceptor memberInfoInterceptor;

    @DisplayName("MemberInfoInterceptor 접근 - 회원")
    @Test
    void testInterceptorAccess_Member() {
        when(request.getAttribute(ALT_ID.getName())).thenReturn(ALT_ID.getName());

        memberInfoInterceptor.postHandle(request, response, handler, modelAndView);

        verify(interceptorUtils).addMemberInfoToModel(modelAndView, ALT_ID.getName());
    }
}
