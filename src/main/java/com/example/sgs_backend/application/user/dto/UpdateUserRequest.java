package com.example.sgs_backend.application.user.dto;
import com.example.sgs_backend.domain.user.RoleName;
import jakarta.validation.constraints.*;
import java.util.Set;
import java.util.UUID;

public record UpdateUserRequest(
    @NotBlank @Email                              String email,
    @NotBlank @Size(max=80)                       String firstName,
    @NotBlank @Size(max=80)                       String lastName,
    @Pattern(regexp="^[+]?[0-9\\s\\-().]{7,20}$") String phone,
    Set<RoleName> roles,
    UUID siteId
) {}
