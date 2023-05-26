package com.goodjob.domain.article.dto.response;

import com.goodjob.domain.comment.entity.Comment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ArticleResponseDto {
    private Long id;
    private LocalDateTime createDate;
    private LocalDateTime modifiedDate;
    private String title;
    private String content;
    private Long likeCount;
    private Long viewCount;
}
