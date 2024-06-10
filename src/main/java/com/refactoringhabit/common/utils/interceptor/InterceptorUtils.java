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

    public void validateUserInDatabase(HttpServletRequest request, String altId) {

        if (Boolean.TRUE.equals(memberRepository.existsByAltId(altId))) {
            request.setAttribute(MEMBER_ALT_ID.name(), altId);
        } else {
            throw new UserNotFoundException();
        }
    }

    public void handleExpiredToken(HttpServletRequest request, HttpServletResponse response,
        String altId) throws JsonProcessingException {

        authService.reissueToken(request, response, altId);
        request.setAttribute(MEMBER_ALT_ID.name(), altId);
    }

    public boolean redirectToUrl(HttpServletResponse response, String redirectURL)
        throws IOException {

        response.sendRedirect(redirectURL);
        return false;
    }

    public boolean redirectToLogin(HttpServletRequest request, HttpServletResponse response)
        throws IOException {

        request.setAttribute(REDIRECT_URL.name(), request.getRequestURI());
        response.sendRedirect(UriMappings.VIEW_LOGIN.getUri());
        return false;
    }

    public boolean isMemberHostById(String altId) {
        Member member = memberRepository.findByAltId(altId)
            .orElseThrow(UserNotFoundException::new);
        return MemberType.HOST.equals(member.getType());
    }

    public void addMemberInfoToModel(ModelAndView modelAndView, String altId) {
        if (altId != null) {
            memberRepository.findByAltId(altId).ifPresent(member ->
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
