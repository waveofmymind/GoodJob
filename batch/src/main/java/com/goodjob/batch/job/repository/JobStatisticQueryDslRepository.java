package com.goodjob.batch.job.repository;


import com.goodjob.batch.job.entity.JobStatistic;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobStatisticQueryDslRepository {
    List<JobStatistic> findAll();


}
