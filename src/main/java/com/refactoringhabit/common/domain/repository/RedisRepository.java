package com.refactoringhabit.common.domain.repository;


import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final CacheManager cacheManager;

    public void setValue(String id, Object value) {
        redisTemplate.opsForValue().set(id, value, Duration.ofDays(2));
    }

    public Object getValue(String id) {
        return redisTemplate.opsForValue().get(id);
    }

    public void setCache(String cacheName, Object key, Object value) {
        redisTemplate.opsForValue().set(cacheName + "::" + key, value);
    }

    public Object getCache(String cacheName, Object key) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            Cache.ValueWrapper valueWrapper = cache.get(key);
            return valueWrapper != null ? valueWrapper.get() : null;
        }
        return null;
    }
}
