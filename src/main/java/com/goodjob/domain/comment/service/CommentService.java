package com.goodjob.domain.comment.service;

import com.goodjob.domain.article.dto.response.ArticleResponseDto;
import com.goodjob.domain.article.entity.Article;
import com.goodjob.domain.comment.dto.request.CommentRequestDto;
import com.goodjob.domain.comment.dto.response.CommentResponseDto;
import com.goodjob.domain.comment.entity.Comment;
import com.goodjob.domain.comment.mapper.CommentMapper;
import com.goodjob.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public void createComment(Article article, String content) {
        CommentRequestDto commentRequestDto = new CommentRequestDto(content);

        Comment comment = Comment
                .builder()
                .member(null)   //TODO : 추후 수정
                .article(article)
                .content(commentRequestDto.getContent())
                .likeCount(0L)
                .isDeleted(false)
                .build();

        commentRepository.save(comment);
    }

    public CommentResponseDto getCommentResponseDto(Long id) {
        return commentMapper.toDto(commentRepository.findById(id).orElseThrow());

    }

    public void updateComment(Comment comment, String content) {
        comment.setContent(content);
        commentRepository.save(comment);
    }

    public Comment getComment(Long id) {
        return commentRepository.findById(id).orElseThrow();
    }

    @Transactional
    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }
}
