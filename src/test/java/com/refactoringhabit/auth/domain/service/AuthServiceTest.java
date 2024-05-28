package com.refactoringhabit.auth.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.refactoringhabit.auth.domain.exception.EmailingException;
import com.refactoringhabit.auth.dto.FindEmailRequestDto;
import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.member.domain.exception.NotFoundEmailException;
import com.refactoringhabit.member.domain.repository.MemberRepository;
import jakarta.mail.internet.MimeMessage;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JavaMailSender javaMailSender;


    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private static final String TEST_EMAIL_ADDRESS = "test@example.com";
    private static final String ENCODED_PASSWORD = "encodedPassword";

    @BeforeEach
    void setUp() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setCharacterEncoding("UTF-8");

        SpringTemplateEngine springTemplateEngine = new SpringTemplateEngine();
        springTemplateEngine.setTemplateResolver(templateResolver);

        authService = new AuthService(memberRepository, javaMailSender, springTemplateEngine,
            passwordEncoder);
    }

    @DisplayName("이메일찾기 - 성공")
    @Test
    void testFindEmail_Successfully() {
        FindEmailRequestDto findEmailRequestDto = new FindEmailRequestDto();
        when(memberRepository.findEmailByPhoneAndBirth(findEmailRequestDto))
            .thenReturn(Optional.of(TEST_EMAIL_ADDRESS));

        String email = authService.findEmail(findEmailRequestDto);

        assertEquals(TEST_EMAIL_ADDRESS, email);
        verify(memberRepository, times(1))
            .findEmailByPhoneAndBirth(findEmailRequestDto);
    }

    @DisplayName("이메일찾기 - 실패 : 찾을 수 없는 이메일 예외 발생")
    @Test
    void testFindEmail_NotFound() {
        FindEmailRequestDto findEmailRequestDto = new FindEmailRequestDto();
        when(memberRepository.findEmailByPhoneAndBirth(findEmailRequestDto))
            .thenReturn(Optional.empty());

        assertThrows(NotFoundEmailException.class, () -> {
            authService.findEmail(findEmailRequestDto);
        });
    }

    @DisplayName("임시 비밀번호 발급 - 성공")
    @Test
    void testResetPassword_Successfully() {
        String email = TEST_EMAIL_ADDRESS;
        Member member = mock(Member.class);
        when(memberRepository.findByEmail(email)).thenReturn(member);
        when(passwordEncoder.encode(anyString())).thenReturn(ENCODED_PASSWORD);

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        authService.resetPassword(email);

        verify(member, times(1)).updatePassword(anyString());
        verify(javaMailSender, times(1)).send(mimeMessage);
    }

    @DisplayName("임시 비밀번호 발급 - 실패 : 이메일 발송 실패")
    @Test
    void testResetPassword_EmailException() {
        String email = TEST_EMAIL_ADDRESS;
        Member member = mock(Member.class);
        when(memberRepository.findByEmail(email)).thenReturn(member);
        when(passwordEncoder.encode(anyString())).thenReturn(ENCODED_PASSWORD);

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        doThrow(MailSendException.class).when(javaMailSender).send(mimeMessage);

        assertThrows(EmailingException.class, () -> {
            authService.resetPassword(email);
        });

        verify(member, times(1)).updatePassword(anyString());
        verify(javaMailSender, times(1)).send(mimeMessage);
    }
}