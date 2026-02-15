package com.example.sgs_backend.infrastructure.persistence.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Configuration de l'audit JPA.
 *
 * Fournit l'utilisateur courant pour les champs @CreatedBy / @LastModifiedBy.
 * Si aucun utilisateur authentifié (ex: migrations Flyway), utilise "SYSTEM".
 */
@Configuration
public class JpaAuditConfig {

    @Bean(name = "auditorAware")
    public AuditorAware<String> auditorAware() {
        return () -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
                return Optional.of("SYSTEM");
            }
            return Optional.of(auth.getName());
        };
    }
}
