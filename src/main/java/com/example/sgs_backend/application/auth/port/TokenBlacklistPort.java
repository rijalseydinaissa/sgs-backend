package com.example.sgs_backend.application.auth.port;

public interface TokenBlacklistPort {
    void blacklist(String token, long expirationMs);
    boolean isBlacklisted(String token);
}
