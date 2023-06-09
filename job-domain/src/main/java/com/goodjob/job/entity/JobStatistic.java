package com.goodjob.job.entity;

import com.goodjob.job.dto.JobResponseDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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

    private String startDate;

    private String deadLine;

    private int career; // 년차 신입은 default 0

    private int sectorCode;

    private String place;

    public static JobStatistic create(JobResponseDto jobResponseDto) {
        return builder()
                .company(jobResponseDto.getCompany())
                .subject(jobResponseDto.getSubject())
                .sectorCode(jobResponseDto.getSectorCode())
                .url(jobResponseDto.getUrl())
                .sector(jobResponseDto.getSector())
                .startDate(jobResponseDto.getCreateDate())
                .deadLine(jobResponseDto.getDeadLine())
                .career(jobResponseDto.getCareer())
                .place(jobResponseDto.getPlace()).build();
    }
}
