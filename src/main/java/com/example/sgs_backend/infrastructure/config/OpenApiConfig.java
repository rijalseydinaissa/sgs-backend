package com.example.sgs_backend.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration Swagger UI / OpenAPI 3.
 *
 * Accessible sur : http://localhost:8080/swagger-ui.html
 * JSON spec sur  : http://localhost:8080/v3/api-docs
 *
 * Intègre l'authentification JWT :
 * Cliquer "Authorize" → saisir "Bearer <token>"
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    private static final String BEARER_AUTH = "bearerAuth";

    @Bean
    public OpenAPI sgsOpenAPI() {
        return new OpenAPI()
                .info(buildInfo())
                .servers(buildServers())
                .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH))
                .components(new Components()
                        .addSecuritySchemes(BEARER_AUTH, buildJwtSecurityScheme()));
    }

    private Info buildInfo() {
        return new Info()
                .title("SGS — API Système de Gestion de Stock")
                .version("2.0.0")
                .description("""
                        **SGS** est un système de gestion de stock multi-catégories pour grandes boutiques et entreprises.
                        
                        ## Authentification
                        1. Appeler `POST /api/v1/auth/login` avec vos identifiants
                        2. Copier le `accessToken` retourné
                        3. Cliquer **Authorize** et entrer : `Bearer <votre_token>`
                        
                        ## Modules disponibles
                        - **Auth** : Authentification JWT + 2FA
                        - **Products** : Catalogue produits, catégories, variantes
                        - **Stock** : Mouvements, inventaires, transferts
                        - **Expenses** : Dépenses opérationnelles, budget, rentabilité
                        - **Orders** : Commandes fournisseurs et clients
                        - **Reports** : Rapports PDF & Excel
                        """)
                .contact(new Contact()
                        .name("Équipe SGS")
                        .email("dev@sgs.com"))
                .license(new License()
                        .name("Propriétaire")
                        .url("https://sgs.com/licence"));
    }

    private List<Server> buildServers() {
        return List.of(
                new Server()
                        .url("http://localhost:" + serverPort)
                        .description("Serveur de développement local"),
                new Server()
                        .url("https://api.sgs.com")
                        .description("Serveur de production")
        );
    }

    private SecurityScheme buildJwtSecurityScheme() {
        return new SecurityScheme()
                .name(BEARER_AUTH)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Entrer le token JWT obtenu via POST /api/v1/auth/login");
    }
}
