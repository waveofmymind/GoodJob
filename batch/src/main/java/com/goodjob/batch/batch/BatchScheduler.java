package com.goodjob.batch.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
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
//    @Async("async-thread")
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

//    @Scheduled(cron = "${scheduler.cron.job}")
//    @Async("async-thread")
//    public void wontedBack() {
//        log.debug("스케줄링 하는중");
//        try {
//            jobLauncher.run(batchConfiguration.job2(jobRepository),
//                    new JobParametersBuilder().addDate("date", new Date()).toJobParameters());
//        } catch (JobParametersInvalidException | JobExecutionAlreadyRunningException | JobRestartException |
//                 JobInstanceAlreadyCompleteException | UnexpectedRollbackException e) {
//            log.error(e.getMessage());
//        }
//    }
//    @Scheduled(cron = "${scheduler.cron.job}")
//    @Async("async-thread")
//    public void wontedFront() {
//        log.debug("스케줄링 하는중");
//        try {
//            jobLauncher.run(batchConfiguration.job3(jobRepository),
//                    new JobParametersBuilder().addDate("date", new Date()).toJobParameters());
//        } catch (JobParametersInvalidException | JobExecutionAlreadyRunningException | JobRestartException |
//                 JobInstanceAlreadyCompleteException | UnexpectedRollbackException e) {
//            log.error(e.getMessage());
//        }
//    }
//
//    @Scheduled(cron = "${scheduler.cron.job}")
//    @Async("async-thread")
//    public void wontedFullStack() {
//        log.debug("스케줄링 하는중");
//        try {
//            jobLauncher.run(batchConfiguration.job4(jobRepository),
//                    new JobParametersBuilder().addDate("date", new Date()).toJobParameters());
//        } catch (JobParametersInvalidException | JobExecutionAlreadyRunningException | JobRestartException |
//                 JobInstanceAlreadyCompleteException | UnexpectedRollbackException e) {
//            log.error(e.getMessage());
//        }
//    }
}