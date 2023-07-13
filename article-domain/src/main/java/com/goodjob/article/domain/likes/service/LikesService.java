package com.goodjob.article.domain.likes.service;

import com.goodjob.article.domain.article.entity.Article;
import com.goodjob.article.domain.article.service.ArticleService;
import com.goodjob.article.domain.comment.entity.Comment;
import com.goodjob.article.domain.comment.service.CommentService;
import com.goodjob.article.domain.likes.dto.request.LikesRequestDto;

import com.goodjob.article.domain.likes.entity.Likes;
import com.goodjob.article.domain.likes.repository.LikesRepository;

import com.goodjob.article.domain.subComment.entity.SubComment;
import com.goodjob.article.domain.subComment.service.SubCommentService;
import com.goodjob.common.rsData.RsData;
import com.goodjob.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

        Article article = articleRsData.getData();

        Optional<Likes> likesOp = likesRepository.findByArticleAndMember(article, member);

        if(!likesOp.isEmpty()) {
            return RsData.of("F-1", "이미 좋아요 표시를 한 게시글입니다.");
        }

        if(article.getMember().getId() == member.getId()) {
            return RsData.of("F-2", "본인의 게시글에는 좋아요 표시를 할 수 없습니다.");
        }

        Likes likes = Likes
                .builder()
                .member(member)
                .article(article)
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

        Optional<Likes> likesOp = likesRepository.findByCommentAndMember(comment, member);

        if(!likesOp.isEmpty()) {
            return RsData.of("F-1", "이미 좋아요 표시를 한 댓글입니다.");
        }

        if(comment.getMember().getId() == member.getId()) {
            return RsData.of("F-2", "본인의 댓글에는 좋아요 표시를 할 수 없습니다.");
        }

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

        SubComment subComment = subCommentRsData.getData();

        Optional<Likes> likesOp = likesRepository.findBySubCommentAndMember(subComment, member);

        if(!likesOp.isEmpty()) {
            return RsData.of("F-1", "이미 좋아요 표시를 한 답글입니다.");
        }

        if(subComment.getMember().getId() == member.getId()) {
            return RsData.of("F-2", "본인의 답글에는 좋아요 표시를 할 수 없습니다.");
        }

        Likes likes = Likes
                .builder()
                .member(member)
                .subComment(subComment)
                .build();

        likesRepository.save(likes);

        return RsData.of("S-1", "해당 답글을 좋아합니다.", likes);
    }
}
