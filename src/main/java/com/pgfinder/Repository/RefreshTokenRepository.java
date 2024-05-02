package com.pgfinder.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pgfinder.Model.RefreshToken;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByToken(String token);
    // find by user id
    Optional<RefreshToken> findByUserId(Long userId);
    // delete by user id
    @Transactional
    void deleteByUserId(Long userId);
}