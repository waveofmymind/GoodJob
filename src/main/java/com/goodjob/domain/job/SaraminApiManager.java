package com.goodjob.domain.job;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodjob.domain.job.dto.JobResponseDto;
import com.goodjob.domain.job.jsonproperty.Job;
import com.goodjob.domain.job.jsonproperty.JobsWrapper;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

public class SaraminApiManager extends JobStatistic{

    private String key;


    public void saraminStatistic() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JobsWrapper jobsWrapper = objectMapper.readValue(Constant.SARAMIN, JobsWrapper.class);

        for (Job job : jobsWrapper.getJobs().getJob()) {
            String company = job.getCompany().getDetail().getName();
            String subject = job.getPosition().getTitle();
            String url = job.getUrl();
            String sector = job.getPosition().getIndustry().getName();
            LocalDateTime createDate = LocalDateTime.parse(job.getPostingTimestamp());
            LocalDateTime deadLine = LocalDateTime.parse(job.getExpirationDate());

            JobResponseDto jobResponseDto = new JobResponseDto(company, subject, url, sector, createDate, deadLine);
            setJobDtos(jobResponseDto);
        }
    }

}

