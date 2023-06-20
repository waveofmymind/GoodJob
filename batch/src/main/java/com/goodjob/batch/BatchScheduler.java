package com.goodjob.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.UnexpectedRollbackException;

import java.util.Date;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class BatchScheduler {
    private final JobLauncher jobLauncher;
    private final BatchConfiguration batchConfiguration;
    private final JobRepository jobRepository;


    @Scheduled(cron = "${scheduler.cron.job}")
    public void runJob() {
        log.debug("스케줄링 하는중");
        try {
            jobLauncher.run(batchConfiguration.job1(jobRepository),
                    new JobParametersBuilder().addDate("date", new Date()).toJobParameters());
        } catch (JobParametersInvalidException | JobExecutionAlreadyRunningException | JobRestartException |
                 JobInstanceAlreadyCompleteException | UnexpectedRollbackException e) {
            log.error(e.getMessage());
        }
    }
}