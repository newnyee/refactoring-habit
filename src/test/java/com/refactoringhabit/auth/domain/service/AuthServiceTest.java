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
import com.refactoringhabit.common.utils.EmailNewPasswordUtil;
import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.member.domain.exception.NotFoundEmailException;
import com.refactoringhabit.member.domain.repository.MemberRepository;
import jakarta.mail.MessagingException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailNewPasswordUtil emailNewPasswordUtil;

    @InjectMocks
    private AuthService authService;

    private static final String TEST_EMAIL_ADDRESS = "test@example.com";

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
    void testResetPassword_Successfully() throws MessagingException {
        Member member = mock(Member.class);
        when(memberRepository.findByEmail(TEST_EMAIL_ADDRESS))
            .thenReturn(Optional.ofNullable(member));

        ArgumentCaptor<String> passwordCaptor = ArgumentCaptor.forClass(String.class);
        when(passwordEncoder.encode(passwordCaptor.capture()))
            .thenAnswer(invocation -> invocation.getArgument(0));

        authService.resetPassword(TEST_EMAIL_ADDRESS);

        verify(memberRepository, times(1))
            .findByEmail(TEST_EMAIL_ADDRESS);
        verify(passwordEncoder, times(1))
            .encode(anyString());
        verify(emailNewPasswordUtil, times(1))
            .sendEmail(TEST_EMAIL_ADDRESS, passwordCaptor.getValue());
    }

    @DisplayName("임시 비밀번호 발급 - 실패 : 이메일 발송 실패")
    @Test
    void testResetPassword_EmailException() throws MessagingException {
        Member member = mock(Member.class);
        when(memberRepository.findByEmail(TEST_EMAIL_ADDRESS))
            .thenReturn(Optional.ofNullable(member));

        ArgumentCaptor<String> passwordCaptor = ArgumentCaptor.forClass(String.class);
        when(passwordEncoder.encode(passwordCaptor.capture()))
            .thenAnswer(invocation -> invocation.getArgument(0));

        doThrow(new MessagingException()).when(emailNewPasswordUtil)
            .sendEmail(anyString(), anyString());

        assertThrows(EmailingException.class, () -> {
            authService.resetPassword(TEST_EMAIL_ADDRESS);
        });

        verify(memberRepository, times(1))
            .findByEmail(TEST_EMAIL_ADDRESS);
        verify(passwordEncoder, times(1))
            .encode(anyString());
        verify(emailNewPasswordUtil, times(1))
            .sendEmail(TEST_EMAIL_ADDRESS, passwordCaptor.getValue());
    }
}
