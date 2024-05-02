package com.pgfinder.Configuration;

import java.util.Date;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import com.pgfinder.Configuration.SecurityConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component // access in other parts of application
public class JWTGenerator {

    public String generateJWTToken(Authentication authentication) {

        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION); // 

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SecurityConstants.JWT_KEY).compact();
        return token;

    }

    public String getUsernameFromJWTToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SecurityConstants.JWT_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SecurityConstants.JWT_KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            System.out.println("Invalid JWT token");
            throw new AuthenticationCredentialsNotFoundException("Invalid JWT token");
        }
        // return false;
    }

}
