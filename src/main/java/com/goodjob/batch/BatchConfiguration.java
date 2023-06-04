package com.goodjob.batch;

import com.goodjob.domain.job.api.SaraminApiManager;
import com.goodjob.domain.job.crawling.WontedStatistic;
import com.goodjob.domain.job.dto.JobResponseDto;
import com.goodjob.domain.job.service.JobStatisticService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriverException;
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

import java.io.IOException;
import java.util.List;


@Configuration
@Slf4j
@RequiredArgsConstructor
public class BatchConfiguration {
    private final JobStatisticService jobStatisticService;
    private final PlatformTransactionManager transactionManager;
    private final WontedStatistic wontedStatistic;

    // 삭제 로직 추가 해야함
    @Bean
    public Job job1(JobRepository jobRepository) {
        return new JobBuilder("job1", jobRepository)
                .start(step1(jobRepository)) // 사람인
                .next(step2(jobRepository)) // 원티드 백엔드
                .next(step3(jobRepository)) // 원티드 프론트
                .next(step4(jobRepository)) // 원티드 풀스택
                .next(step5(jobRepository)) // 중복된 값 필터하고 db저장
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
            //TODO: 사람인 공고 기간 필터로직 추가
            /**
             * @param sectorCode 백엔드 84, 프론트 92, 풀스택 2232
             */
            SaraminApiManager.saraminStatistic(84);
            SaraminApiManager.saraminStatistic(92);
            SaraminApiManager.saraminStatistic(2232);
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    @JobScope
    public Step step2(JobRepository jobRepository) {
        return new StepBuilder("wontedBack", jobRepository)
                .tasklet(taskletWontedBack(), transactionManager)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet taskletWontedBack() {
        return (contribution, chunkContext) -> {
            System.out.println("Wonted 테스크렛");
            int back = 872;
            for (int i = 0; i < 10; i += 3) {
                try {
                    wontedStatistic.crawlWebsite(back, i);
                }  catch (IOException | InterruptedException | WebDriverException e) {
                    log.error(e.getMessage());
                }
            }
            return RepeatStatus.FINISHED;
        };
    }

    @JobScope
    @Bean
    public Step step3(JobRepository repository) {
        return new StepBuilder("WontedFront", repository)
                .tasklet(taskletWontedFront(), transactionManager)
                .build();
    }

    @StepScope
    @Bean
    public Tasklet taskletWontedFront() {
        return (contribution, chunkContext) -> {
            int front = 669;
            for (int i = 0; i < 10; i += 3) {
                try {
                    wontedStatistic.crawlWebsite(front, i);
                }  catch (IOException | InterruptedException | WebDriverException e) {
                    log.error(e.getMessage());
                }
            }
            return RepeatStatus.FINISHED;
        };
    }

    @JobScope
    @Bean
    public Step step4(JobRepository repository) {
        return new StepBuilder("WontedFullStack", repository)
                .tasklet(taskletWontedFullStack(), transactionManager)
                .build();
    }




    @StepScope
    @Bean
    public Tasklet taskletWontedFullStack() {
        return (contribution, chunkContext) -> {
            int fullStack = 873;
            for (int i = 0; i < 10; i += 3) {
                try {
                    wontedStatistic.crawlWebsite(fullStack, i);
                } catch (IOException | InterruptedException | WebDriverException e) {
                    log.error(e.getMessage());
                }
            }
            return RepeatStatus.FINISHED;
        };
    }


    @JobScope
    @Bean
    public Step step5(JobRepository jobRepository) {
        return new StepBuilder("DB 작업", jobRepository)
                .tasklet(taskletStatistic(), transactionManager)
                .build();

    }

    @StepScope
    @Bean
    public Tasklet taskletStatistic() {
        return (contribution, chunkContext) -> {
            List<JobResponseDto> pureSaram = SaraminApiManager.getJobResponseDtos();
            List<JobResponseDto> filterSaram = jobStatisticService.sameDtoFilter(pureSaram, pureSaram);
            System.out.println("필터 전 : " + pureSaram.size() + "필터 후 : " + filterSaram.size());
            List<JobResponseDto> pureWonted = WontedStatistic.getJobResponseDtos();
            List<JobResponseDto> filterWonted = jobStatisticService.sameDtoFilter(pureWonted, pureWonted);
            System.out.println("필터전 : " + pureWonted.size() + "필터 후 : " + filterWonted.size());
            // 사람인 에서 받은것 잡코리아 동일내용 필터
            List<JobResponseDto> saram = jobStatisticService.getFilterDto(filterSaram, filterWonted);
            // 잡코리아 에서 받은것 사람인 동일내용 필터
            List<JobResponseDto> wonted = jobStatisticService.getFilterDto(filterWonted, filterSaram);

            for (JobResponseDto dto : saram) {
                try {
                    jobStatisticService.create(dto);
                } catch (IllegalStateException e) {
                    log.error(e.getMessage());
                }
            }
            for (JobResponseDto dto : wonted) {
                try {
                    jobStatisticService.create(dto);
                } catch (IllegalStateException e) {
                    log.error(e.getMessage());
                }
            }
            return RepeatStatus.FINISHED;
        };
    }

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


