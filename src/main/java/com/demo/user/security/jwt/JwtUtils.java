package com.demo.user.security.jwt;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtils {

        private final String SECRET = "veryverysecretkeyveryverysecretkey";

        private final long ACCESS_TOKEN_EXPIRATION = TimeUnit.MINUTES.toMillis(45);

        private final long REFRESH_TOKEN_EXPIRATION = TimeUnit.DAYS.toMillis(30);

        public String generateAccessToken(String userID, String roleId) {

                return Jwts.builder()
                                .setSubject(userID)
                                .claim("type", "access")
                                .claim("roleId", roleId)
                                .setIssuedAt(new Date())
                                .setExpiration(new Date(
                                                System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                                .compact();
        }

        public String generateRefreshToken(String userID, String roleId) {

                return Jwts.builder()
                                .setSubject(userID)
                                .claim("type", "refresh")
                                .claim("roleId", roleId)
                                .setIssuedAt(new Date())
                                .setExpiration(new Date(
                                                System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                                .compact();
        }

        public Claims getClaims(String token) {
                return Jwts.parserBuilder().setSigningKey(SECRET.getBytes()).build().parseClaimsJws(token).getBody();
        }

        public boolean validateToken(String token, String expectedType) {
                Claims claim = getClaims(token);
                return expectedType.equals(claim.get("type", String.class))
                                && !claim.getExpiration().before(new Date());
        }

        public String resolveToken(HttpServletRequest request) {
                String header = request.getHeader("Authorization");
                if (header != null && header.startsWith("Bearer ")) {
                        return header.substring(7);
                }
                throw new RuntimeException("No JWT token provided");
        }

        public UUID getUserIdFromToken(String token) {
                Claims claim = getClaims(token);
                return UUID.fromString(claim.get("userID", String.class));
        }

}
