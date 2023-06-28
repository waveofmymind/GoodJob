package com.goodjob.core.domain.mentoring.dto.response;

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
public class MentoringResponseDto {
    private Long id;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String title;
    private String content;
    private Member member;
}

