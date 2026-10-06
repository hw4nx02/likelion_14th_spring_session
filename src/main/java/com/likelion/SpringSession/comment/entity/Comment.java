package com.likelion.SpringSession.comment.entity;

import com.likelion.SpringSession.post.entity.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public Comment(Post post, String content) {
        this.post = post;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        post.getComments().add(this); // 반대쪽에 나를 추가
    }

    public void update(String content) {
        this.content = content;
    }
}
