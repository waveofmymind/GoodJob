//package com.goodjob.batch.batch;
//
//import com.goodjob.batch.job.dto.JobResponseDto;
//import com.goodjob.batch.job.service.JobStatisticService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.batch.core.ExitStatus;
//import org.springframework.batch.core.StepExecution;
//import org.springframework.batch.core.annotation.AfterStep;
//import org.springframework.batch.item.Chunk;
//import org.springframework.batch.item.ItemWriter;
//
//@RequiredArgsConstructor
//public class JobItemWriter implements ItemWriter<JobResponseDto> {
//
//    private final JobStatisticService jobStatisticService;
//
//
//    @Override
//    public void write(Chunk<? extends JobResponseDto> chunk) throws Exception {
//        chunk.forEach(jobStatisticService::upsert);
//    }
//
//    @AfterStep
//    public ExitStatus afterStep(StepExecution stepExecution) {
//
//        jobStatisticService.
//        return ExitStatus.COMPLETED
//    }
//}
