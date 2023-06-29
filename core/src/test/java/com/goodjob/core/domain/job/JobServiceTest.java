package com.goodjob.core.domain.job;

import com.goodjob.api.GoodjobApplication;
import com.goodjob.core.domain.job.dto.JobResponseDto;
import com.goodjob.core.domain.job.repository.JobStatisticRepository;
import com.goodjob.core.domain.job.service.JobStatisticService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = {JobStatisticService.class, JobStatisticRepository.class, GoodjobApplication.class})
@Transactional
public class JobServiceTest {

    @Autowired
    JobStatisticService jobStatisticService;

    @Autowired
    JobStatisticRepository jobStatisticRepository;



    JobResponseDto dtoSet() {
        return new JobResponseDto("company", "subject", "url", "sector", 1, "create", "deadLine", 1, "place");
    }
    JobResponseDto forDtoSet(int i) {
        return new JobResponseDto("company" + i, "subject" + i, "url" + i, "sector" + i, 1 + i, "create" + i, "deadLine" + i, 1 + i, "place" + i);
    }



    @DisplayName("create")
    @Test
    void createTest() {
        JobResponseDto dto = dtoSet();
        jobStatisticService.create(dto);
    }
}
