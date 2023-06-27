package com.goodjob.core.domain.resume.adaptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaPredictionProducer {
    private static final String TOPIC_QUESTION = "question-request";
    private static final String TOPIC_ADVICE = "advice-request";
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaPredictionProducer(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendQuestionRequest(String message) {
        this.kafkaTemplate.send(TOPIC_QUESTION, message);
    }

    public void sendAdviceRequest(String message) {
        this.kafkaTemplate.send(TOPIC_ADVICE, message);
    }

}