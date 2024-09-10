package com.transfer.backendbankmasr.security;

import com.transfer.backendbankmasr.entity.UserEntity;
import com.transfer.backendbankmasr.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${app.jwt.secret}")
private String jwtSecret;

@Value("${app.jwt.expiration.ms}")
private int jwtExpirationMs;

@Value("${app.jwt.refreshExpiration.ms}")
    private int jwtRefreshExpirationMs;

    @Autowired
    private UserRepository userRepository  ;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private UserDetailsService userDetailsService;

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
        Optional<UserEntity> user = userRepository.findUserByEmail(userDetails.getUsername());

        Long userId = user.get().getUserId();
        extraClaims.put("userId", userId);  // Add the user ID to the token claims
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
    private String generateRefreshToken(UserDetails userDetails) {
        Optional<UserEntity> user = userRepository.findUserByEmail(userDetails.getUsername());
        Long userId = user.get().getUserId();
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", userId);
        String refreshToken = Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtRefreshExpirationMs))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
        redisTemplate.opsForValue().set("refresh_token:" + userDetails.getUsername(), refreshToken, jwtRefreshExpirationMs, TimeUnit.MILLISECONDS);
        return refreshToken;
    }
    public String refreshAccessToken(String refreshToken) throws SecurityException {
        // Verify the refresh token validity
        String username = extractUserEmail(refreshToken);
        if (username == null || !validateRefreshToken(refreshToken,userDetailsService.loadUserByUsername(username))) {
            throw new SecurityException("Invalid or expired refresh token.");
        }

        // Generate a new access token
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String newAccessToken = generateToken(new HashMap<>(), userDetails);

        // Optionally, generate a new refresh token and save both to Redis
        String newRefreshToken = generateRefreshToken(userDetails);
        redisTemplate.opsForValue().set("access_token:" + username, newAccessToken, 30, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set("refresh_token:" + username, newRefreshToken, jwtRefreshExpirationMs, TimeUnit.MILLISECONDS);

        return newAccessToken;
    }
    public boolean validateRefreshToken(String token, UserDetails userDetails) {
        final String username = userDetails.getUsername();
        String lastRefreshToken = redisTemplate.opsForValue().get("refresh_token:" + username);
        return token.equals(lastRefreshToken) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = getClaimsFromToken(token).getExpiration();
        return expiration.before(new Date());
    }
}
