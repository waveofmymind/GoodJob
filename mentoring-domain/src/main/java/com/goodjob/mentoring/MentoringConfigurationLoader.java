package com.goodjob.mentoring;

import com.goodjob.member.MemberConfigurationLoader;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = "com.goodjob.mentoring")
@Import({MemberConfigurationLoader.class})
public class MentoringConfigurationLoader {
}
