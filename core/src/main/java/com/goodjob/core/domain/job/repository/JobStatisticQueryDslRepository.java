package com.goodjob.core.domain.job.repository;


import com.goodjob.core.domain.job.entity.JobStatistic;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobStatisticQueryDslRepository {
    List<JobStatistic> findAll();


}
