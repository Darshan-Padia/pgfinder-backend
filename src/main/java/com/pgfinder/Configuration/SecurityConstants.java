package com.pgfinder.Configuration;

import javax.crypto.SecretKey;

import org.springframework.boot.autoconfigure.ssl.SslBundleProperties.Key;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class SecurityConstants {
    public static final long JWT_EXPIRATION = 70000; 
    public static final String JWT_SECRET = "secret";
    public static final SecretKey JWT_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    
}
