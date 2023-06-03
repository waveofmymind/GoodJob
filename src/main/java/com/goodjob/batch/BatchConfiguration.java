package com.goodjob.batch;

import com.goodjob.domain.job.api.SaraminApiManager;
import com.goodjob.domain.job.crawling.SeleniumService;
import com.goodjob.domain.job.service.JobStatisticService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
@Slf4j
@RequiredArgsConstructor
public class BatchConfiguration {
    private final JobStatisticService jobStatisticService;
    private final PlatformTransactionManager transactionManager;
    private final SeleniumService seleniumService;

    // 삭제 로직 추가 해야함
    @Bean
    public Job job1(JobRepository jobRepository) {
        return new JobBuilder("job1", jobRepository)
                .start(step1(jobRepository)) // 사람인
                .next(step2(jobRepository)) // 원티드
//                .next(step3(jobRepository)) // 중복된 값 필터하고 db저장
                .build();
    }

    @Bean
    @JobScope
    public Step step1(JobRepository jobRepository) {
        return new StepBuilder("Saramin", jobRepository)
                .tasklet(taskletSaramin(), transactionManager)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet taskletSaramin() {
        return (contribution, chunkContext) -> {
            System.out.println("사람인 테스크렛");
            // 사람인 공고 기간 필터로직 추가
            SaraminApiManager.saraminStatistic();
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    @JobScope
    public Step step2(JobRepository jobRepository) {
        return new StepBuilder("wonted", jobRepository)
                .tasklet(taskletWonted(), transactionManager)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet taskletWonted() {
        return (contribution, chunkContext) -> {
            System.out.println("Wonted 테스크렛");
            // 원티드
            seleniumService.crawlWebsite();
            return RepeatStatus.FINISHED;
        };
    }

//    @JobScope
//    @Bean
//    public Step step3(JobRepository jobRepository) {
//        return new StepBuilder("DB 작업", jobRepository)
//                .tasklet(taskletStatistic(), transactionManager)
//                .build();
//
//    }

//    @StepScope
//    @Bean
//    public Tasklet taskletStatistic() {
//        return (contribution, chunkContext) -> {
//            List<JobResponseDto> pureSaram = SaraminApiManager.getJobResponseDtos();
//            // 사람인 에서 받은것 잡코리아 동일내용 필터
//            List<JobResponseDto> saram = jobStatisticService.getFilterDto(pureSaram, pureJobKorea);
//            // 잡코리아 에서 받은것 사람인 동일내용 필터
//            List<JobResponseDto> jobKorea = jobStatisticService.getFilterDto(pureJobKorea, pureSaram);
//
//            for (JobResponseDto dto : saram) {
//                jobStatisticService.create(dto);
//            }
//            for (JobResponseDto dto : jobKorea) {
//                jobStatisticService.create(dto);
//            }
//            return RepeatStatus.FINISHED;
//        };
//    }

    /**
     * Chunk Processing
     * @return
     */

//    @Bean
//    @StepScope
//    public ItemReader<JobResponseDto> saraminReader() {
//        // 검색일 최소, 최대 값 설정으로 이후 값만 받아오기
//        saramin.saraminStatistic();
//        List<JobResponseDto> jobResponseDtos = saramin.getJobResponseDtos();
//        return new ListItemReader<>(jobResponseDtos);
//    }
//
//    @Bean
//    @StepScope
//    public ItemProcessor<JobResponseDto, JobResponseDto> saraminProcessor() {
//
//        return
//    }
    
}


