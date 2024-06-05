package com.refactoringhabit.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.refactoringhabit.auth.domain.exception.InvalidTokenException;
import com.refactoringhabit.auth.domain.exception.NullTokenException;
import com.refactoringhabit.common.response.TokenResponse;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TokenUtil {

    private static final String CLAIM_MEMBER_ID = "altId";
    private static final String PREFIX_TOKEN = "Bearer";

    private final Algorithm algorithm;
    private final Long expiredAccessTokenMs;

    public TokenResponse createToken(String altId) {
        Date expiredTimeForAccessToken =
                new Date(System.currentTimeMillis() + expiredAccessTokenMs);

        return TokenResponse.builder()
                .accessToken(JWT.create()
                        .withClaim(CLAIM_MEMBER_ID, altId)
                        .withExpiresAt(expiredTimeForAccessToken)
                        .sign(algorithm))
                .refreshToken(UUID.randomUUID().toString())
                .build();
    }

    public String verifyToken(String token) {
        if (token == null) {
            throw new NullTokenException();
        }

        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT verified = verifier.verify(getTokenNumber(token));
        return verified.getClaim(CLAIM_MEMBER_ID).asString();
    }

    public String getClaimMemberId(String tokenNumber) {
        DecodedJWT decodeToken = JWT.decode(tokenNumber);
        return decodeToken.getClaim(CLAIM_MEMBER_ID).asString();
    }

    public String getTokenNumber(String token) {
        String[] accessTokenParts = token.split(" ");
        if (accessTokenParts.length != 2 || !PREFIX_TOKEN.equals(accessTokenParts[0])) {
            throw new InvalidTokenException();
        }
        return accessTokenParts[1];
    }
}
