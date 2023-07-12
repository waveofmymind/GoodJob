package com.goodjob.core;

import com.goodjob.common.CommonConfigurationLoader;
import com.goodjob.resume.ResumeConfigurationLoader;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@Configuration
@ComponentScan
@EnableAutoConfiguration
@Import({ResumeConfigurationLoader.class, CommonConfigurationLoader.class})
@EnableJpaRepositories(basePackages = "com.goodjob.core")
public class CoreConfigurationLoader {
}