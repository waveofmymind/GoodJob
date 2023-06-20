package com.goodjob;

import com.goodjob.core.CoreConfigurationLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import({CoreConfigurationLoader.class, BatchConfigurationLoader.class})
@SpringBootApplication
public class GoodjobApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoodjobApplication.class, args);
    }
}
