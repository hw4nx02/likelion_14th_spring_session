package com.likelion.SpringSession.post.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter // Getter 추가
@RequiredArgsConstructor // final 필드에 대한 생성자 추가
public class PostSummaryResponse {
    private final Long id;
    private final String title;
    private final LocalDateTime createdAt;
}
