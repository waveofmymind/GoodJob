package com.goodjob.batch;

import com.goodjob.batch.job.dto.JobResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;

@RequiredArgsConstructor
public class JobProcess implements ItemProcessor<JobResponseDto, JobResponseDto> {

    @Override
    public JobResponseDto process(JobResponseDto item) throws Exception {
        return item;
    }
}
