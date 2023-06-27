package com.goodjob.batch.batch;


import com.goodjob.batch.crawling.WontedStatistic;

import com.goodjob.batch.job.api.SaraminApiManager;
import com.goodjob.batch.job.dto.JobResponseDto;
import com.goodjob.batch.job.entity.JobStatistic;
import com.goodjob.batch.job.service.JobStatisticService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriverException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.*;
import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.support.AbstractItemStreamItemReader;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.repeat.support.TaskExecutorRepeatTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;


@Configuration
@Slf4j
@RequiredArgsConstructor
public class BatchConfiguration {
    private final JobStatisticService jobStatisticService;
    private final PlatformTransactionManager transactionManager;


    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
        taskExecutor.setConcurrencyLimit(8);
        return taskExecutor;
    }

    // 병렬처리 고민
    @Bean
    public Job job1(JobRepository jobRepository) {
        Flow saramin = new FlowBuilder<SimpleFlow>("saramin")
                .start(step1(jobRepository))
                .build();
        Flow wontedBack = new FlowBuilder<SimpleFlow>("wontedBack")
                .start(step2(jobRepository))
                .build();

        Flow wontedFront = new FlowBuilder<SimpleFlow>("wontedFront")
                .start(step3(jobRepository))
                .build();
        Flow wontedFullStack = new FlowBuilder<SimpleFlow>("wontedFullStack")
                .start(step4(jobRepository))
                .build();
        Flow db = new FlowBuilder<SimpleFlow>("db작업")
                .start(step5(jobRepository))
                .next(step6(jobRepository))
                .next(step7(jobRepository))
                .build();


        return new JobBuilder("job1", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(saramin)
                .split(taskExecutor()).add(wontedBack, wontedFront, wontedFullStack)
                .next(db)
                .end()
                .build();
    }


    @Bean
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
    public Step step2(JobRepository jobRepository) {
        return new StepBuilder("wontedBack", jobRepository)
                .tasklet(taskletWontedBack(), transactionManager)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet taskletWontedBack() {
        return (contribution, chunkContext) -> {
            WontedStatistic wontedStatistic = new WontedStatistic();
            int back = 872;
            for (int i = 0; i < 10; i++) {
                try {
                    wontedStatistic.crawlWebsite(back, i);
                }  catch (IOException | InterruptedException | WebDriverException e) {
                    log.error(e.getMessage());
                }
            }
            return RepeatStatus.FINISHED;
        };
    }

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
            WontedStatistic wontedStatistic = new WontedStatistic();
            for (int i = 0; i < 10; i++) {
                try {
                    wontedStatistic.crawlWebsite(front, i);
                }  catch (IOException | InterruptedException | WebDriverException e) {
                    log.error(e.getMessage());
                }
            }
            return RepeatStatus.FINISHED;
        };
    }

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
            WontedStatistic wontedStatistic = new WontedStatistic();
            for (int i = 0; i < 10; i++) {
                try {
                    wontedStatistic.crawlWebsite(fullStack, i);
                } catch (IOException | InterruptedException | WebDriverException e) {
                    log.error(e.getMessage());
                }
            }
            return RepeatStatus.FINISHED;
        };
    }


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

            List<JobResponseDto> pureWonted = WontedStatistic.getJobResponseDtos();
            List<JobResponseDto> filterWonted = jobStatisticService.sameDtoFilter(pureWonted, pureWonted);

            // 사람인 에서 받은것 잡코리아 동일내용 필터
            List<JobResponseDto> saram = jobStatisticService.getFilterDto(filterSaram, filterWonted);
            // 잡코리아 에서 받은것 사람인 동일내용 필터
            List<JobResponseDto> wonted = jobStatisticService.getFilterDto(filterWonted, filterSaram);

            for (JobResponseDto dto : saram) {
                try {
                    jobStatisticService.create(dto);
                } catch (IllegalStateException | DataIntegrityViolationException e) {
                    log.error(e.getMessage());
                }
            }
            for (JobResponseDto dto : wonted) {
                try {
                    jobStatisticService.create(dto);
                } catch (IllegalStateException | DataIntegrityViolationException e) {
                    log.error(e.getMessage());
                }
            }
            return RepeatStatus.FINISHED;
        };
    }


    @Bean
    public Step step6(JobRepository repository) {
        return new StepBuilder("List 초기화", repository)
                .tasklet(taskletListClear(), transactionManager)
                .build();
    }

    @StepScope
    @Bean
    public Tasklet taskletListClear() {
        return (contribution, chunkContext) -> {
            SaraminApiManager.resetList();
            WontedStatistic.resetList();

            return RepeatStatus.FINISHED;
        };
    }


    @Bean
    public Step step7(JobRepository repository) {
        return new StepBuilder("db삭제", repository)
                .tasklet(taskletDbDelete(), transactionManager)
                .build();
    }

    @StepScope
    @Bean
    public Tasklet taskletDbDelete() {
        return (contribution, chunkContext) -> {
            List<JobStatistic> all = jobStatisticService.getAll();
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            DateTimeFormatter dateTimeFormatter1 = new DateTimeFormatterBuilder().append(dateTimeFormatter)
                    .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                    .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                    .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                    .toFormatter();

            for (JobStatistic jobStatistic : all) {
                if (jobStatistic.getDeadLine().equals("상시채용")) {
                    String startDate = jobStatistic.getStartDate();
                    LocalDateTime createDateTime = LocalDateTime.parse(startDate, dateTimeFormatter1).plusMonths(1);
                    if (now.isAfter(createDateTime)) {
                        jobStatisticService.delete(jobStatistic);
                    }
                }
            }
            return RepeatStatus.FINISHED;
        };
    }

}



/**
 * chunk
 */

