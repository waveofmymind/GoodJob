package com.goodjob.api;

import com.goodjob.BatchConfigurationLoader;
import com.goodjob.core.CoreConfigurationLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.goodjob.batch.job.repository","com.goodjob.core"})
@Import({CoreConfigurationLoader.class, BatchConfigurationLoader.class})
public class GoodjobApplication {
    public static void main(String[] args) {
        SpringApplication.run(GoodjobApplication.class, args);
    }
}