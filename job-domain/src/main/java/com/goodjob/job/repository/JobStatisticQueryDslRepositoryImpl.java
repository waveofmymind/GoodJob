package com.goodjob.job.repository;

import com.goodjob.job.entity.JobStatistic;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.goodjob.job.entity.QJobStatistic.jobStatistic;


@RequiredArgsConstructor
@Transactional
@Repository
public class JobStatisticQueryDslRepositoryImpl implements JobStatisticQueryDslRepository {
    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public Page<JobStatistic> filterList(int sectorNum, int careerCode, String place, String subject, Pageable pageable) {
        JPAQuery<JobStatistic> query;
        if (careerCode == -1) {
            query = jpaQueryFactory.select(jobStatistic)
                    .from(jobStatistic)
                    .where(
                            jobStatistic.subject.contains(subject), jobStatistic.place.contains(place),
                            jobStatistic.sectorCode.eq(sectorNum))
                    .orderBy(jobStatistic.id.desc());
        } else {
            query = jpaQueryFactory.select(jobStatistic)
                    .from(jobStatistic)
                    .where(
                            jobStatistic.subject.contains(subject), jobStatistic.place.contains(place),
                            jobStatistic.sectorCode.eq(sectorNum), jobStatistic.career.eq(careerCode))
                    .orderBy(jobStatistic.id.desc());
        }


        List<JobStatistic> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = query.fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<JobStatistic> noKeyword(int sectorNum, int careerCode, String place, Pageable pageable) {
        JPAQuery<JobStatistic> query = jpaQueryFactory.select(jobStatistic)
                .from(jobStatistic)
                .where(jobStatistic.place.contains(place), jobStatistic.sectorCode.eq(sectorNum), jobStatistic.career.eq(careerCode))
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
