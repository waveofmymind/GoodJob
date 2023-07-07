package com.goodjob.core.global.base.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisUt {

    @Autowired
    private RedisTemplate redisTemplate;

    public <T> long getExpire(T key) {
        return redisTemplate.getExpire(key);
    }

    public <T> boolean hasValue(T key) {
        ValueOperations<Object, String> valueOperations = redisTemplate.opsForValue();
        String refreshToken = valueOperations.get(key);

        if (refreshToken == null) {
            return false;
        }

        return true;
    }

    public <T> String getValue(T key) {
        ValueOperations<Object, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    public <T> void setValue(T key, String value, long timeout) {
        ValueOperations<Object, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value, timeout, TimeUnit.MILLISECONDS);
    }

    public void delete(Object key) {
        redisTemplate.delete(key);
    }
}
