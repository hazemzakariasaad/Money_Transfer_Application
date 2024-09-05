package com.transfer.backendbankmasr.security;

import org.springframework.beans.factory.annotation.Value;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${app.jwt.secret}")
private String jwtSecret;

@Value("${app.jwt.expiration.ms}")
private int jwtExpirationMs;


    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public String extractUserEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public void invalidateToken(String token) {
        // Decode the token to extract the username
        String username = extractUserEmail(token);
        // Remove the token from Redis
        redisTemplate.delete("token:" + username);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = getClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    // User can only enter from one device
    // Session be only for 30 min
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        String token = Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs)) // 30 min
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
        redisTemplate.opsForValue().set("token:" + userDetails.getUsername(), token, 30, TimeUnit.MINUTES);
        return token;
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = userDetails.getUsername();
        // Retrieve the last token from Redis
        String lastToken = redisTemplate.opsForValue().get("token:" + username);
        return token.equals(lastToken) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = getClaimsFromToken(token).getExpiration();
        return expiration.before(new Date());
    }
}
