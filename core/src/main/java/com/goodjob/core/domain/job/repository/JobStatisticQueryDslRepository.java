package com.goodjob.core.domain.job.repository;


import com.goodjob.core.domain.job.entity.JobStatistic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface JobStatisticQueryDslRepository {
    Page<JobStatistic> filterList(String subject, Pageable pageable);
}
