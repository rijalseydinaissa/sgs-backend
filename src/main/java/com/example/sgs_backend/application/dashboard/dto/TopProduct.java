package com.example.sgs_backend.application.dashboard.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record TopProduct(
    UUID productId,
    String productName,
    int quantitySold,
    BigDecimal revenue
) {}
