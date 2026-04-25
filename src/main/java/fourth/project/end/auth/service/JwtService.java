package fourth.project.end.auth.service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import fourth.project.end.auth.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final JwtProperties jwtProperties;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public String generateToken(UserDetails userDetails) {
        Instant now = Instant.now();
        return Jwts.builder()
            .claims(Map.of())
            .subject(userDetails.getUsername())
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plusSeconds(jwtProperties.getExpirationSeconds())))
            .signWith(getSigningKey())
            .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username != null
            && username.equals(userDetails.getUsername())
            && !isTokenExpired(token);
    }

    public long getExpirationSeconds() {
        return jwtProperties.getExpirationSeconds();
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
        return claimsResolver.apply(claims);
    }

    private SecretKey getSigningKey() {
        String secret = jwtProperties.getSecret();
        Assert.isTrue(StringUtils.hasText(secret), "APP_JWT_SECRET must be configured");
        byte[] keyBytes = isBase64(secret)
            ? Decoders.BASE64.decode(secret)
            : secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private boolean isBase64(String value) {
        return value.matches("^[A-Za-z0-9+/=\\r\\n]+$");
    }
}