/**
 //     * Chunk Processing
 //     *
 //     * @return 백엔드 84, 프론트 92, 풀스택 2232
 //     */
//    @Bean
//    @JobScope
//    public Step chunkSaram(JobRepository repository) {
//        return new StepBuilder("chunk", repository)
//                .<JobResponseDto, JobResponseDto>chunk(100, transactionManager)
//                .reader(saraminReader())
//                .processor(process())
//                .writer(saraminWriter())
//                .taskExecutor(taskExecutor())
//                .build();
//    }
//
//    @Bean
//    @StepScope
//    public ItemReader<JobResponseDto> saraminReader() {
//        // 검색일 최소, 최대 값 설정으로 이후 값만 받아오기
//        SaraminApiManager.saraminStatistic(84);
//        SaraminApiManager.saraminStatistic(92);
//        SaraminApiManager.saraminStatistic(2232);
//        List<JobResponseDto> jobResponseDtos = SaraminApiManager.getJobResponseDtos();
//        return new ListItemReader<>(jobResponseDtos);
//    }
//
//
//    @Bean
//    @StepScope
//    public JobProcess process() {
//        return new JobProcess();
//    }
//
//    @Bean
//    @StepScope
//    public ItemWriter<JobResponseDto> saraminWriter() {
//        return dto -> dto.getItems().forEach(jobStatisticService::upsert);
//    }
//
//
//    @Bean
//    @JobScope
//    public Step wontedBackStep(JobRepository repository) {
//        return new StepBuilder("wontedBack", repository)
//                .<JobResponseDto, JobResponseDto>chunk(100, transactionManager)
//                .reader(wontedBackReader())
//                .processor(wontedBackProcess())
//                .writer(wontedBackWrite())
//                .taskExecutor(taskExecutor())
//                .build();
//    }
//
//    @StepScope
//    @Bean
//    public ItemReader<JobResponseDto> wontedBackReader() {
//        int back = 872;
//        WontedStatistic wontedStatistic = new WontedStatistic();
//        for (int i = 0; i < 10; i++) {
//            try {
//                wontedStatistic.crawlWebsite(back, i);
//            } catch (IOException | InterruptedException | WebDriverException e) {
//                log.error(e.getMessage());
//            }
//        }
//        List<JobResponseDto> dtos = wontedStatistic.getJobResponseDtos();
//        List<JobResponseDto> filterDto = jobStatisticService.sameDtoFilter(dtos, dtos);
//        return new ListItemReader<>(filterDto);
//    }
//
//    @StepScope
//    @Bean
//    public JobProcess wontedBackProcess() {
//        return new JobProcess();
//    }
//
//    @Bean
//    @StepScope
//    public ItemWriter<JobResponseDto> wontedBackWrite() {
//        return dto -> dto.getItems().forEach(jobStatisticService::upsert);
//    }
//
//
//    @Bean
//    @JobScope
//    public Step wontedFrontStep(JobRepository repository) {
//        return new StepBuilder("wontedBack", repository)
//                .<JobResponseDto, JobResponseDto>chunk(100, transactionManager)
//                .reader(wontedFrontReader())
//                .processor(wontedFrontProcess())
//                .writer(wontedFrontWrite())
//                .taskExecutor(taskExecutor())
//                .build();
//    }
//
//    @StepScope
//    @Bean
//    public ItemReader<JobResponseDto> wontedFrontReader() {
//        int front = 669;
//        WontedStatistic wontedStatistic = new WontedStatistic();
//        for (int i = 0; i < 10; i++) {
//            try {
//                wontedStatistic.crawlWebsite(front, i);
//            } catch (IOException | InterruptedException | WebDriverException e) {
//                log.error(e.getMessage());
//            }
//        }
//        List<JobResponseDto> dtos = wontedStatistic.getJobResponseDtos();
//        List<JobResponseDto> filterDto = jobStatisticService.sameDtoFilter(dtos, dtos);
//        return new ListItemReader<>(filterDto);
//    }
//
//    @StepScope
//    @Bean
//    public JobProcess wontedFrontProcess() {
//        return new JobProcess();
//    }
//
//    @Bean
//    @StepScope
//    public ItemWriter<JobResponseDto> wontedFrontWrite() {
//        return dto -> dto.getItems().forEach(jobStatisticService::upsert);
//    }
//
//    @Bean
//    @JobScope
//    public Step wontedFullStackStep(JobRepository repository) {
//        return new StepBuilder("wontedBack", repository)
//                .<JobResponseDto, JobResponseDto>chunk(100, transactionManager)
//                .reader(wontedFullStackReader())
//                .processor(wontedFullStackProcess())
//                .writer(wontedFullStackWrite())
//                .taskExecutor(taskExecutor())
//                .build();
//    }
//
//    @StepScope
//    @Bean
//    public ItemReader<JobResponseDto> wontedFullStackReader() {
//        int fullStack = 873;
//        WontedStatistic wontedStatistic = new WontedStatistic();
//        for (int i = 0; i < 10; i++) {
//            try {
//                wontedStatistic.crawlWebsite(fullStack, i);
//            } catch (IOException | InterruptedException | WebDriverException e) {
//                log.error(e.getMessage());
//            }
//        }
//        List<JobResponseDto> dtos = wontedStatistic.getJobResponseDtos();
//        List<JobResponseDto> filterDto = jobStatisticService.sameDtoFilter(dtos, dtos);
//        return new ListItemReader<>(filterDto);
//    }
//
//    @StepScope
//    @Bean
//    public JobProcess wontedFullStackProcess() {
//        return new JobProcess();
//    }
//
//    @Bean
//    @StepScope
//    public ItemWriter<JobResponseDto> wontedFullStackWrite() {
//        return dto -> dto.getItems().forEach(jobStatisticService::upsert);
//    }