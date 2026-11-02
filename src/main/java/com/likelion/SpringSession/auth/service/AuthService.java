package com.likelion.SpringSession.auth.service;

import com.likelion.SpringSession.auth.dto.*;
import com.likelion.SpringSession.auth.entity.RefreshToken;
import com.likelion.SpringSession.auth.exception.DuplicateEmailException;
import com.likelion.SpringSession.auth.exception.InvalidCredentialsException;
import com.likelion.SpringSession.auth.exception.InvalidRefreshTokenException;
import com.likelion.SpringSession.auth.repository.RefreshTokenRepository;
import com.likelion.SpringSession.security.JwtProvider;
import com.likelion.SpringSession.user.entity.User;
import com.likelion.SpringSession.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public SignUpResponse signUp(SignUpRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException(request.getEmail());
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = userRepository.save(
                new User(request.getEmail(), encodedPassword, request.getNickname()));

        return new SignUpResponse(user.getId(), user.getEmail(), user.getNickname());
    }

    @Transactional
    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        return issueTokens(user);
    }

    @Transactional
    public TokenResponse reissue(ReissueRequest request) {

        String refreshToken = request.getRefreshToken();

        // (1) 서명과 만료를 검증한다
        if (!jwtProvider.validateRefreshToken(refreshToken)) {
            throw new InvalidRefreshTokenException();
        }

        // (2) 서버가 기억하고 있는 것과 대조한다
        RefreshToken saved = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(InvalidRefreshTokenException::new);

        return issueTokens(saved.getUser());
    }

    @Transactional
    public void logout(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }

    private TokenResponse issueTokens(User user) {

        String accessToken  = jwtProvider.createAccessToken(user.getId());
        String refreshToken = jwtProvider.createRefreshToken(user.getId());
        LocalDateTime expiresAt = jwtProvider.getExpiresAt(refreshToken);

        RefreshToken saved = refreshTokenRepository.findByUser(user).orElse(null);

        if (saved == null) {
            refreshTokenRepository.save(new RefreshToken(user, refreshToken, expiresAt));
        } else {
            saved.update(refreshToken, expiresAt);       // 변경 감지 → UPDATE
        }

        return new TokenResponse(accessToken, refreshToken);
    }
}
