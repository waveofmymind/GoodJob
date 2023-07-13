package com.goodjob.article.domain.article.dto.response;

import com.goodjob.article.domain.comment.dto.response.CommentResponseDto;
import com.goodjob.article.domain.hashTag.dto.response.HashTagResponseDto;
import com.goodjob.article.domain.likes.dto.response.LikesResponseDto;
import com.goodjob.member.entity.Member;
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
    private int category;

    public boolean isMemberLikesArticle(Member member) {
        for(LikesResponseDto likesResponseDto : likesList) {
            if(likesResponseDto.getMember().getId() == member.getId()) {
                return true;
            }
        }

        return false;
    }

    public String getCategoryDisplayName() {
        return switch(category) {
            case 2 -> "취준";
            case 3 -> "자소서";
            case 4 -> "면접";
            case 5 -> "Q&A";
            case 6 -> "이직";
            case 7 -> "퇴사";
            case 8 -> "잡담";
            default -> "신입";
        };
    }
}
