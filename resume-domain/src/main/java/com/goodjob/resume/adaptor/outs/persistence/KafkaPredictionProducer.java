package com.goodjob.resume.adaptor.outs.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaPredictionProducer {
    @Value("${custom.kafka.topic.question}")
    private static String TOPIC_QUESTION;
    @Value("${custom.kafka.topic.advice}")
    private static String TOPIC_ADVICE;

    private final KafkaTemplate<String, String> kafkaTemplate;
    public void sendQuestionRequest(String message) {
        this.kafkaTemplate.send(TOPIC_QUESTION, message);
    }

    public void sendAdviceRequest(String message) {
        this.kafkaTemplate.send(TOPIC_ADVICE, message);
    }
}
