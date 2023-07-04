package com.goodjob.core.domain.job.repository;

import com.goodjob.core.domain.job.entity.JobStatistic;
import com.querydsl.jpa.impl.JPAQuery;
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
        JPAQuery<JobStatistic> query = jpaQueryFactory.select(jobStatistic)
                .from(jobStatistic)
                .where(jobStatistic.subject.contains(subject))
                .orderBy(jobStatistic.id.desc());

        List<JobStatistic> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = query.fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public List<JobStatistic> findDeadLine(String today, String aMonthAgo) {

        return jpaQueryFactory.select(jobStatistic)
                .from(jobStatistic)
                .where(jobStatistic.deadLine.eq("상시").and(jobStatistic.startDate.eq(aMonthAgo)), jobStatistic.deadLine.eq(today))
                .fetch();
    }


}
