package com.goodjob.job.consume;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodjob.job.dto.JobResponseDto;
import com.goodjob.job.service.JobStatisticService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BatchConsumer {
    private final JobStatisticService service;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "job-local", groupId = "job-group")
    public void batchConsumer(String message) {
        try {
            JobResponseDto jobResponseDto = objectMapper.readValue(message, JobResponseDto.class);
            log.debug("consume message : {}", jobResponseDto.getUrl());
            service.upsert(jobResponseDto);
        } catch (Exception e) {
            log.error("batch consumer error : {}", e.getMessage());
        }
    }
}
