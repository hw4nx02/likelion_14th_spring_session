package com.likelion.SpringSession.user.repository;

import com.likelion.SpringSession.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * 이메일로 회원정보 조회
     * @param email
     * @return
     */
    Optional<User> findByEmail(String email);

    /**
     * 이메일 존재 여부 확인
     * @param email
     * @return
     */
    boolean existsByEmail(String email);
}
