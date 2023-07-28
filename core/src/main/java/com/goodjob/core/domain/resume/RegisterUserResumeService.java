package com.goodjob.core.domain.resume

import com.goodjob.core.domain.outbox.AggregateType;
import com.goodjob.core.domain.outbox.OutboxService;
import com.goodjob.resume.adaptor.outs.persistence.ResumePersistenceAdapter;
import com.goodjob.resume.dto.request.CreateResumeCommand;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class RegisterUserResumeService {

    private final OutboxService outboxService;
    private final ResumePersistenceAdapter resumePersistenceAdapter;

    public void saveUserResume(CreateResumeCommand command) {
        Long resumeId = resumePersistenceAdapter.save(command.toEntity());
        outboxService.createOutbox(resumeId, AggregateType.RESUME);
    }
}