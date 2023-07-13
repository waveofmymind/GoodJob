package com.goodjob.article.domain.likes.dto.response;

import com.goodjob.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikesResponseDto {
    private Long id;
    private Member member;
}
