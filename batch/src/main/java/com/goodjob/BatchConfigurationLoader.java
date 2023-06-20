package com.goodjob;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableAspectJAutoProxy(exposeProxy = true)
@EnableCaching
@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableJpaRepositories
public class BatchConfigurationLoader {
}
