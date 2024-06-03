package com.refactoringhabit.common.config.jwt;

import com.auth0.jwt.algorithms.Algorithm;
import com.refactoringhabit.common.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret-key}")
    private String jwtSecretKey;

    @Value("${token.expire-time.access}")
    private Long expiredAccessTokenMs;

    @Bean
    public TokenUtil jwtUtil() {
        return new TokenUtil(
            Algorithm.HMAC256(jwtSecretKey),
            expiredAccessTokenMs
        );
    }
}
