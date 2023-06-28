package com.goodjob.core.domain.resume.adaptor;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaPredictionProducer {
    private static final String TOPIC_QUESTION = "question-request";
    private static final String TOPIC_ADVICE = "advice-request";
    private final KafkaTemplate<String, String> kafkaTemplate;


    public void sendQuestionRequest(String message) {
        this.kafkaTemplate.send(TOPIC_QUESTION, message);
    }

    public void sendAdviceRequest(String message) {
        this.kafkaTemplate.send(TOPIC_ADVICE, message);
    }

}