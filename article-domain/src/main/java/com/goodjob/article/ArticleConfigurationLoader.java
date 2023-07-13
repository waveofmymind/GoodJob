package com.goodjob.article;

import com.goodjob.common.CommonConfigurationLoader;
import com.goodjob.member.MemberConfigurationLoader;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@Import({MemberConfigurationLoader.class, CommonConfigurationLoader.class})
@EnableJpaRepositories(basePackages = "com.goodjob.article")
public class ArticleConfigurationLoader {
}
