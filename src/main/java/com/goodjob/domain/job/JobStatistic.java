package com.goodjob.domain.job;

import com.goodjob.domain.job.dto.JobResponseDto;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class JobStatistic {

    List<JobResponseDto> jobResponseDtos = new ArrayList<>();




    public void setJobDtos(JobResponseDto jobDtos) {
        this.jobResponseDtos.add(jobDtos);
    }
}
