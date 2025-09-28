package com.practicum.authentication_service.service;

import com.practicum.authentication_service.Model.User;
import com.practicum.authentication_service.repository.EnrollmentRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class JwtService {
    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    EnrollmentRepository enrollmentRepository;

    public String getId(String username) throws Exception {
        try {
            Optional<User> user = enrollmentRepository.findByEmail(username);
            if (user.isPresent()) {
                return user.get().getId();
            } else {
                throw new Exception("User not found");
            }
        } catch (Exception exception){
            throw exception;
        }
    }

    public boolean validateToken(String token) {

        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token);
            Date expiration = claimsJws.getBody().getExpiration();
            return expiration.after(new Date()); // Check expiration
        } catch (ExpiredJwtException ex) {
            System.out.println("Token expired");
        } catch (UnsupportedJwtException ex) {
            System.out.println("Unsupported JWT");
        } catch (MalformedJwtException ex) {
            System.out.println("Malformed JWT");
        } catch (SignatureException ex) {
            System.out.println("Invalid signature");
        } catch (IllegalArgumentException ex) {
            System.out.println("Empty or null token");
        }

        return false;
    }


    public String createToken(Map<String, String> claims, String userName) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

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

    private boolean isStandardClaim(String key) {
        return key.equals("sub") || key.equals("exp") || key.equals("iat");
    }
}
