package com.goodjob;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableBatchProcessing
public class GoodjobApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoodjobApplication.class, args);
    }

}
