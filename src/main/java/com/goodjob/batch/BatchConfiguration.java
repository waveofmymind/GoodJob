package com.goodjob.batch;

import com.goodjob.domain.job.api.JobKoreaApiManager;
import com.goodjob.domain.job.api.SaraminApiManager;
import com.goodjob.domain.job.dto.JobResponseDto;
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

import java.util.List;


@Configuration
@Slf4j
@RequiredArgsConstructor
public class BatchConfiguration {
    private final JobStatisticService jobStatisticService;
    private final PlatformTransactionManager transactionManager;

    // 삭제 로직 추가 해야함
    @Bean
    public Job job1(JobRepository jobRepository) {
        return new JobBuilder("job1", jobRepository)
                .start(step1(jobRepository))
                .next(step2(jobRepository))
                .next(step3(jobRepository))
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
        return new StepBuilder("JobKorea", jobRepository)
                .tasklet(taskletJobKorea(), transactionManager)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet taskletJobKorea() {
        return (contribution, chunkContext) -> {
            System.out.println("JobKorea 테스크렛");
            // 잡코리아 공고 기간 필터로직 추가
            JobKoreaApiManager.jobKoreaStatistic();
            return RepeatStatus.FINISHED;
        };
    }

    @JobScope
    @Bean
    public Step step3(JobRepository jobRepository) {
        return new StepBuilder("DB 작업", jobRepository)
                .tasklet(taskletStatistic(), transactionManager)
                .build();

    }

    @StepScope
    @Bean
    public Tasklet taskletStatistic() {
        return (contribution, chunkContext) -> {
            List<JobResponseDto> pureSaram = SaraminApiManager.getJobResponseDtos();
            List<JobResponseDto> pureJobKorea = JobKoreaApiManager.getJobResponseDtos();
            // 사람인 에서 받은것 잡코리아 동일내용 필터
            List<JobResponseDto> saram = jobStatisticService.getFilterDto(pureSaram, pureJobKorea);
            // 잡코리아 에서 받은것 사람인 동일내용 필터
            List<JobResponseDto> jobKorea = jobStatisticService.getFilterDto(pureJobKorea, pureSaram);

            for (JobResponseDto dto : saram) {
                jobStatisticService.create(dto);
            }
            for (JobResponseDto dto : jobKorea) {
                jobStatisticService.create(dto);
            }
            return RepeatStatus.FINISHED;
        };
    }

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


