package com.refactoringhabit.auth.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.refactoringhabit.auth.domain.exception.EmailingException;
import com.refactoringhabit.auth.domain.exception.InvalidTokenException;
import com.refactoringhabit.auth.domain.repository.RedisRefreshTokenRepository;
import com.refactoringhabit.auth.dto.FindEmailRequestDto;
import com.refactoringhabit.auth.dto.SignInRequestDto;
import com.refactoringhabit.auth.dto.SignInResponseDto;
import com.refactoringhabit.common.response.TokenResponse;
import com.refactoringhabit.common.utils.EmailNewPasswordUtil;
import com.refactoringhabit.common.utils.TokenUtil;
import com.refactoringhabit.common.utils.cookies.CookieUtil;
import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.member.domain.exception.NotFoundEmailException;
import com.refactoringhabit.member.domain.exception.UserNotFoundException;
import com.refactoringhabit.member.domain.repository.MemberRepository;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailNewPasswordUtil emailNewPasswordUtil;

    @Mock
    private TokenUtil tokenUtil;

    @Mock
    private RedisRefreshTokenRepository redisRefreshTokenRepository;

    @Mock
    private CookieUtil cookieUtil;

    @InjectMocks
    private AuthService authService;

    private static final String TEST_EMAIL_ADDRESS = "test@example.com";
    private static final String ANY_STRING = "anyString";
    private static final String MEMBER_ID = "memberId";
    private static final String OLD_REFRESH_TOKEN = "oldRefreshToken";
    private static final String INVALID_REFRESH_TOKEN = "invalidRefreshToken";

    @DisplayName("이메일찾기 - 성공")
    @Test
    void testFindEmail_Success() {
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
    void testResetPassword_Success() throws MessagingException {
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

    @DisplayName("사용자 인증 후 토큰 발급 - 성공")
    @Test
    void testAuthenticationAndCreateToken_Success() {
        HttpServletResponse response = new MockHttpServletResponse();
        SignInRequestDto signInRequestDto = mock(SignInRequestDto.class);

        Member member = mock(Member.class);

        TokenResponse tokenResponse = TokenResponse.builder()
            .accessToken(ANY_STRING)
            .expiredTimeForAccessToken(ANY_STRING)
            .refreshToken(ANY_STRING)
            .expiredTimeForRefreshToken(ANY_STRING)
            .build();

        when(memberRepository.findByEmail(signInRequestDto.getEmail()))
            .thenReturn(Optional.of(member));
        when(passwordEncoder.matches(signInRequestDto.getPassword(), member.getEncodedPassword()))
            .thenReturn(true);
        when(tokenUtil.createToken(member.getAltId())).thenReturn(tokenResponse);

        SignInResponseDto responseDto =
            authService.authenticationAndCreateToken(response, signInRequestDto);

        assertEquals(tokenResponse, responseDto.tokenResponse());
        assertEquals(member.getNickName(), responseDto.nickName());
        assertEquals(member.getProfileImage(), responseDto.profileImage());

        verify(redisRefreshTokenRepository)
            .setRefreshToken(member.getAltId(), tokenResponse.refreshToken());
        verify(cookieUtil).createRefreshTokenCookie(response, tokenResponse.refreshToken());
    }

    @DisplayName("사용자 인증 후 토큰 발급 - 실패 : 사용자를 찾을 수 없음")
    @Test
    void testAuthenticationAndCreateToken_UserNotFound() {
        HttpServletResponse response = new MockHttpServletResponse();
        SignInRequestDto signInRequestDto = mock(SignInRequestDto.class);

        when(memberRepository.findByEmail(signInRequestDto.getEmail()))
            .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            authService.authenticationAndCreateToken(response, signInRequestDto);
        });
    }

    @DisplayName("토큰 재발급 - 성공")
    @Test
    void testReissueToken_Success() {
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();

        TokenResponse tokenResponse = TokenResponse.builder()
            .accessToken(ANY_STRING)
            .expiredTimeForAccessToken(ANY_STRING)
            .refreshToken(ANY_STRING)
            .expiredTimeForRefreshToken(ANY_STRING)
            .build();

        when(redisRefreshTokenRepository.getRefreshToken(MEMBER_ID))
            .thenReturn(OLD_REFRESH_TOKEN);
        when(cookieUtil.getRefreshTokenInCookie(request)).thenReturn(OLD_REFRESH_TOKEN);
        when(tokenUtil.createToken(MEMBER_ID)).thenReturn(tokenResponse);

        TokenResponse result = authService.reissueToken(request, response, MEMBER_ID);

        assertEquals(tokenResponse, result);
        verify(redisRefreshTokenRepository)
            .setRefreshToken(MEMBER_ID, tokenResponse.refreshToken());
        verify(cookieUtil).createRefreshTokenCookie(response, tokenResponse.refreshToken());
    }

    @DisplayName("토큰 재발급 - 실패 : 유효하지 않은 리프래시 토큰")
    @Test
    void testReissueToken_InvalidToken() {
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();

        when(redisRefreshTokenRepository.getRefreshToken(MEMBER_ID))
            .thenReturn(OLD_REFRESH_TOKEN);
        when(cookieUtil.getRefreshTokenInCookie(request)).thenReturn(INVALID_REFRESH_TOKEN);

        assertThrows(InvalidTokenException.class, () -> {
            authService.reissueToken(request, response, MEMBER_ID);
        });
    }
}
