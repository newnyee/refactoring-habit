package com.refactoringhabit.auth.domain.service;

import com.refactoringhabit.auth.domain.exception.EmailingException;
import com.refactoringhabit.auth.dto.FindEmailRequestDto;
import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.member.domain.exception.NotFoundEmailException;
import com.refactoringhabit.member.domain.repository.MemberRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine springTemplateEngine;
    private final PasswordEncoder passwordEncoder;

    private final Random random = new Random();
    private static final String EMAIL_SUBJECT = "[habit] 임시 비밀번호 발급";
    private static final String EMAIL_TEMPLATE_NAME = "/pages/reset-password";

    @Transactional(readOnly = true)
    public String findEmail(FindEmailRequestDto findEmailRequestDto) {
        return memberRepository.findEmailByPhoneAndBirth(findEmailRequestDto)
            .orElseThrow(NotFoundEmailException::new);
    }

    @Transactional
    public void resetPassword(String email) {
        String newPassword = createPassword();
        Member member = memberRepository.findByEmail(email);
        member.updatePassword(passwordEncoder.encode(newPassword));

        try {
            sendEmail(email, newPassword);
        } catch (MessagingException e) {
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

    private String setContext(String newPassword) {
        Context context = new Context();
        context.setVariable("newPassword", newPassword);
        return springTemplateEngine.process(EMAIL_TEMPLATE_NAME, context);
    }

    private void sendEmail(String email, String newPassword) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper
            = new MimeMessageHelper(mimeMessage, false, "UTF-8");

        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject(EMAIL_SUBJECT);
        mimeMessageHelper.setText(setContext(newPassword), true);
        javaMailSender.send(mimeMessage);
    }
}
