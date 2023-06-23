package com.goodjob.gpt.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class KafkaConsumer {

    @KafkaListener(topics = "Test", groupId = "gptgroup")
    public void consume(String message) throws IOException {
        System.out.println(String.format("Consumed message : %s", message));
    }

}