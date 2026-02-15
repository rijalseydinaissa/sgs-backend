package com.example.sgs_backend.application.auth.dto;
public record TwoFactorSetupResponse(String secret, String qrCodeUrl, String[] backupCodes) {}
