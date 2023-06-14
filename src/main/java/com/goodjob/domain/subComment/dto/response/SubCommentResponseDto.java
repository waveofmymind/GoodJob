package com.goodjob.domain.subComment.dto.response;

import com.goodjob.domain.likes.dto.response.LikesResponseDto;
import com.goodjob.domain.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class SubCommentResponseDto {

   private Long id;
   private LocalDateTime createdDate;
   private LocalDateTime modifiedDate;
   private Member member;
   private String content;

   private boolean isDeleted;
   private List<LikesResponseDto> likesList;

   public boolean isMemberLikesSubComment(Member member) {
      for(LikesResponseDto likesResponseDto : likesList) {
         if(likesResponseDto.getMember().getId() == member.getId()) {
            return true;
         }
      }

      return false;
   }
}
