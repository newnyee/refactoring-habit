package com.refactoringhabit.auth.domain.service;

import static com.refactoringhabit.common.enums.AttributeNames.MEMBER_ALT_ID;
import static com.refactoringhabit.common.utils.cookies.CookieAttributes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.refactoringhabit.auth.domain.exception.EmailingException;
import com.refactoringhabit.auth.domain.exception.InvalidTokenException;
import com.refactoringhabit.auth.domain.repository.RedisRefreshTokenRepository;
import com.refactoringhabit.auth.dto.FindEmailRequestDto;
import com.refactoringhabit.auth.dto.SignInRequestDto;
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

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Member member;

    @Mock
    private FindEmailRequestDto findEmailRequestDto;

    @Mock
    private SignInRequestDto signInRequestDto;

    @InjectMocks
    private AuthService authService;

    private static final String TEST_EMAIL_ADDRESS = "test@example.com";
    private static final String OLD_REFRESH_TOKEN = "oldRefreshToken";
    private static final String NEW_REFRESH_TOKEN = "newRefreshToken";
    private static final String NEW_ACCESS_TOKEN = "newAccessToken";
    private static final String INVALID_REFRESH_TOKEN = "invalidRefreshToken";

    @DisplayName("이메일찾기 - 성공")
    @Test
    void testFindEmail_Success() {
        when(memberRepository.findEmailByPhoneAndBirth(findEmailRequestDto))
            .thenReturn(Optional.of(TEST_EMAIL_ADDRESS));

        String email = authService.findEmail(findEmailRequestDto);
        assertEquals(TEST_EMAIL_ADDRESS, email);
    }

    @DisplayName("이메일찾기 - 실패 : 찾을 수 없는 이메일 예외 발생")
    @Test
    void testFindEmail_NotFound() {
        when(memberRepository.findEmailByPhoneAndBirth(findEmailRequestDto))
            .thenReturn(Optional.empty());

        assertThrows(NotFoundEmailException.class, () -> {
            authService.findEmail(findEmailRequestDto);
        });
    }

    @DisplayName("임시 비밀번호 발급 - 성공")
    @Test
    void testResetPassword_Success() throws MessagingException {
        when(memberRepository.findByEmail(TEST_EMAIL_ADDRESS))
            .thenReturn(Optional.of(member));

        ArgumentCaptor<String> passwordCaptor = ArgumentCaptor.forClass(String.class);
        when(passwordEncoder.encode(passwordCaptor.capture()))
            .thenAnswer(invocation -> invocation.getArgument(0));

        authService.resetPassword(TEST_EMAIL_ADDRESS);
        verify(emailNewPasswordUtil).sendEmail(TEST_EMAIL_ADDRESS, passwordCaptor.getValue());
    }

    @DisplayName("임시 비밀번호 발급 - 실패 : 이메일 발송 실패")
    @Test
    void testResetPassword_EmailException() throws MessagingException {
        when(memberRepository.findByEmail(TEST_EMAIL_ADDRESS))
            .thenReturn(Optional.of(member));

        ArgumentCaptor<String> passwordCaptor = ArgumentCaptor.forClass(String.class);
        when(passwordEncoder.encode(passwordCaptor.capture()))
            .thenAnswer(invocation -> invocation.getArgument(0));

        doThrow(new MessagingException()).when(emailNewPasswordUtil)
            .sendEmail(eq(TEST_EMAIL_ADDRESS), passwordCaptor.capture());

        assertThrows(EmailingException.class, () -> {
            authService.resetPassword(TEST_EMAIL_ADDRESS);
        });
    }

    @DisplayName("사용자 인증 후 토큰 발급 - 성공")
    @Test
    void testAuthenticationAndCreateToken_Success() {
        TokenResponse createdToken = TokenResponse.builder()
            .refreshToken(NEW_REFRESH_TOKEN)
            .accessToken(NEW_ACCESS_TOKEN)
            .build();
        when(memberRepository.findByEmail(signInRequestDto.getEmail()))
            .thenReturn(Optional.of(member));
        when(member.getAltId()).thenReturn(MEMBER_ALT_ID.getName());
        when(passwordEncoder.matches(signInRequestDto.getPassword(), member.getEncodedPassword()))
            .thenReturn(true);
        when(tokenUtil.createToken(MEMBER_ALT_ID.getName())).thenReturn(createdToken);

        authService.authenticationAndCreateToken(response, signInRequestDto);

        verify(redisRefreshTokenRepository)
            .setRefreshToken(MEMBER_ALT_ID.getName(), NEW_REFRESH_TOKEN);
        verify(cookieUtil)
            .createTokenCookie(response, REFRESH_TOKEN_COOKIE_NAME, NEW_REFRESH_TOKEN);
        verify(cookieUtil)
            .createTokenCookie(response, ACCESS_TOKEN_COOKIE_NAME, NEW_ACCESS_TOKEN);
    }

    @DisplayName("사용자 인증 후 토큰 발급 - 실패 : 사용자를 찾을 수 없음")
    @Test
    void testAuthenticationAndCreateToken_UserNotFound() {
        when(memberRepository.findByEmail(signInRequestDto.getEmail()))
            .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            authService.authenticationAndCreateToken(response, signInRequestDto);
        });
    }

   @DisplayName("토큰 재발급 - 성공")
    @Test
    void testReissueToken_Success() {
        TokenResponse createdToken = TokenResponse.builder()
            .refreshToken(NEW_REFRESH_TOKEN)
            .accessToken(NEW_ACCESS_TOKEN)
            .build();
        when(redisRefreshTokenRepository.getRefreshToken(MEMBER_ALT_ID.getName()))
            .thenReturn(OLD_REFRESH_TOKEN);
        when(cookieUtil.getTokenInCookie(request, REFRESH_TOKEN_COOKIE_NAME))
            .thenReturn(OLD_REFRESH_TOKEN);
        when(tokenUtil.createToken(MEMBER_ALT_ID.getName())).thenReturn(createdToken);

        authService.reissueToken(request, response, MEMBER_ALT_ID.getName());

        verify(redisRefreshTokenRepository)
            .setRefreshToken(MEMBER_ALT_ID.getName(), NEW_REFRESH_TOKEN);
        verify(cookieUtil)
            .createTokenCookie(response, REFRESH_TOKEN_COOKIE_NAME, NEW_REFRESH_TOKEN);
        verify(cookieUtil)
            .createTokenCookie(response, ACCESS_TOKEN_COOKIE_NAME, NEW_ACCESS_TOKEN);
    }

    @DisplayName("토큰 재발급 - 실패 : 유효하지 않은 리프래시 토큰")
    @Test
    void testReissueToken_InvalidToken() {
        when(redisRefreshTokenRepository.getRefreshToken(MEMBER_ALT_ID.getName()))
            .thenReturn(OLD_REFRESH_TOKEN);
        when(cookieUtil.getTokenInCookie(request, REFRESH_TOKEN_COOKIE_NAME))
            .thenReturn(INVALID_REFRESH_TOKEN);

        assertThrows(InvalidTokenException.class, () -> {
            authService.reissueToken(request, response, MEMBER_ALT_ID.getName());
        });
    }
}
