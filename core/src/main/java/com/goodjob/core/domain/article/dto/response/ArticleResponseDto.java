package com.goodjob.core.domain.article.dto.response;

import com.goodjob.core.domain.comment.dto.response.CommentResponseDto;
import com.goodjob.core.domain.hashTag.dto.response.HashTagResponseDto;
import com.goodjob.core.domain.likes.dto.response.LikesResponseDto;
import com.goodjob.core.domain.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
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
    private Map<String, Object> extra = new LinkedHashMap<>();

    public boolean isMemberLikesArticle(Member member) {
        for(LikesResponseDto likesResponseDto : likesList) {
            if(likesResponseDto.getMember().getId() == member.getId()) {
                return true;
            }
        }

        return false;
    }
}
