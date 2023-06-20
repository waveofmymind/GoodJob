package com.goodjob;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAspectJAutoProxy(exposeProxy = true)
@EnableCaching
@Configuration
@EnableBatchProcessing
@EnableScheduling
@ComponentScan
@EnableAutoConfiguration
//@EnableJpaRepositories(basePackages = {"com.goodjob.batch.job.repository"})
public class BatchConfigurationLoader {
}