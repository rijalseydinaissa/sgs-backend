package com.example.sgs_backend.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

/**
 * Configuration type-safe depuis application.yml.
 * Toutes les propriétés app.* sont injectées ici.
 * Préférer @ConfigurationProperties à @Value pour les groupes de config.
 */
@Component
@ConfigurationProperties(prefix = "app")
@Data
@Validated
public class ApplicationProperties {

    private Security security = new Security();
    private Pagination pagination = new Pagination();
    private Stock stock = new Stock();
    private Storage storage = new Storage();

    @Data
    public static class Security {
        private Jwt jwt = new Jwt();

        @Data
        public static class Jwt {
            @NotBlank
            private String secret;
            @Positive
            private long expirationMs = 3_600_000L;        // 1h par défaut
            @Positive
            private long refreshExpirationMs = 604_800_000L; // 7j par défaut
        }
    }

    @Data
    public static class Pagination {
        private int defaultPageSize = 20;
        private int maxPageSize = 100;
    }

    @Data
    public static class Stock {
        private int lowStockAlertThreshold = 10;
        private int expiryAlertDays = 30;
    }

    @Data
    public static class Storage {
        private String type = "local";   // local | s3
        private String basePath = "uploads/";
        private long maxFileSizeBytes = 5_242_880L;  // 5MB
    }
}
