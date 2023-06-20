package com.goodjob.job.repository;


import com.goodjob.job.entity.JobStatistic;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobStatisticQueryDslRepository {
    List<JobStatistic> findAll();
}
