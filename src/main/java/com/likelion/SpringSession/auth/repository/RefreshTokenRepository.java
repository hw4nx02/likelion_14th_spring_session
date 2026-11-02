package com.likelion.SpringSession.auth.repository;

import com.likelion.SpringSession.auth.entity.RefreshToken;
import com.likelion.SpringSession.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUser(User user);

    void deleteByUserId(Long userId);
}
