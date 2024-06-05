package com.refactoringhabit.common.interceptor;

import static com.refactoringhabit.common.enums.AttributeNames.ALT_ID;

import com.refactoringhabit.common.utils.interceptor.InterceptorUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
@RequiredArgsConstructor
public class MemberInfoInterceptor implements HandlerInterceptor {

    private final InterceptorUtils interceptorUtils;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler, ModelAndView modelAndView) {

        // 회원인 경우 request attribute에 아이디가 저장 되어있음
        String altId = (String) request.getAttribute(ALT_ID.getName());
        interceptorUtils.addMemberInfoToModel(modelAndView, altId);
    }
}
