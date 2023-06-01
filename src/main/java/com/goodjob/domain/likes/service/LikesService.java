package com.goodjob.domain.likes.service;

import com.goodjob.domain.article.dto.request.ArticleRequestDto;
import com.goodjob.domain.article.dto.response.ArticleResponseDto;
import com.goodjob.domain.article.entity.Article;
import com.goodjob.domain.article.mapper.ArticleMapper;
import com.goodjob.domain.article.repository.ArticleRepository;
import com.goodjob.domain.article.service.ArticleService;
import com.goodjob.domain.comment.entity.Comment;
import com.goodjob.domain.comment.service.CommentService;
import com.goodjob.domain.likes.dto.request.LikesRequestDto;
import com.goodjob.domain.likes.entity.Likes;
import com.goodjob.domain.likes.repository.LikesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikesService {
    private final LikesRepository likesRepository;
    private final ArticleService articleService;
    private final CommentService commentService;

    public void likeArticle(LikesRequestDto likesRequestDto) {
        Article article = articleService.getArticle(likesRequestDto.getArticleId());

        Likes likes = Likes
                .builder()
                .article(article)
                .build();

        likesRepository.save(likes);
    }

    public void likeComment(LikesRequestDto likesRequestDto) {
        Comment comment = commentService.getComment(likesRequestDto.getCommentId());

        Likes likes = Likes
                .builder()
                .comment(comment)
                .build();

        likesRepository.save(likes);
    }
}
