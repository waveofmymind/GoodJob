package com.goodjob.resume.application.service;

import com.goodjob.resume.adaptor.outs.persistence.ResumePersistenceAdapter;
import com.goodjob.resume.adaptor.outs.persistence.ResumeRepository;
import com.goodjob.resume.dto.request.CreateResumeCommand;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ResumeService {
    private final ResumePersistenceAdapter resumePersistenceAdapter;

    public void saveResume(CreateResumeCommand command) {
        resumePersistenceAdapter.save(command.toEntity());
    }

}
