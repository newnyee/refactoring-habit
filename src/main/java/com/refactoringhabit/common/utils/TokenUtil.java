package com.refactoringhabit.common.utils;

import static com.refactoringhabit.common.enums.AttributeNames.MEMBER_ALT_ID;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.refactoringhabit.auth.domain.exception.NullTokenException;
import com.refactoringhabit.common.response.Session;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TokenUtil {

    private final Algorithm algorithm;
    private final Long expiredAccessTokenMs;

    public Session createToken(String memberAltId) {
        Date expiredTimeForAccessToken =
                new Date(System.currentTimeMillis() + expiredAccessTokenMs);

        return Session.builder()
                .accessToken(JWT.create()
                        .withClaim(MEMBER_ALT_ID.getName(), memberAltId)
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
        DecodedJWT verified = verifier.verify(token);
        return verified.getClaim(MEMBER_ALT_ID.getName()).asString();
    }

    public String getClaimMemberId(String token) {
        DecodedJWT decodeToken = JWT.decode(token);
        return decodeToken.getClaim(MEMBER_ALT_ID.getName()).asString();
    }
}
