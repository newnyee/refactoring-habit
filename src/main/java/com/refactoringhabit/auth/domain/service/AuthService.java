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
    public String findEmail(FindEmailRequestDto findEmailRequestDto) {
        return memberRepository.findEmailByPhoneAndBirth(findEmailRequestDto)
            .orElseThrow(NotFoundEmailException::new);
    }

    @Timer
    @Transactional
    public void resetPassword(String email) {
        String newPassword = createPassword();
        Member member =
            memberRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        member.changePassword(passwordEncoder.encode(newPassword));

        try {
            emailNewPasswordUtil.sendEmail(email, newPassword);
        } catch (MessagingException | MailException e) {
            log.error("[{}] {}", e.getClass().getSimpleName(), e.getMessage());
            throw new EmailingException();
        }
    }

    @Transactional
    public void authenticationAndCreateToken(
        HttpServletResponse response, SignInRequestDto signInRequestDto)
        throws JsonProcessingException {

        Member member = memberRepository.findByEmail(signInRequestDto.getEmail())
                .orElseThrow(UserNotFoundException::new);
        String altId = member.getAltId();

        if (passwordEncoder.matches(signInRequestDto.getPassword(), member.getEncodedPassword())) {
            Session createdToken = tokenUtil.createToken(altId);
            String createdRefreshToken = createdToken.refreshToken();

            redisRefreshTokenRepository.setRefreshToken(altId, createdRefreshToken);

            cookieUtil.createSessionCookie(response, createdToken);
        } else {
            throw new PasswordNotMatchException();
        }
    }

    @Transactional
    public void reissueToken(
        HttpServletRequest request, HttpServletResponse response, String altId)
        throws JsonProcessingException {

        Session valueInCookie =
            cookieUtil.getValueInCookie(request, SESSION_COOKIE_NAME.getName(), Session.class);

        if (redisRefreshTokenRepository.getRefreshToken(altId)
            .equals(valueInCookie.refreshToken())) {

            Session createdToken = tokenUtil.createToken(altId);
            String createdRefreshToken = createdToken.refreshToken();

            redisRefreshTokenRepository.setRefreshToken(altId, createdRefreshToken);

            cookieUtil.createSessionCookie(response, createdToken);
        } else {
            throw new InvalidTokenException();
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
}
