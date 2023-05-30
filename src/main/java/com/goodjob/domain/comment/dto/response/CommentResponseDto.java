package com.goodjob.domain.comment.dto.response;

import com.goodjob.domain.article.entity.Article;
import com.goodjob.domain.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponseDto {

    private Long id;
    private LocalDateTime createDate;
    private LocalDateTime modifiedDate;
    private Member member;
    private Article article;
    private String content;
    private Long likeCount;
    private boolean isDeleted;
}
