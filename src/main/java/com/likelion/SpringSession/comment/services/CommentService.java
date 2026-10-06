package com.likelion.SpringSession.comment.services;

import com.likelion.SpringSession.comment.dto.CommentCreateRequest;
import com.likelion.SpringSession.comment.dto.CommentResponse;
import com.likelion.SpringSession.comment.dto.CommentUpdateRequest;
import com.likelion.SpringSession.comment.entity.Comment;
import com.likelion.SpringSession.comment.repository.CommentRepository;
import com.likelion.SpringSession.post.entity.Post;
import com.likelion.SpringSession.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    /**
     * Comment 작성
     * @param postId
     * @param request
     * @return
     */
    @Transactional
    public CommentResponse addComment(
            Long postId,
            CommentCreateRequest request
    ) {
        Post post = postRepository.findById(postId)
                .orElseThrow();

        Comment saved = commentRepository.save(new Comment(post, request.getContent()));
        return new CommentResponse(saved);
    }

    /**
     * Comment 목록 조회
     * @param postId
     * @return
     */
    public List<CommentResponse> getComments(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow();

        List<CommentResponse> responses = new ArrayList<>();
        for (Comment comment : commentRepository.findAllByPost(post)) {
            responses.add(new CommentResponse(comment));
        }

        return responses;
    }

    /**
     * Comment 수정
     * @param postId
     * @param commentId
     * @param request
     * @return
     */
    @Transactional
    public CommentResponse editComment(
            Long postId,
            Long commentId,
            CommentUpdateRequest request
    ) throws Exception {
        if (!isValidComment(postId, commentId)) {
            throw new Exception("유효하지 않은 접근입니다.");
        }
        Comment comment = findCommentById(commentId);

        comment.update(
                request.getContent()
        );

        return new CommentResponse(comment);
    }

    @Transactional
    public void deleteComment(
            Long postId,
            Long commentId
    ) throws Exception {
        if (!isValidComment(postId, commentId)) {
            throw new Exception("유효하지 않은 접근입니다.");
        }
        Comment comment = findCommentById(commentId);
        commentRepository.delete(comment);
    }

    /**
     * Comment 단건 조회
     * @param commentId
     * @return
     */
    private Comment findCommentById(
            Long commentId
    ) {
        return commentRepository.findById(commentId)
                .orElseThrow();
    }

    /**
     * 존재하는 게시글의 댓글인지 확인
     * @param postId
     * @param commentId
     * @return
     */
    private boolean isValidComment(
            Long postId,
            Long commentId
    ) {
        Post post = postRepository.findById(postId)
                .orElseThrow();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow();

        return comment.getPost().equals(post);
    }
}
