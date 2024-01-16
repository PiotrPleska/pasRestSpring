package com.example.passpringrest.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "D3btUsyQ3pjdWoAZfS90hih34oj8GgTgx63yuVwfOXt+g2IhiEt2D7+LFIW1Wh1sH57lKWYsTniJ83PeRrzkKrmEbChXgY7B0vG6ySw5gPVvfZY8k6/ndk1FjPAjvtoFGc82T9Y3yOhFnZRuKdsveQNXzQ0P8csT7flck9Hk1E3OyiGj7j+6x5l7hdi+u4Ga0jtxHp0kssd7gYZQyAyRMk5oD57PhCe8NBY/rFYCGKib97Ex4MxcMQ8w7zJztU/bs4XdkpdiTwF57EigL/8HJz0KP9oivRuJp3E+M/ZoUkmZAPHCciX9Or2i9SetxEkpvNQLEYSYY4cRZj1TshlYJy+wilLPvVbN2eKJolS3SXg=";

    public String extractLogin(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSecretKey() {
        byte[] secretKey = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(secretKey);
    }

    public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        var authorities = userDetails.getAuthorities();
        String role = authorities.stream().findFirst().get().getAuthority();
        System.out.println(authorities);
        return Jwts
                .builder()
                .setClaims(claims)
                .claim("role", role)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String login = extractLogin(token);
        return (login.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
