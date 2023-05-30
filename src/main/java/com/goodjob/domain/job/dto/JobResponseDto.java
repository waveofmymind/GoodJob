package com.goodjob.domain.job.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobResponseDto {
    private String company; // 회사명

    private String subject; // 공고명

    private String url; // 주소

    private String sector; // 분야 백엔드, 프론트엔드

    private LocalDateTime createDate; // 생성일

    private LocalDateTime deadLine; // 마감일

    private int career; // 경력
}