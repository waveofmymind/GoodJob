package com.goodjob.batch.batch;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BatchProducer {

    private static final String topic = "job";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void batchProducer(String message) throws JsonProcessingException {
        log.debug("produce message : {}", message);
        kafkaTemplate.send(topic, message);
    }
}
