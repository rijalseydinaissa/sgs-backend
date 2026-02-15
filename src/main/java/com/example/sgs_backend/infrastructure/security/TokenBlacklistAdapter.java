package com.example.sgs_backend.infrastructure.security;

import com.example.sgs_backend.application.auth.port.TokenBlacklistPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Component @RequiredArgsConstructor
public class TokenBlacklistAdapter implements TokenBlacklistPort {

    private static final String PREFIX = "blacklist:jwt:";
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void blacklist(String token, long expirationMs) {
        redisTemplate.opsForValue().set(PREFIX + token.hashCode(), "revoked", expirationMs, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(PREFIX + token.hashCode()));
    }
}
