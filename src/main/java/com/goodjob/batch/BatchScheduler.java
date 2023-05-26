package com.goodjob.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Date;

@Configuration
@RequiredArgsConstructor
public class BatchScheduler {
    private final JobLauncher jobLauncher;
    private final BatchConfiguration batchConfiguration;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final Tasklet tasklet;


    @Scheduled(cron = "${scheduler.cron.job}")
    public void runJob() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        System.out.println("스케줄링 하는중임");
        jobLauncher.run(batchConfiguration.job1(jobRepository, batchConfiguration.step1(jobRepository, tasklet, transactionManager)),
                new JobParametersBuilder().addDate("date", new Date()).toJobParameters());
    }
}