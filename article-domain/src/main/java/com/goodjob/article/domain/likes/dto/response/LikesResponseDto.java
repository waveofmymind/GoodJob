package com.goodjob.article.domain.likes.dto.response;

import com.goodjob.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class LikesResponseDto implements Serializable {
    private Long id;
    private Member member;
}
