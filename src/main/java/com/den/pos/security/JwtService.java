package com.den.pos.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;

@Service
public class JwtService {

    private static final String CLAIM_UID = "uid";
    private static final String CLAIM_TYP = "typ";
    private static final String TYP_ACCESS = "access";
    private static final String TYP_REFRESH = "refresh";

    private final SecretKey key;
    private final JwtProperties props;

    public JwtService(JwtProperties props) {
        byte[] secretBytes = props.secret().getBytes(StandardCharsets.UTF_8);
        if (secretBytes.length < 32) {
            throw new IllegalStateException(
                "JWT_SECRET must be at least 256 bits (32 bytes). Generate one with: openssl rand -base64 64");
        }
        this.key = Keys.hmacShaKeyFor(secretBytes);
        this.props = props;
    }

    public String generateAccessToken(UUID userId, String email) {
        return buildToken(userId, email, TYP_ACCESS, props.expiration());
    }

    public String generateRefreshToken(UUID userId, String email) {
        return buildToken(userId, email, TYP_REFRESH, props.refreshTokenExpiration());
    }

    private String buildToken(UUID userId, String email, String typ, long ttlMs) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(email)
                .claim(CLAIM_UID, userId.toString())
                .claim(CLAIM_TYP, typ)
                .issuedAt(JwtDateAdapter.toDate(now))
                .expiration(JwtDateAdapter.toDate(now.plusMillis(ttlMs)))
                .signWith(key)
                .compact();
    }

    public Claims parse(String token) throws JwtException {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isRefreshToken(Claims claims) {
        return TYP_REFRESH.equals(claims.get(CLAIM_TYP, String.class));
    }

    public UUID extractUserId(Claims claims) {
        return UUID.fromString(claims.get(CLAIM_UID, String.class));
    }

    public long getAccessTokenExpiration() {
        return props.expiration();
    }
}
