package com.goodjob.resume.adaptor.outs.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@RequiredArgsConstructor
public class KafkaProducerAdapter implements ProducerAdapter {
    private static final String TOPIC_QUESTION = "question-prod";
    private static final String TOPIC_ADVICE = "advice-prod";

    private static final String TOPIC_ERROR = "error-prod";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendQuestionRequest(String message) {
        this.kafkaTemplate.send(TOPIC_QUESTION, message);
    }

    public void sendAdviceRequest(String message) {
        this.kafkaTemplate.send(TOPIC_ADVICE, message);
    }

    public void sendError(String message) {
        this.kafkaTemplate.send(TOPIC_ERROR, message);
    }
}
