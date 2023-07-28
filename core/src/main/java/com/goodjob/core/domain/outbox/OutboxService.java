package com.goodjob.core.domain.outbox;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class OutboxService {
    private final OutboxRepository outboxRepository;

    public Long createOutbox(Long aggregateId, AggregateType aggregateType) {
        Outbox outbox = Outbox.builder()
                .aggregateId(aggregateId)
                .aggregateType(aggregateType)
                .build();
        return outboxRepository.save(outbox).getId();
    }
}
