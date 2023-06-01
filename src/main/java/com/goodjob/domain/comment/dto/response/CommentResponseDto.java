package com.goodjob.domain.comment.dto.response;

import com.goodjob.domain.article.entity.Article;
import com.goodjob.domain.likes.entity.Likes;
import com.goodjob.domain.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CommentResponseDto {

    private Long id;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private Member member;
    private Article article;
    private String content;
    private List<Likes> likesList;
    private boolean isDeleted;
}
