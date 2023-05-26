package com.goodjob.domain.comment.service;

import com.goodjob.domain.article.dto.response.ArticleResponseDto;
import com.goodjob.domain.article.entity.Article;
import com.goodjob.domain.comment.dto.request.CommentRequestDto;
import com.goodjob.domain.comment.entity.Comment;
import com.goodjob.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public void create(Article article, String content) {
        CommentRequestDto commentRequestDto = new CommentRequestDto(content);

        Comment comment = Comment
                .builder()
                .createDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .member(null)   //TODO : 추후 수정
                .article(article)
                .content(commentRequestDto.getContent())
                .likeCount(0L)
                .isDeleted(false)
                .build();

        commentRepository.save(comment);
    }
}
