package com.refactoringhabit.common.interceptor;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.refactoringhabit.auth.domain.exception.AuthTokenExpiredException;
import com.refactoringhabit.auth.domain.exception.InvalidTokenException;
import com.refactoringhabit.auth.domain.exception.NullTokenException;
import com.refactoringhabit.common.utils.TokenUtil;
import com.refactoringhabit.member.domain.exception.UserNotFoundException;
import com.refactoringhabit.member.domain.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    private static final String PREFIX_FOR_TOKEN = "Bearer";
    private static final String ALLOW_EXPIRED_TOKEN_ENDPOINT = "/api/v2/auth/tokens";
    private static final String MEMBER_ID_ATTRIBUTE = "altId";

    private final TokenUtil tokenUtil;
    private final MemberRepository memberRepository;

    @Override
    public boolean preHandle(
        HttpServletRequest request, HttpServletResponse response, Object handler) {

        log.debug("Access Interceptor");
        String authorization = request.getHeader("Authorization");

        if (authorization == null) {
            throw new NullTokenException();
        }

        String[] accessToken = authorization.split(" ");
        String tokenPrefix = accessToken[0];
        String token = accessToken[1];

        if (!tokenPrefix.equals(PREFIX_FOR_TOKEN)) {
            throw new InvalidTokenException();
        }

        try {
            /* 토큰 유효성 검사시 문제가 없고, 서버의 db에 해당 유저 아이디가 존재할 경우
            api 핸들러를 거치지 않고 ok 상태를 response에 담아서 클라이언트에게 전달 */
            String altId = tokenUtil.verifyToken(token);
            if (Boolean.TRUE.equals(memberRepository.existsByAltId(altId))) {
                response.setStatus(HttpServletResponse.SC_OK);
                return false;
            }
            throw new UserNotFoundException();

        } catch (TokenExpiredException e) {
            /* 토큰 재발급 API 호출시 Access 토큰이
            서버에서 발급된 토큰 이면서
             만료된 토큰일 때에만 호출 가능 */
            if (request.getRequestURI().equals(ALLOW_EXPIRED_TOKEN_ENDPOINT)) {
                request.setAttribute(MEMBER_ID_ATTRIBUTE, tokenUtil.getClaimMemberId(token));
                return true;
            }

            log.error("[{}] ex", e.getClass().getSimpleName(), e);
            throw new AuthTokenExpiredException();

        } catch (Exception e) {
            log.error("[{}] ex ", e.getClass().getSimpleName(), e);
            throw new InvalidTokenException();
        }
    }
}
