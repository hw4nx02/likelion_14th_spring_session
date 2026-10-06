package com.likelion.SpringSession.post.repository;

import com.likelion.SpringSession.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
