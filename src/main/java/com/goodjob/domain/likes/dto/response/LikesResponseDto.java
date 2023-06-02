package com.goodjob.domain.likes.dto.response;

import com.goodjob.domain.article.dto.response.ArticleResponseDto;
import com.goodjob.domain.article.entity.Article;
import com.goodjob.domain.comment.dto.response.CommentResponseDto;
import com.goodjob.domain.comment.entity.Comment;
import com.goodjob.domain.member.entity.Member;
import com.goodjob.domain.subComment.dto.response.SubCommentResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class LikesResponseDto {
    private Long id;
    private Member member;
}
