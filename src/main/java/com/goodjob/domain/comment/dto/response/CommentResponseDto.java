package com.goodjob.domain.comment.dto.response;

import com.goodjob.domain.article.dto.response.ArticleResponseDto;
import com.goodjob.domain.article.entity.Article;
import com.goodjob.domain.likes.dto.request.LikesRequestDto;
import com.goodjob.domain.likes.dto.response.LikesResponseDto;
import com.goodjob.domain.likes.entity.Likes;
import com.goodjob.domain.member.entity.Member;
import com.goodjob.domain.subComment.dto.request.SubCommentRequestDto;
import com.goodjob.domain.subComment.dto.response.SubCommentResponseDto;
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
    private String content;
    private List<LikesResponseDto> likesList;
    private boolean isDeleted;
    private List<SubCommentResponseDto> subCommentList;
}
