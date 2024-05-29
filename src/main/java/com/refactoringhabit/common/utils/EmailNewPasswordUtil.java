package com.refactoringhabit.common.utils;

import com.refactoringhabit.common.annotation.Timer;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Component
@RequiredArgsConstructor
public class EmailNewPasswordUtil {

    private final SpringTemplateEngine springTemplateEngine;
    private final JavaMailSender javaMailSender;

    private static final String EMAIL_SUBJECT = "[habit] 임시 비밀번호 발급";
    private static final String EMAIL_TEMPLATE_NAME = "/pages/reset-password";

    private String setContext(String newPassword) {
        Context context = new Context();
        context.setVariable("newPassword", newPassword);
        return springTemplateEngine.process(EMAIL_TEMPLATE_NAME, context);
    }

    @Timer
    @Async
    public void sendEmail(String email, String newPassword)
        throws MessagingException, MailException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper
            = new MimeMessageHelper(mimeMessage, false, "UTF-8");

        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject(EMAIL_SUBJECT);
        mimeMessageHelper.setText(setContext(newPassword), true);
        javaMailSender.send(mimeMessage);
    }
}
