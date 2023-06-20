package com.goodjob.job.repository;

import com.goodjob.job.entity.JobStatistic;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobStatisticRepository extends JpaRepository<JobStatistic, Long> {
    Page<JobStatistic> findByCareerAndSectorCode(int career, int sectorCode, Pageable pageable);
    Page<JobStatistic> findBySectorCode(int sectorCode, Pageable pageable);

    List<JobStatistic> findByUrl(String url);
}
