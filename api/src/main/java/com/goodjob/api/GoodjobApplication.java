package com.goodjob.api;

import com.goodjob.mentoring.MentoringConfigurationLoader;
import com.goodjob.common.CommonConfigurationLoader;
import com.goodjob.core.CoreConfigurationLoader;
import com.goodjob.job.JobConfigurationLoader;
import com.goodjob.member.MemberConfigurationLoader;
import com.goodjob.resume.ResumeConfigurationLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@SpringBootApplication
@EnableScheduling
@Import({CoreConfigurationLoader.class, CommonConfigurationLoader.class, ResumeConfigurationLoader.class, JobConfigurationLoader.class
, MemberConfigurationLoader.class, MentoringConfigurationLoader.class})
public class GoodjobApplication {
    public static void main(String[] args) {
        SpringApplication.run(GoodjobApplication.class, args);
    }
}
