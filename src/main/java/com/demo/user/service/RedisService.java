package com.demo.user.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void save(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }

    public void put(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public void storeRefreshToken(String refreshToken, String userId) {
        String key = "refresh_token:" + refreshToken;
        stringRedisTemplate.opsForValue().set(key, userId, 7, TimeUnit.DAYS);
    }

    public String validateRefreshToken(String refreshToken) {
        System.out.println("refreshToken------------------->" + refreshToken);
        String key = "refresh_token:" + refreshToken;
        var userId = get(key);
        System.out.println("key------------------->" + key);
        System.out.println("userId------------------->" + userId);
        if (userId == null) {
            throw new RuntimeException("Please login again");
        }
        return userId;
    }

    public void blackListAccessToken(String accessToken) {
        String key = "blacklisted_access_token:" + accessToken;
        stringRedisTemplate.opsForValue().set(key, "true", 1, TimeUnit.MINUTES);
    }

    public boolean isAccessTokenBlacklisted(String accessToken) {
        String key = "blacklisted_access_token:" + accessToken;
        return stringRedisTemplate.hasKey(key);
    }

}
