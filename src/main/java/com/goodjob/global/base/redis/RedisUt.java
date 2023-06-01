package com.goodjob.global.base.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisUt {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // 리프레시 토큰 생성
    public String genRefreshToken() {
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public long getExpire(String key) {
        return stringRedisTemplate.getExpire(key);
    }

    public String getRefreshToken(String key) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();

        return valueOperations.get(key);
    }

    public void setRefreshToken(String key, String value, long timeout) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set(key, value, timeout, TimeUnit.MILLISECONDS);
    }

    public void deleteRefreshToken(String key) {
        // TODO: 로그아웃 시 액세스 토큰 만료시키기
        stringRedisTemplate.delete(key);
    }
}
