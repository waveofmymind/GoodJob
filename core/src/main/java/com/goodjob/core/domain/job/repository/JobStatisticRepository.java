package com.goodjob.core.domain.job.repository;

import com.goodjob.core.domain.job.dto.JobResponseDto;
import com.goodjob.core.domain.job.entity.JobStatistic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobStatisticRepository extends JpaRepository<JobStatistic, Long> {
    Page<JobStatistic> findByCareerAndSectorCode(int career, int sectorCode, Pageable pageable);
    Page<JobStatistic> findBySectorCode(int sectorCode, Pageable pageable);

    List<JobStatistic> findByUrl(String url);
    @Modifying
    @Query(value = """
            INSERT INTO job_statistic(
                  company,
                  subject,
                  url,
                  sector,
                  start_date,
                  dead_line,
                  career,
                  sector_code
                  )
                  VALUES(
                  :#{#dto.company},
                  :#{#dto.subject},
                  :#{#dto.url},
                  :#{#dto.sector},
                  :#{#dto.createDate},
                  :#{#dto.deadLine},
                  :#{#dto.career},
                  :#{#dto.sectorCode}
                  )
                  ON DUPLICATE KEY UPDATE
                    company = :#{#dto.company},
                    sector = :#{#dto.sector},
                    career = :#{#dto.career},
                 	subject = :#{#dto.subject}
        """, nativeQuery = true)
    void upsert(@Param("dto") JobResponseDto dto);
}
