package com.den.pos.service.impl;

import com.den.pos.entity.auth.User;
import com.den.pos.exception.ConflictException;
import com.den.pos.exception.UnauthorizedException;
import com.den.pos.repository.auth.RoleRepository;
import com.den.pos.repository.auth.UserRepository;
import com.den.pos.repository.auth.UserRoleRepository;
import com.den.pos.security.JwtService;
import com.den.pos.service.AuthService;
import com.den.pos.web.dto.LoginRequest;
import com.den.pos.web.dto.RefreshRequest;
import com.den.pos.web.dto.RegisterRequest;
import com.den.pos.web.dto.TokenResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           UserRoleRepository userRoleRepository,
                           JwtService jwtService,
                           AuthenticationManager authenticationManager,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public TokenResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        } catch (AuthenticationException ex) {
            throw new UnauthorizedException("Invalid email or password");
        }

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        return issueTokens(user);
    }

    @Override
    @Transactional
    public TokenResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new ConflictException("Email already registered");
        }

        User user = new User();
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setIsActive(true);
        user.setLoginAttempts(0);
        user = userRepository.save(user);

        assignBootstrapRole(user.getId());

        return issueTokens(user);
    }

    @Override
    @Transactional
    public TokenResponse refresh(RefreshRequest request) {
        Claims claims;
        try {
            claims = jwtService.parse(request.refreshToken());
        } catch (JwtException ex) {
            throw new UnauthorizedException("Invalid refresh token");
        }
        if (!jwtService.isRefreshToken(claims)) {
            throw new UnauthorizedException("Invalid refresh token");
        }

        User user = userRepository.findByEmail(claims.getSubject())
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

        if (user.getRefreshToken() == null
                || !MessageDigest.isEqual(
                        user.getRefreshToken().getBytes(StandardCharsets.UTF_8),
                        request.refreshToken().getBytes(StandardCharsets.UTF_8))) {
            throw new UnauthorizedException("Refresh token revoked");
        }

        return issueTokens(user);
    }

    @Override
    @Transactional
    public void logout(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));
        userRepository.updateRefreshToken(user.getId(), null);
    }

    private void assignBootstrapRole(UUID userId) {
        String roleName = userRepository.count() <= 1 ? "owner" : "cashier";
        roleRepository.findByName(roleName)
                .ifPresent(role -> userRoleRepository.grant(userId, role.getId()));
    }

    private TokenResponse issueTokens(User user) {
        String accessToken = jwtService.generateAccessToken(user.getId(), user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getId(), user.getEmail());

        userRepository.updateRefreshToken(user.getId(), refreshToken);
        userRepository.recordSuccessfulLogin(user.getId(), LocalDateTime.now());

        return TokenResponse.of(accessToken, refreshToken, jwtService.getAccessTokenExpiration());
    }
}
