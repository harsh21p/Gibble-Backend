package com.practicum.inventory_service.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtService {
    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";


    public static String extractSubject(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Map<String, String> extractAttributes(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        Map<String, Object> rawClaims = claims;
        Map<String, String> attributes = new HashMap<>();

        for (Map.Entry<String, Object> entry : rawClaims.entrySet()) {
            if (!isStandardClaim(entry.getKey())) {
                attributes.put(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }

        return attributes;
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private boolean isStandardClaim(String key) {
        return key.equals("sub") || key.equals("exp") || key.equals("iat");
    }
}
