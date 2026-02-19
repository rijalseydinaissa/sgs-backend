package com.example.sgs_backend.application.product.dto;

import java.util.List;
import java.util.UUID;

public record CategoryResponse(
    UUID   id,
    String code,
    String name,
    String description,
    String iconUrl,
    UUID   parentId,
    String parentName,
    String fullPath,
    int    depth,
    long   productCount,
    List<CategoryResponse> children
) {}
