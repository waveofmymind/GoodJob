package com.goodjob.core.domain.mentoring.dto.response;

import com.goodjob.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MentoringResponseDto {
    private Long id;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String title;
    private String content;
    private Member member;
}

