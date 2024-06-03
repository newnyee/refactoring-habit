package com.refactoringhabit.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.refactoringhabit.common.response.TokenResponse;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TokenUtil {

    private static final String CLAIM_MEMBER_ID = "altId";

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
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT verified = verifier.verify(token);
        return verified.getClaim(CLAIM_MEMBER_ID).asString();
    }

    public String getClaimMemberId(String token) {
        DecodedJWT decodeToken = JWT.decode(token);
        return decodeToken.getClaim(CLAIM_MEMBER_ID).asString();
    }
}
