package com.goodjob.domain.likes.dto.response;

import com.goodjob.domain.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikesResponseDto {
    private Long id;
    private Member member;
}
