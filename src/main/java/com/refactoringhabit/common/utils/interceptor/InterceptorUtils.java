package com.refactoringhabit.common.utils.interceptor;

import static com.refactoringhabit.common.enums.AttributeNames.*;
import static com.refactoringhabit.common.enums.UriAccessLevel.NULL_SESSION_ONLY_URI;
import static com.refactoringhabit.common.enums.UriAccessLevel.PUBLIC_URI;
import static com.refactoringhabit.common.enums.UriMappings.VIEW_HOME;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.refactoringhabit.auth.domain.service.AuthService;
import com.refactoringhabit.common.enums.UriMappings;
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

    public void validateUserInDatabase(HttpServletRequest request, String memberAltId) {

        if (Boolean.TRUE.equals(memberRepository.existsByAltId(memberAltId))) {
            request.setAttribute(MEMBER_ALT_ID.getName(), memberAltId);
        } else {
            throw new UserNotFoundException();
        }
    }

    public void handleExpiredToken(HttpServletRequest request, HttpServletResponse response,
        String memberAltId) throws JsonProcessingException {

        authService.reissueSession(request, response, memberAltId);
        request.setAttribute(MEMBER_ALT_ID.getName(), memberAltId);
    }

    public boolean redirectToUrl(HttpServletResponse response, String redirectURL)
        throws IOException {

        response.sendRedirect(redirectURL);
        return false;
    }

    public boolean redirectToLogin(HttpServletRequest request, HttpServletResponse response)
        throws IOException {

        request.setAttribute(REDIRECT_URL.getName(), request.getRequestURI());
        response.sendRedirect(UriMappings.VIEW_LOGIN.getUri());
        return false;
    }

    public boolean isMemberHostById(String memberAltId) {
        Member member = memberRepository.findByAltId(memberAltId)
            .orElseThrow(UserNotFoundException::new);
        return MemberType.HOST.equals(member.getType());
    }

    public void addMemberInfoToModel(ModelAndView modelAndView, String memberAltId) {
        if (memberAltId != null) {
            memberRepository.findByAltId(memberAltId).ifPresent(member ->
                modelAndView.addObject(
                    MEMBER_INFO.getName(), MemberEntityMapper.INSTANCE.toMemberInfoDto(member))
            );
        }
    }

    public boolean isNullSessionOnlyUri(String nowUri) {
        return NULL_SESSION_ONLY_URI.getUriMappingsList().stream()
            .anyMatch(uriMappings -> nowUri.startsWith(uriMappings.getUri()));
    }

    public boolean isPublicUri(String nowUri) {
        return nowUri.equals(VIEW_HOME.getUri()) || PUBLIC_URI.getUriMappingsList().stream()
            .anyMatch(uriMappings -> nowUri.startsWith(uriMappings.getUri()));
    }
}
