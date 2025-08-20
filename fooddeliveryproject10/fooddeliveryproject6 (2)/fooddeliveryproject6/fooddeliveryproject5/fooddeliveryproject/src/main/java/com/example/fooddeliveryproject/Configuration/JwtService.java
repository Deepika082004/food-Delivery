package com.example.fooddeliveryproject.Configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {
    // Use a 256-bit (32+ char) key
    private static final String SECRET_KEY_STRING = "9W7cVK3B59dOk0SdPyqgEGrv7EUz7WhA";
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes());

    private static final long ACCESS_EXPIRY = 60 * 60;        // 1 hour
   // private static final long REFRESH_EXPIRY = 7 * 24 * 60 * 60; // 7 days

    // Generate Access Token
    public String generateAccessToken(UserDetails user) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(user.getUsername())                  // subject = username
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(ACCESS_EXPIRY)))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    // Generate Refresh Token
//    public String generateRefreshToken(UserDetails user) {
//        Instant now = Instant.now();
//        return Jwts.builder()
//                .subject(user.getUsername())                  // subject = username
//                .issuedAt(Date.from(now))
//                .expiration(Date.from(now.plusSeconds(REFRESH_EXPIRY)))
//                .signWith(key, Jwts.SIG.HS256)
//                .compact();
//    }

    public String extractUsername(String token) {
        return parseAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, UserDetails user) {
        return extractUsername(token).equals(user.getUsername()) && !isExpired(token);
    }

    private boolean isExpired(String token) {
        return parseAllClaims(token).getExpiration().before(new Date());
    }

    private Claims parseAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
