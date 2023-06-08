package com.goodjob.domain.job.repository;

import com.goodjob.domain.job.entity.JobStatistic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobStatisticRepository extends JpaRepository<JobStatistic, Long> {
    List<JobStatistic> findBySubjectAndUrl(String subject, String url);
    Page<JobStatistic> findByCareerAndSectorCode(int career, int sectorCode, Pageable pageable);
    Page<JobStatistic> findBySectorCode(int sectorCode, Pageable pageable);
    List<JobStatistic> findBySectorCode(int sectorCode);
    Page<JobStatistic> findBySubjectContaining(String keyword,Pageable pageable);
}
