package com.goodjob.resume.adaptor.outs.persistence;

import com.goodjob.resume.domain.resume.Resume;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResumePersistenceAdapter {
    private final ResumeRepository resumeRepository;

    public Long save(Resume resume) {
        return resumeRepository.save(resume).getId();
    }
}
