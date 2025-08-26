package com.example.insurance.mis.mvp.util;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long EXPIRATION_TIME = 1000 * 60 * 60;  // 1 hr

    private Logger log = LoggerFactory.getLogger(JwtUtil.class);

    public String generateToken(String email, String role) {
         return Jwts.builder()
            .setSubject(email)
            .claim("role", "ROLE_" + role.toUpperCase())
            .setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + EXPIRATION_TIME))
            .signWith(key)
            .compact();
    }

    public String validateTokenAndGetUsername(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return claimsJws.getBody().getSubject();

        } catch (JwtException e) {
            log.error("Exception :: ", e);

            return null; 
        }
    }

    public String getRoleFromToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return claimsJws.getBody().get("role", String.class);

        } catch (JwtException e) {
            log.error("Exception :: ", e);
            
            return null;
        }
    }

    public String getEmailFromToken(String token) {
        try {
            
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(key) 
                .build()
                .parseClaimsJws(token)
                .getBody();

            return claims.getSubject();

        } catch (Exception e) {
            log.error("Exception :: ", e);
            
            return null;
        }
        

}

}
