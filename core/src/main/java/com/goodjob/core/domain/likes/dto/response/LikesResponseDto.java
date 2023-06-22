package com.goodjob.core.domain.likes.dto.response;

import com.goodjob.core.domain.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikesResponseDto {
    private Long id;
    private Member member;
}
