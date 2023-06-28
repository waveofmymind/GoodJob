package com.goodjob.core.domain.job.repository;

import com.goodjob.core.domain.job.entity.JobStatistic;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Repository
public class JobStatisticQueryDslRepositoryImpl implements JobStatisticQueryDslRepository {
    private final JPAQueryFactory jpaQueryFactory;



    @Override
    public List<JobStatistic> findAll() {
        return null;
    }


}
