package com.transfer.backend.security;

import com.transfer.backend.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String username = jwtService.extractUserEmail(jwt);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Check if the access token exists in Redis
                String storedAccessToken = redisTemplate.opsForValue().get("token:" + username);

                if (storedAccessToken != null && storedAccessToken.equals(jwt)) {
                    // Extend Redis session timeout for active users
                    redisTemplate.expire("token:" + username, 30, TimeUnit.MINUTES);
                    redisTemplate.expire("refresh_token:" + username, 30, TimeUnit.MINUTES);

                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } else if (storedAccessToken == null) {
                    // Access token expired, check for valid refresh token
                    String refreshToken = redisTemplate.opsForValue().get("refresh_token:" + username);
                    if (refreshToken != null && jwtService.validateRefreshToken(refreshToken, userDetails)) {
                        // Generate new access token for active user
                        String newAccessToken = jwtService.refreshAccessToken(refreshToken);
                        redisTemplate.opsForValue().set("token:" + username, newAccessToken, 30, TimeUnit.MINUTES);
                        redisTemplate.expire("refresh_token:" + username, 30, TimeUnit.MINUTES);

                        // Authenticate user with new access token
                        authenticateUserWithNewToken(newAccessToken, userDetails, request);
                    } else {
                        // If no valid refresh token, user is inactive for more than 30 minutes
                        // Proceed without reauthentication; tokens have expired
                        SecurityContextHolder.clearContext();
                    }
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private void authenticateUserWithNewToken(String newAccessToken, UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}