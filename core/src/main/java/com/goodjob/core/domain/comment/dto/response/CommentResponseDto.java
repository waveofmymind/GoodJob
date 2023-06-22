package com.goodjob.core.domain.comment.dto.response;

import com.goodjob.core.domain.likes.dto.response.LikesResponseDto;
import com.goodjob.core.domain.member.entity.Member;
import com.goodjob.core.domain.subComment.dto.response.SubCommentResponseDto;
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

    public boolean isMemberLikesComment(Member member) {
        for(LikesResponseDto likesResponseDto : likesList) {
            if(likesResponseDto.getMember().getId() == member.getId()) {
                return true;
            }
        }

        return false;
    }
}
