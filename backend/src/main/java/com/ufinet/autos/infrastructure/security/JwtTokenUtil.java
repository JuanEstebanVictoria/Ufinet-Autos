package com.ufinet.autos.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Utility class for generating, parsing, and validating JSON Web Tokens (JWT).
 */
@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secret;

    private Key key;
    private final long JWT_TOKEN_VALIDITY = 5 * 60 * 60; // 5 hours

    @PostConstruct
    public void init() {
        // Derive a stable HMAC-SHA256 key from the configured secret string
        byte[] keyBytes = Base64.getEncoder().encode(secret.getBytes());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extracts the username (subject) from a JWT token.
     *
     * @param token the JWT string
     * @return the username stored in the token
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date from a JWT token.
     *
     * @param token the JWT string
     * @return the token's expiration date
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from a JWT token using a resolver function.
     *
     * @param token          the JWT string
     * @param claimsResolver function to apply to the token's claims
     * @param <T>            the expected return type of the claim
     * @return the extracted claim value
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Parses the JWT and returns all claims inside it.
     *
     * @param token the JWT string
     * @return all claims in the token
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    /**
     * Checks whether a JWT token is expired.
     *
     * @param token the JWT string
     * @return true if the token has expired, false otherwise
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * Generates a signed JWT token for the given user.
     * Includes the user's database ID as a custom {@code userId} claim.
     *
     * @param userDetails the authenticated user
     * @return the signed JWT string
     */
    public String generateToken(CustomUserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userDetails.getId()); // embed the DB id as a custom claim
        return doGenerateToken(claims, userDetails.getUsername());
    }

    /**
     * Extracts the user's database ID from the {@code userId} custom claim.
     *
     * @param token the JWT string
     * @return the user's primary key
     */
    public Long getUserIdFromToken(String token) {
        // Numbers in JWT claims are stored as Integer when small; cast via Number for safety.
        return getClaimFromToken(token, claims ->
                ((Number) claims.get("userId")).longValue());
    }

    /**
     * Builds and signs the JWT token with the given claims and subject.
     *
     * @param claims  additional claims to embed in the token
     * @param subject the subject (username) of the token
     * @return the signed JWT string
     */
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(key)
                .compact();
    }

    /**
     * Validates that the token belongs to the given user and has not expired.
     *
     * @param token       the JWT string
     * @param userDetails the user to validate against
     * @return true if valid, false otherwise
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
