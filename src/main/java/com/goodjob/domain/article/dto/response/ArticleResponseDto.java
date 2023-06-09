package com.goodjob.domain.article.dto.response;

import com.goodjob.domain.comment.dto.response.CommentResponseDto;
import com.goodjob.domain.comment.entity.Comment;
import com.goodjob.domain.hashTag.dto.response.HashTagResponseDto;
import com.goodjob.domain.likes.dto.response.LikesResponseDto;
import com.goodjob.domain.likes.entity.Likes;
import com.goodjob.domain.likes.repository.LikesRepository;
import com.goodjob.domain.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
    private Member member;
    private List<HashTagResponseDto> hashTagList;

    public boolean isMemberLikesArticle(Member member) {
        for(LikesResponseDto likesResponseDto : likesList) {
            if(likesResponseDto.getMember().getId() == member.getId()) {
                return true;
            }
        }

        return false;
    }
}
