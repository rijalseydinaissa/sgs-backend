package com.example.sgs_backend.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configuration de sécurité Spring Security — Sprint 1 (base).
 *
 * Sprint 2 ajoutera : JwtAuthenticationFilter, UserDetailsService, 2FA.
 *
 * Pour l'instant :
 * - Swagger UI est accessible sans authentification
 * - Actuator health est public
 * - Tout le reste nécessite une authentification (préparation Sprint 2)
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    // Endpoints publics (pas besoin d'être authentifié)
    private static final String[] PUBLIC_ENDPOINTS = {
            // Swagger UI
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**",
            // Actuator
            "/actuator/health",
            "/actuator/info",
            // Auth (sera complété Sprint 2)
            "/api/v1/auth/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Désactiver CSRF (API REST stateless)
                .csrf(AbstractHttpConfigurer::disable)

                // CORS configuré via corsConfigurationSource()
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Pas de session HTTP — JWT = stateless
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Règles d'autorisation
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()  // Preflight CORS
                        .anyRequest().authenticated()
                )

                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt force 12 — bon équilibre sécurité / performance
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of(
                "http://localhost:*",
                "http://127.0.0.1:*",
                "https://*.sgs.com"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization", "X-Total-Count", "X-Page-Number"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
