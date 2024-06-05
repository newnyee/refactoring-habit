package com.refactoringhabit.common.utils.interceptor;

import static com.refactoringhabit.common.enums.AttributeNames.*;
import static com.refactoringhabit.common.enums.UrlMappings.PREFIX_API;

import com.refactoringhabit.auth.domain.service.AuthService;
import com.refactoringhabit.common.enums.UrlMappings;
import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.member.domain.enums.MemberType;
import com.refactoringhabit.member.domain.exception.UserNotFoundException;
import com.refactoringhabit.member.domain.mapper.MemberEntityMapper;
import com.refactoringhabit.member.domain.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

@Component
@RequiredArgsConstructor
public class InterceptorUtils {

    private final MemberRepository memberRepository;
    private final AuthService authService;

    public void validateUserInDatabase(HttpServletRequest request, String altId) {

        if (Boolean.TRUE.equals(memberRepository.existsByAltId(altId))) {
            request.setAttribute(ALT_ID.name(), altId);
        } else {
            throw new UserNotFoundException();
        }
    }

    public void handleExpiredToken(HttpServletRequest request, HttpServletResponse response,
        String altId) {

        authService.reissueToken(request, response, altId);
        request.setAttribute(ALT_ID.name(), altId);
    }

    public boolean redirectToUrl(HttpServletResponse response, String redirectURL)
        throws IOException {

        response.sendRedirect(redirectURL);
        return false;
    }

    public boolean redirectToLogin(HttpServletRequest request, HttpServletResponse response)
        throws IOException {

        request.setAttribute(REDIRECT_URL.name(), request.getRequestURI());
        response.sendRedirect(UrlMappings.VIEW_LOGIN.getUrl());
        return false;
    }

    public boolean isMemberHostById(String altId) {
        Member member = memberRepository.findByAltId(altId)
            .orElseThrow(UserNotFoundException::new);
        return MemberType.HOST.equals(member.getType());
    }

    public boolean isApiUrl(HttpServletRequest request) {
        return request.getRequestURI().startsWith(PREFIX_API.getUrl());
    }

    public void addMemberInfoToModel(ModelAndView modelAndView, String altId) {
        if (altId != null) {
            memberRepository.findByAltId(altId).ifPresent(member ->
                modelAndView.addObject(
                    MEMBER_INFO.getName(), MemberEntityMapper.INSTANCE.toMemberInfoDto(member))
            );
        }
    }
}