package com.likelion.SpringSession.post.controller;

import com.likelion.SpringSession.post.dto.PostCreateRequest;
import com.likelion.SpringSession.post.dto.PostDetailResponse;
import com.likelion.SpringSession.post.dto.PostSummaryResponse;
import com.likelion.SpringSession.post.dto.PostUpdateRequest;
import com.likelion.SpringSession.post.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts") // 공통 URL 지정
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/{postId}")
    public PostDetailResponse getPost(
            @PathVariable("postId") Long postId
    ) {
        return postService.getPost(postId);
    }

    @GetMapping
    public List<PostSummaryResponse> getPosts() {
        return postService.getPostSummaries();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // 201
    public PostDetailResponse createPost( // @Valid를 통해서 입력 인자 검증 -> 틀리면 400
            @Valid @RequestBody PostCreateRequest request
    ) {
        return postService.createPost(request);
    }

    @PutMapping("/{postId}")
    public PostDetailResponse updatePost(
            @PathVariable("postId") Long postId,
            @Valid @RequestBody PostUpdateRequest request
    ) {
        return postService.updatePost(postId, request);
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(
            @PathVariable("postId") Long postId
    ) {
        postService.deletePost(postId);
    }
}
