package org.example.projectmanagementsystem.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${jwt.refresh-token-expiration-ms}")
    private long refreshExpirationMs;


    private String buildKey(String username){
        return "refresh_token:" + username;
    }

    public void saveRefreshToken(String username, String refreshToken){
        redisTemplate.opsForValue().set(
                buildKey(username),
                refreshToken,
                refreshExpirationMs,
                TimeUnit.MILLISECONDS
        );
    }

    public void deleteRefreshToken(String username){
        redisTemplate.delete(buildKey(username));
    }

    public boolean isRefreshTokenValid(String username, String refreshToken){
        Object savedToken = redisTemplate.opsForValue().get(buildKey(username));

        return savedToken != null && savedToken.toString().equals(refreshToken);
    }

}
