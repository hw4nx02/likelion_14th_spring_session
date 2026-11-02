package com.likelion.SpringSession.auth.controller;

import com.likelion.SpringSession.auth.dto.*;
import com.likelion.SpringSession.auth.service.AuthService;
import com.likelion.SpringSession.post.dto.PostCreateRequest;
import com.likelion.SpringSession.post.dto.PostDetailResponse;
import com.likelion.SpringSession.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final PostService postService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public SignUpResponse signUp(@Valid @RequestBody SignUpRequest request) {
        return authService.signUp(request);
    }

    @PostMapping("/login")
    public TokenResponse login(
            @Valid @RequestBody LoginRequest request
    ) {
        return authService.login(request);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostDetailResponse createPost(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody PostCreateRequest request
    ) {
        System.out.println("요청한 사람: " + userId);

        return postService.createPost(userId, request);
    }

    @PostMapping("/reissue")
    public TokenResponse reissue(
            @Valid @RequestBody ReissueRequest request
    ) {
        return authService.reissue(request);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(
            @AuthenticationPrincipal Long userId
    ) {
        authService.logout(userId);
    }
}