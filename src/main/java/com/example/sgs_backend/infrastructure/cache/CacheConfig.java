package com.example.sgs_backend.infrastructure.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Configuration cache + scheduling
 */
@Configuration
@EnableCaching
@EnableScheduling
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(
            "dashboardKPIs",  // KPIs dashboard (TTL 1h via @Scheduled)
            "reports",        // Rapports récents
            "topProducts"     // Top produits (refresh daily)
        );
    }
}
