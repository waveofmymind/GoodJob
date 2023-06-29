package com.goodjob.core.domain.job.batch;

import com.goodjob.api.GoodjobApplication;
import com.goodjob.batch.batch.BatchConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBatchTest
@SpringBootTest(classes = {TestBatchConfig.class, BatchConfiguration.class, GoodjobApplication.class})
public class BatchTest {

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    @DisplayName("job 테스트")
    void simpleJob_test() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addDate("date", new Date())
                .toJobParameters();

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        Assertions.assertEquals(jobExecution.getStatus(), BatchStatus.COMPLETED);
        Assertions.assertEquals(jobExecution.getExitStatus(), ExitStatus.COMPLETED);
    }

}
