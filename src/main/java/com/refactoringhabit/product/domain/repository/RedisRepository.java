package com.refactoringhabit.product.domain.repository;


import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public void setLongValue(String id, Long value) {
        redisTemplate.opsForValue().set(id, String.valueOf(value), Duration.ofDays(2));
    }

    public Long getLongValue(String id) {
        String stringValue = redisTemplate.opsForValue().get(id);
        return stringValue != null
            ? Long.parseLong(stringValue)
            : 0;
    }
}
