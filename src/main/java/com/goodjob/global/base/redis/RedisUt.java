package com.goodjob.global.base.redis;

import com.goodjob.global.base.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

@Service
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

    public void deleteToken(String key) {
        stringRedisTemplate.delete(key);
    }
}
