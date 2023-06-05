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

    public RsData likeArticle(Member member, LikesRequestDto likesRequestDto) {
        RsData<Article> articleRsData = articleService.getArticle(likesRequestDto.getArticleId());

        if(articleRsData.isFail()) {
            return articleRsData;
        }

        Likes likes = Likes
                .builder()
                .member(member)
                .article(articleRsData.getData())
                .build();

        likesRepository.save(likes);

        return RsData.of("S-1", "해당 게시글을 좋아합니다.", likes);
    }

    public RsData likeComment(Member member, LikesRequestDto likesRequestDto) {
        RsData<Comment> commentRsData = commentService.getComment(likesRequestDto.getCommentId());

        if(commentRsData.isFail()) {
            return commentRsData;
        }

        Comment comment = commentRsData.getData();

        Likes likes = Likes
                .builder()
                .member(member)
                .comment(comment)
                .build();

        likesRepository.save(likes);

        return RsData.of("S-1", "해당 댓글을 좋아합니다.", likes);
    }

    public RsData likeSubComment(Member member, LikesRequestDto likesRequestDto) {
        RsData<SubComment> subCommentRsData = subCommentService.getSubComment(likesRequestDto.getSubCommentId());

        if(subCommentRsData.isFail()) {
            return subCommentRsData;
        }

        Likes likes = Likes
                .builder()
                .member(member)
                .subComment(subCommentRsData.getData())
                .build();

        likesRepository.save(likes);

        return RsData.of("S-1", "해당 답글을 좋아합니다.", likes);
    }
}
