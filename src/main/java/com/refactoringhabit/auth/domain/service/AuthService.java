package com.refactoringhabit.auth.domain.service;

import static com.refactoringhabit.common.enums.AttributeNames.SESSION_COOKIE_NAME;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.refactoringhabit.auth.domain.exception.EmailingException;
import com.refactoringhabit.auth.domain.exception.InvalidTokenException;
import com.refactoringhabit.auth.domain.exception.PasswordNotMatchException;
import com.refactoringhabit.auth.domain.repository.RedisRefreshTokenRepository;
import com.refactoringhabit.auth.dto.FindEmailRequestDto;
import com.refactoringhabit.auth.dto.SignInRequestDto;
import com.refactoringhabit.common.annotation.Timer;
import com.refactoringhabit.common.response.Session;
import com.refactoringhabit.common.utils.cookies.CookieUtil;
import com.refactoringhabit.common.utils.EmailNewPasswordUtil;
import com.refactoringhabit.common.utils.TokenUtil;
import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.member.domain.exception.NotFoundEmailException;
import com.refactoringhabit.member.domain.exception.UserNotFoundException;
import com.refactoringhabit.member.domain.repository.MemberRepository;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenUtil tokenUtil;
    private final EmailNewPasswordUtil emailNewPasswordUtil;
    private final RedisRefreshTokenRepository redisRefreshTokenRepository;
    private final CookieUtil cookieUtil;

    private final Random random = new Random();

    @Transactional(readOnly = true)
    public boolean emailCheck(String email) {
        return memberRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public String findEmail(FindEmailRequestDto findEmailRequestDto) {
        return memberRepository.findEmailByPhoneAndBirth(findEmailRequestDto)
            .orElseThrow(NotFoundEmailException::new);
    }

    @Timer
    @Transactional
    public void resetPassword(String email) {
        String newPassword = createPassword();
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(UserNotFoundException::new);
        member.setEncodedPassword(passwordEncoder.encode(newPassword));

        try {
            emailNewPasswordUtil.sendEmail(email, newPassword);
        } catch (MessagingException | MailException e) {
            log.error("[{}] {}", e.getClass().getSimpleName(), e.getMessage());
            throw new EmailingException();
        }
    }

    @Transactional
    public void authenticationAndCreateSession(
        HttpServletResponse response, SignInRequestDto signInRequestDto)
        throws JsonProcessingException {

        Member member = memberRepository.findByEmail(signInRequestDto.getEmail())
                .orElseThrow(UserNotFoundException::new);
        if (isPasswordMatched(signInRequestDto.getPassword(), member.getEncodedPassword())) {
            createAndSaveSession(response, member.getAltId());
        } else {
            throw new PasswordNotMatchException();
        }
    }

    @Transactional
    public void reissueSession(
        HttpServletRequest request, HttpServletResponse response, String memberAltId)
        throws JsonProcessingException {

        Session valueInCookie =
            cookieUtil.getValueInCookie(request, SESSION_COOKIE_NAME.getName(), Session.class);

        if (redisRefreshTokenRepository.getRefreshToken(memberAltId)
            .equals(valueInCookie.refreshToken())) {
            createAndSaveSession(response, memberAltId);
        } else {
            throw new InvalidTokenException();
        }
    }

    @Transactional
    public void removeSession(HttpServletRequest request, HttpServletResponse response) {

        try {
            Session sessionCookie =
                getSessionFromRequest(request, SESSION_COOKIE_NAME.getName());

            if (sessionCookie != null) {
                String memberAltId = tokenUtil.getClaimMemberId(sessionCookie.accessToken());
                redisRefreshTokenRepository.deleteRefreshTokenById(memberAltId);
            }

        } catch (Exception e) {
            log.error("[{}] ex", e.getClass().getSimpleName(), e);
        } finally {
            cookieUtil.removeSessionCookie(response, SESSION_COOKIE_NAME.getName());
        }
    }

    @Transactional
    public void verifyPassword(String memberAltId, String password) {
        Member member =
            memberRepository.findByAltId(memberAltId).orElseThrow(UserNotFoundException::new);

        if (!isPasswordMatched(password, member.getEncodedPassword())) {
            throw new PasswordNotMatchException();
        }
    }

    private String createPassword() {
        StringBuilder newPassword = new StringBuilder();
        boolean run = true;
        while (run) {
            int num = random.nextInt(122);
            if ((num >= 48 && num <= 57) || (num >= 65 && num <= 90) || (num >= 97)) {
                newPassword.append((char) num);
            }
            if (newPassword.length() == 10) {
                run = false;
            }
        }
        return newPassword.toString();
    }

    private void createAndSaveSession(HttpServletResponse response, String memberAltId)
        throws JsonProcessingException {
        Session createdToken = tokenUtil.createToken(memberAltId);
        String createdRefreshToken = createdToken.refreshToken();

        redisRefreshTokenRepository.setRefreshToken(memberAltId, createdRefreshToken);

        cookieUtil.createSessionCookie(response, createdToken);
    }

    private Session getSessionFromRequest(HttpServletRequest request, String cookieName)
        throws JsonProcessingException {
        return cookieUtil.getValueInCookie(request, cookieName, Session.class);
    }

    private boolean isPasswordMatched(String password, String encodedPassword) {
        return passwordEncoder.matches(password, encodedPassword);
    }
}
