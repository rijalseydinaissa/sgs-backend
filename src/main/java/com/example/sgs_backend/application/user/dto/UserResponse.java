package com.example.sgs_backend.application.user.dto;

import com.example.sgs_backend.domain.user.UserStatus;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record UserResponse(
        UUID id, String username, String email,
        String firstName, String lastName, String fullName,
        String phone, UserStatus status, boolean twoFactorEnabled,
        Set<String> roles, UUID siteId, String siteName,
        LocalDateTime lastLoginAt, LocalDateTime createdAt, String createdBy
) {}
