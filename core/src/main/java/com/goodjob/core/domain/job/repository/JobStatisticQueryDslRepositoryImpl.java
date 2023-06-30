package com.goodjob.core.domain.job.repository;

import com.goodjob.core.domain.job.entity.JobStatistic;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.goodjob.core.domain.job.entity.QJobStatistic.jobStatistic;

@RequiredArgsConstructor
@Transactional
@Repository
public class JobStatisticQueryDslRepositoryImpl implements JobStatisticQueryDslRepository {
    private final JPAQueryFactory jpaQueryFactory;



    @Override
    public Page<JobStatistic> filterList(String subject, Pageable pageable) {
        List<JobStatistic> fetch = jpaQueryFactory.select(jobStatistic)
                .from(jobStatistic)
                .where(jobStatistic.subject.contains(subject))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(jobStatistic.id.desc())
                .fetch();
        return new PageImpl<>(fetch, pageable, fetch.size());
    }
}
