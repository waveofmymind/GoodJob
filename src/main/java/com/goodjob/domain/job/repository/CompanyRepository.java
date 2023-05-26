package com.goodjob.domain.job.repository;

import com.goodjob.domain.job.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
