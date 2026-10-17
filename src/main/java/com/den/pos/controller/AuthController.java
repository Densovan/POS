package com.den.pos.controller;

import com.den.pos.service.AuthService;
import com.den.pos.web.dto.LoginRequest;
import com.den.pos.web.dto.RefreshRequest;
import com.den.pos.web.dto.RegisterRequest;
import com.den.pos.web.dto.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "Register, login, refresh and logout. Seeded admin: admin@pos.local / Admin@123")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Log in and receive tokens",
            description = "Access token lives 15 minutes, refresh token 7 days")
    @SecurityRequirements
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @Operation(summary = "Register a new user",
            description = "The first registered user becomes owner, later users become cashier")
    @SecurityRequirements
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public TokenResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @Operation(summary = "Rotate tokens",
            description = "Exchanges a valid refresh token for a new pair; the old refresh token is invalidated")
    @SecurityRequirements
    @PostMapping(value = "/refresh", consumes = MediaType.APPLICATION_JSON_VALUE)
    public TokenResponse refresh(@Valid @RequestBody RefreshRequest request) {
        return authService.refresh(request);
    }

    @Operation(summary = "Log out", description = "Revokes the stored refresh token")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(Authentication authentication) {
        authService.logout(authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
