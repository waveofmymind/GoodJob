package com.goodjob.batch.batch;

import com.goodjob.core.domain.job.dto.JobResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.item.ItemProcessor;

import javax.annotation.Nullable;

@RequiredArgsConstructor
public class JobProcess implements ItemProcessor<JobResponseDto, JobResponseDto> {

    @Override
    public JobResponseDto process(@Nullable JobResponseDto item) throws Exception {
        if (item == null) {
            System.out.println("끝");
        }
        return item;
    }

    @AfterStep
    public ExitStatus afterStep(StepExecution stepExecution) {
        return ExitStatus.COMPLETED;
    }
}
