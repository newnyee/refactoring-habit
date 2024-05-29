package com.refactoringhabit.auth.domain.service;

import com.refactoringhabit.auth.domain.exception.EmailingException;
import com.refactoringhabit.auth.dto.FindEmailRequestDto;
import com.refactoringhabit.common.annotation.Timer;
import com.refactoringhabit.common.utils.EmailNewPasswordUtil;
import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.member.domain.exception.NotFoundEmailException;
import com.refactoringhabit.member.domain.repository.MemberRepository;
import jakarta.mail.MessagingException;
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
    private final EmailNewPasswordUtil emailNewPasswordUtil;

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
        Member member = memberRepository.findByEmail(email);
        member.updatePassword(passwordEncoder.encode(newPassword));

        try {
            emailNewPasswordUtil.sendEmail(email, newPassword);
        } catch (MessagingException | MailException e) {
            log.error("[{}] {}", e.getClass().getSimpleName(), e.getMessage());
            throw new EmailingException();
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
