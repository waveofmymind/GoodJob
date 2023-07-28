package com.goodjob.resume.adaptor.outs.persistence;

import com.goodjob.resume.domain.resume.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
}
