package com.likelion.SpringSession.post.service;

import com.likelion.SpringSession.post.dto.PostCreateRequest;
import com.likelion.SpringSession.post.dto.PostDetailResponse;
import com.likelion.SpringSession.post.dto.PostSummaryResponse;
import com.likelion.SpringSession.post.dto.PostUpdateRequest;
import com.likelion.SpringSession.post.entity.Post;
import com.likelion.SpringSession.post.exception.PostAccessDeniedException;
import com.likelion.SpringSession.post.repository.PostRepository;
import com.likelion.SpringSession.user.entity.User;
import com.likelion.SpringSession.user.exception.UserNotFoundException;
import com.likelion.SpringSession.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor // final 필드 필요한 생성자 주입
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostDetailResponse getPost(Long postId) {
        Post post = findPostById(postId);
        return toDetailResponse(post);
    }

    public List<PostSummaryResponse> getPostSummaries() {
        List<Post> posts = postRepository.findAll();
        List<PostSummaryResponse> responses = new ArrayList<>();

        for (Post post : posts) {
            PostSummaryResponse response = new PostSummaryResponse(
                    post.getId(),
                    post.getTitle(),
                    post.getAuthor().getNickname(),
                    post.getCreateAt()
            );
            responses.add(response);
        }

        return responses;
    }

    @Transactional
    public PostDetailResponse createPost(
            Long userId,
            PostCreateRequest request
    ) {
        User author = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Post post = new Post(
                author,
                request.getTitle(),
                request.getContent()
        );

        Post savedPost = postRepository.save(post);

        return toDetailResponse(savedPost);
    }

    @Transactional
    public PostDetailResponse updatePost(
            Long userId,
            Long postId,
            PostUpdateRequest request
    ) {
        Post post = findPostById(postId);

        if (!post.isAuthor(userId)) {
            throw new PostAccessDeniedException();
        }

        post.update(
                request.getTitle(),
                request.getContent()
        );

        return toDetailResponse(post);
    }

    @Transactional
    public void deletePost(
            Long userId,
            Long postId
    ) {
        Post post = findPostById(postId);

        if (!post.isAuthor(userId)) {
            throw new PostAccessDeniedException();
        }

        postRepository.delete(post);
    }

    private PostDetailResponse toDetailResponse(Post post) {
        return new PostDetailResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor().getNickname(),
                post.getCreateAt()
        );
    }
    // 게시글 ID 조회
    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(); // 해당하는 ID 없을 수도 있어서
    }
}
