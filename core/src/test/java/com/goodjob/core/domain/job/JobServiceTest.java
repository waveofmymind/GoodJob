package com.goodjob.core.domain.job;

import com.goodjob.api.GoodjobApplication;
import com.goodjob.core.domain.job.dto.JobResponseDto;
import com.goodjob.core.domain.job.repository.JobStatisticRepository;
import com.goodjob.core.domain.job.service.JobStatisticService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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





    @DisplayName("filter")
    @Test
    void createTest() {
        List<JobResponseDto> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            JobResponseDto forDto = forDtoSet(i);
            list.add(forDto);
        }

        Assertions.assertEquals(100, list.size());

        for (int i = 0; i < 100; i++) {
            JobResponseDto forDto = forDtoSet(i);
            list.add(forDto);
        }
        Assertions.assertEquals(200, list.size());

        List<JobResponseDto> filter = jobStatisticService.sameDtoFilter(list, list);
        Assertions.assertEquals(100, filter.size());
    }

}
