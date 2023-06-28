package com.goodjob.api;

import com.goodjob.core.CoreConfigurationLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;

@EnableCaching
@SpringBootApplication
@Import({CoreConfigurationLoader.class})
public class GoodjobApplication {
    public static void main(String[] args) {
        SpringApplication.run(GoodjobApplication.class, args);
    }
}
