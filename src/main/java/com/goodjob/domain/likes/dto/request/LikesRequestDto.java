package com.goodjob.domain.likes.dto.request;

import com.goodjob.domain.article.entity.Article;
import com.goodjob.domain.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikesRequestDto {
    private Long articleId;
    private Long commentId;
    private Long subCommentId;
}
