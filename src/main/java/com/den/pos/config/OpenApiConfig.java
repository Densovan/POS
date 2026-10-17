package com.den.pos.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class OpenApiConfig {

    public static final String BEARER_SCHEME = "bearerAuth";

    @Bean
    public OpenAPI posOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("POS — Point of Sale API")
                        .version("v1")
                        .description("""
                                Retail management REST API (Spring Boot + PostgreSQL).

                                **Quick start**
                                1. Call `POST /auth/login` with the seeded admin account:
                                   `{"email":"admin@pos.local","password":"Admin@123"}`
                                2. Copy the `accessToken` from the response.
                                3. Click **Authorize** (top right), paste the token and authorize.
                                4. Every secured endpoint now sends `Authorization: Bearer <token>`.

                                **Notes**
                                - Access tokens live 15 minutes; use `POST /auth/refresh` to rotate.
                                - `POST /auth/register` creates the first user as `owner`, later users as `cashier`.
                                - All request payloads below are pre-filled with ready-to-run examples.
                                """))
                .addServersItem(new Server()
                        .url("/api/v1")
                        .description("Current host (context path included)"))
                .components(new Components()
                        .addSecuritySchemes(BEARER_SCHEME, new SecurityScheme()
                                .name(BEARER_SCHEME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Paste the accessToken returned by POST /auth/login")))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_SCHEME));
    }
}
