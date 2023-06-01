package com.goodjob.domain.job.repository;

import com.goodjob.domain.job.entity.JobStatistic;

import java.util.List;

public interface JobStatisticQueryDslRepository {
    List<JobStatistic> findAll();
}
