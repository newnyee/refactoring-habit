package com.refactoringhabit.auth.domain.repository;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisRefreshTokenRepository {

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${token.expire-time.refresh}")
    private Long expiredRefreshTokenTime;

    @Value("${token.prefix-key-refresh-token}")
    private String refreshTokenIdPrefix;

    public void setRefreshToken(String id, String refreshToken) {
        redisTemplate.opsForValue().set(refreshTokenIdPrefix + id, refreshToken,
            Duration.ofSeconds(expiredRefreshTokenTime));
    }

    public String getRefreshToken(String id) {
        return redisTemplate.opsForValue().get(refreshTokenIdPrefix + id);
    }

    public void deleteRefreshTokenById(String id) {
        redisTemplate.delete(refreshTokenIdPrefix + id);
    }
}
