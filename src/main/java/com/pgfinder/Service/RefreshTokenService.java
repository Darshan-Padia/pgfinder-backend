package com.pgfinder.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pgfinder.Model.RefreshToken;
import com.pgfinder.Repository.RefreshTokenRepository;
import com.pgfinder.Repository.UserRepository;
@Service
public class RefreshTokenService {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserRepository userRepository;

    public RefreshToken createRefreshToken(String username){
        RefreshToken refreshToken = RefreshToken.builder()
                .user(userRepository.findByEmail(username))
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(6000000)) // set expiry of refresh token to 10 minutes - you can configure it application.properties file 
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public void deleteByUserId(Long userID){
        refreshTokenRepository.deleteByUserId(userID);
    }
   
    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public Optional<RefreshToken> findByUserId(Long userId){
        return refreshTokenRepository.findByUserId(userId);
    }
    

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token is expired. Please make a new login..!");
        }
        return token;
    }
}