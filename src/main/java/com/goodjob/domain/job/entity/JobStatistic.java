package com.goodjob.domain.job.entity;

import com.goodjob.domain.job.dto.JobResponseDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@SuperBuilder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class JobStatistic {
    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String company;

    private String subject;

    @Column(unique = true)
    private String url;

    private String sector;

    private LocalDateTime startDate;

    private LocalDateTime deadLine;

    private int career; // 년차 신입은 default 0

    private int sectorCode;

    public static JobStatistic create(JobResponseDto jobResponseDto) {
        return builder()
                .company(jobResponseDto.getCompany())
                .subject(jobResponseDto.getSubject())
                .sectorCode(jobResponseDto.getSectorCode())
                .url(jobResponseDto.getUrl())
                .sector(jobResponseDto.getSector())
                .startDate(jobResponseDto.getCreateDate())
                .deadLine(jobResponseDto.getDeadLine())
                .career(jobResponseDto.getCareer()).build();
    }
}
