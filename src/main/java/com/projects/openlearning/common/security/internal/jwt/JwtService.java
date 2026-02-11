package com.projects.openlearning.common.security.internal.jwt;

import com.projects.openlearning.common.security.api.TokenIssuerPort;
import com.projects.openlearning.common.security.api.TokenParserPort;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService implements TokenIssuerPort, TokenParserPort {

    private final JwtProperties jwtProperties;
    private final StringRedisTemplate redisTemplate;
    private SecretKey secretKey;

    private static final String TOKEN_TYPE_CLAIM = "token_type";
    private static final String TYPE_ACCESS = "ACCESS";
    private static final String TYPE_REFRESH = "REFRESH";
    private static final String BLACKLIST_PREFIX = "blacklist:token:";

    /**
     * Initializes the secret key for JWT operations after the service is constructed.
     */
    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
        log.info("JWT secret key initialized successfully.");
    }

    @Override
    public String generateAccessToken(String subject, Map<String, Object> extraClaims) {
        Map<String, Object> claims = new HashMap<>(extraClaims);
        claims.put(TOKEN_TYPE_CLAIM, TYPE_ACCESS);

        long durationMillis = jwtProperties.getAccessExpirationSeconds() * 1000L;
        return buildToken(claims, subject, durationMillis);
    }

    @Override
    public String generateRefreshToken(String subject, Map<String, Object> extraClaims) {
        Map<String, Object> claims = new HashMap<>(extraClaims);
        claims.put(TOKEN_TYPE_CLAIM, TYPE_REFRESH);

        long durationMillis = jwtProperties.getRefreshExpirationSeconds() * 1000L;
        return buildToken(claims, subject, durationMillis);
    }

    @Override
    public boolean isAccessTokenValid(String token) {
        return isTokenValidAndType(token, TYPE_ACCESS);
    }

    /**
     * Validates the JWT token by checking its signature and ensuring it is of the expected type (ACCESS or REFRESH).
     * This method does not check for token expiration, allowing for separate handling of expired tokens if needed.
     */
    @Override
    public boolean isRefreshTokenValid(String token) {
        return !isTokenValidAndType(token, TYPE_REFRESH);
    }

    /**
     * Extracts the subject (usually username or email) from the JWT token.
     */
    @Override
    public String getSubjectFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the user ID from the JWT token by accessing the custom claim defined for user ID.
     */
    @Override
    public String getUserIdFromToken(String token) {
        return extractClaim(token, claims -> claims.get("userId", String.class));
    }

    /**
     * Extracts the JWT ID (JTI) from the token, which is a unique identifier for the token instance.
     */
    @Override
    public String getJtiFromToken(String token) {
        return extractClaim(token, Claims::getId);
    }

    /**
     * Extracts the expiration time from the JWT token and returns it as an Instant.
     */
    @Override
    public Instant getExpirationFromToken(String token) {
        return extractClaim(token, claims -> claims.getExpiration().toInstant());
    }

    /**
     * Gets the Time-To-Live (TTL) duration for refresh tokens based on configuration.
     */
    @Override
    public Duration getRefreshTokenTtl() {
        return Duration.ofSeconds(jwtProperties.getRefreshExpirationSeconds());
    }

    /**
     * Builds a JWT token with the given claims, subject, and expiration details.
     */
    public String buildToken(Map<String, Object> claims, String subject, long durationMillis) {
        return Jwts.builder()
                .subject(subject)
                .id(UUID.randomUUID().toString())
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + durationMillis))
                .signWith(secretKey)
                .compact();
    }

    /**
     * Validates the JWT signature and checks for malformed tokens. Does not check for expiration.
     */
    private boolean isTokenValidAndType(String token, String expectedType) {
        try {
            // 1. Validate signature and structure by attempting to parse claims
            Claims claims = getAllClaims(token);

            // 2. Validate token type claim to ensure it matches the expected type (ACCESS or REFRESH)
            String tokenType = claims.get(TOKEN_TYPE_CLAIM, String.class);
            if (!expectedType.equals(tokenType)) {
                log.warn("Invalid token type: {}", tokenType);
                return false;
            }

            // 3. Check if the token's JTI is in the revocation list (blacklist) in Redis
            String jti = claims.getId();
            if (isTokenRevoked(jti)) {
                log.warn("Token with JTI {} is revoked", jti);
                return false;
            }

            return true;
        } catch (JwtException | IllegalArgumentException | NullPointerException e) {
            log.warn("Invalid or malformed token: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Revokes a token by extracting its JTI and storing it in Redis with an expiration time that matches the token's remaining validity period.
     * This allows for efficient revocation without needing to store the entire token or check a database on every request.
     */
    @Override
    public void revokeToken(String token) {
        try {
            Claims claims = getAllClaims(token);
            String jti = claims.getId();
            Date expiration = claims.getExpiration();

            // Calculate TTL for Redis entry based on token expiration
            long nowMillis = System.currentTimeMillis();
            long expirationMillis = expiration.getTime();
            long ttlMillis = expirationMillis - nowMillis;

            if (ttlMillis > 0) {
                redisTemplate.opsForValue().set(
                        BLACKLIST_PREFIX + jti,
                        "revoked",
                        Duration.ofMillis(ttlMillis)
                );
                log.info("Token revoked: {}", jti);
            }
        } catch (Exception e) {
            log.error("Error revoking token: {}", e.getMessage());
        }
    }

    /**
     * Checks if a token with the given JTI is revoked by looking it up in Redis. If the JTI is found in the blacklist, it means the token has been revoked.
     */
    private boolean isTokenRevoked(String jti) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + jti));
    }

    /**
     * Extracts all claims from the JWT token. Throws exceptions if the token is invalid or malformed.
     */
    public Claims getAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Extracts a specific claim from the JWT token using a resolver function. The resolver defines how to extract
     * the desired claim from the Claims object.
     */
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = getAllClaims(token);
        return resolver.apply(claims);
    }

    /**
     * Creates an Authentication object from the JWT token. It extracts the username and role claims to build a
     * UsernamePasswordAuthenticationToken that can be used by Spring Security for authorization decisions.
     */
    public Authentication getAuthentication(String token) {
        Claims claims = getAllClaims(token);
        String username = claims.getSubject();
        String role = claims.get("role", String.class);

        // Convert role claim to Spring Security authority
        List<SimpleGrantedAuthority> authorities = (role != null)
                ? Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
                : Collections.emptyList();

        User principal = new User(username, "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }
}
