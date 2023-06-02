package com.goodjob.domain.article.dto.response;

import com.goodjob.domain.comment.dto.response.CommentResponseDto;
import com.goodjob.domain.comment.entity.Comment;
import com.goodjob.domain.likes.dto.response.LikesResponseDto;
import com.goodjob.domain.likes.entity.Likes;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ArticleResponseDto {
    private Long id;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String title;
    private String content;
    private List<LikesResponseDto> likesList;
    private Long viewCount;
    private List<CommentResponseDto> commentList;

    private Long commentsCount;
}
