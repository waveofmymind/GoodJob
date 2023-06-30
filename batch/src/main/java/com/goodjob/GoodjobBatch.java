package com.goodjob;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableBatchProcessing
@EnableScheduling
@EnableAsync
@SpringBootApplication
public class GoodjobBatch {
    public static void main(String[] args) {
        SpringApplication.run(GoodjobBatch.class, args);
    }
}
