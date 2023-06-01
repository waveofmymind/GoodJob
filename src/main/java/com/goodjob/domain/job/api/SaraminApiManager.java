package com.goodjob.domain.job.api;

import com.goodjob.domain.job.Constants;
import com.goodjob.domain.job.api.jsonproperty.Job;
import com.goodjob.domain.job.api.jsonproperty.JobsWrapper;
import com.goodjob.domain.job.dto.JobResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class SaraminApiManager {



    private static String key;
    private final static List<JobResponseDto> jobResponseDtos = new ArrayList<>();


    @Value("${accessKey.saramin}")
    public void setKey(String value) {
        key = value;
    }


    public static List<JobResponseDto> getJobResponseDtos() {
        return jobResponseDtos;
    }
    public static void setJobDtos(JobResponseDto jobDtos) {
        jobResponseDtos.add(jobDtos);
    }

    public static void saraminStatistic() {
        int sectorCode = 84;
        String job_cd = String.valueOf(sectorCode); // 백엔드 코드
        String connectURL = Constants.SARAMIN + key + "&job_cd=" + job_cd + "&count=110";

        RestTemplate restTemplate = new RestTemplate();
        JobsWrapper jobsWrapper;
        jobsWrapper = restTemplate.getForObject(connectURL, JobsWrapper.class);

        assert jobsWrapper != null;
        for (Job job : jobsWrapper.getJobs().getJob()) {
            String company = job.getCompany().getDetail().getName();
            String subject = job.getPosition().getTitle();
            String url = job.getUrl();
            String sector = job.getPosition().getIndustry().getName();

            long postTimeStamp = Long.parseLong(job.getPostingTimestamp());
            long deadLineTimeStamp;
            if (job.getExpirationDate() == null) {
                deadLineTimeStamp = 0L;
            } else {
                deadLineTimeStamp = Long.parseLong(job.getExpirationDate());
            }

            Instant postTimeInstant = Instant.ofEpochSecond(postTimeStamp);
            Instant deadLineInstant = Instant.ofEpochSecond(deadLineTimeStamp);
            LocalDateTime createDate = LocalDateTime.ofInstant(postTimeInstant, ZoneId.systemDefault());
            LocalDateTime deadLine = LocalDateTime.ofInstant(deadLineInstant, ZoneId.systemDefault());
            int career = job.getPosition().getExperienceLevel().getCareer();

            log.debug(subject);
            JobResponseDto jobResponseDto = new JobResponseDto(company, subject, url, sector, sectorCode, createDate, deadLine, career);
            setJobDtos(jobResponseDto);
        }
    }

}

