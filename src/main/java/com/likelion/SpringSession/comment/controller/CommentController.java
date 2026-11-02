package com.likelion.SpringSession.comment.controller;

import com.likelion.SpringSession.comment.dto.CommentCreateRequest;
import com.likelion.SpringSession.comment.dto.CommentResponse;
import com.likelion.SpringSession.comment.dto.CommentUpdateRequest;
import com.likelion.SpringSession.comment.services.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    /**
     * Comment 생성
     * @param postId
     * @param request
     * @return
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse create(
            @AuthenticationPrincipal Long userId,
            @PathVariable("postId") Long postId,
            @Valid @RequestBody CommentCreateRequest request
    ) {
        return commentService.addComment(
                userId,
                postId,
                request
        );
    }

    /**
     * Comment 목록 조회
     * @param postId
     * @return
     */
    @GetMapping
    public List<CommentResponse> list(
            @PathVariable("postId") Long postId
    ) {
        return commentService.getComments(postId);
    }

    @PutMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentResponse editComment(
            @AuthenticationPrincipal Long userId,
            @PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId,
            @Valid @RequestBody CommentUpdateRequest request
    ) throws Exception {
        return commentService.editComment(userId, postId, commentId, request);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(
            @AuthenticationPrincipal Long userId,
            @PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId
    ) throws Exception {
        commentService.deleteComment(userId, postId, commentId);
    }
}
