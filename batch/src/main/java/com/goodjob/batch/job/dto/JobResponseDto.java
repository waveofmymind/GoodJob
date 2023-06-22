package com.goodjob.batch.job.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobResponseDto {
    private String company; // 회사명

    private String subject; // 공고명

    private String url; // 주소

    private String sector; // 분야 백엔드, 프론트엔드

    private int sectorCode;

    private String createDate; // 생성일

    private String deadLine; // 마감일

    private int career; // 경력
}