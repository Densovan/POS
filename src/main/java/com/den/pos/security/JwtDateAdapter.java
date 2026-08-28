package com.den.pos.security;

import java.time.Instant;
import java.util.Date;

/**
 * Single isolated point of conversion between {@link Instant} and the legacy
 * {@link Date} type required by the JJWT claim API ({@code issuedAt}/{@code expiration}).
 * Keeping all legacy date usage here avoids spreading {@link java.util.Date}
 * through the application domain.
 */
final class JwtDateAdapter {

    private JwtDateAdapter() {
    }

    static Date toDate(Instant instant) {
        return Date.from(instant);
    }
}
