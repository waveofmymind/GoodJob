package com.goodjob.domain.likes.service;

import com.goodjob.domain.article.entity.Article;
import com.goodjob.domain.article.service.ArticleService;
import com.goodjob.domain.comment.entity.Comment;
import com.goodjob.domain.comment.service.CommentService;
import com.goodjob.domain.likes.dto.request.LikesRequestDto;
import com.goodjob.domain.likes.entity.Likes;
import com.goodjob.domain.likes.repository.LikesRepository;
import com.goodjob.domain.member.entity.Member;
import com.goodjob.domain.subComment.entity.SubComment;
import com.goodjob.domain.subComment.service.SubCommentService;
import com.goodjob.global.base.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikesService {
    private final LikesRepository likesRepository;
    private final ArticleService articleService;
    private final CommentService commentService;
    private final SubCommentService subCommentService;

    public Long likeArticle(Member member, LikesRequestDto likesRequestDto) {
        RsData<Article> articleRsData = articleService.getArticle(likesRequestDto.getArticleId());

        Likes likes = Likes
                .builder()
                .member(member)
                .article(articleRsData.getData())
                .build();

        likesRepository.save(likes);

        return articleRsData.getData().getId();
    }

    public Long likeComment(Member member, LikesRequestDto likesRequestDto) {
        Comment comment = commentService.getComment(likesRequestDto.getCommentId());

        Likes likes = Likes
                .builder()
                .member(member)
                .comment(comment)
                .build();

        likesRepository.save(likes);

        return comment.getArticle().getId();
    }

    public Long likeSubComment(Member member, LikesRequestDto likesRequestDto) {
        SubComment subComment = subCommentService.getSubComment(likesRequestDto.getSubCommentId());

        Likes likes = Likes
                .builder()
                .member(member)
                .subComment(subComment)
                .build();

        likesRepository.save(likes);

        return subComment.getComment().getArticle().getId();
    }
}
