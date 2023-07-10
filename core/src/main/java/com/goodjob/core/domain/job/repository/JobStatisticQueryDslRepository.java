package com.goodjob.core.domain.job.repository;


import com.goodjob.core.domain.job.entity.JobStatistic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobStatisticQueryDslRepository {
    Page<JobStatistic> filterList(int sectorNum, int careerCode, String place, String subject, Pageable pageable);
    Page<JobStatistic> noKeyword(int sectorNum, int careerCode, String place, Pageable pageable);

    List<JobStatistic> findDeadLine(String today, String aMonthAgo);
}
