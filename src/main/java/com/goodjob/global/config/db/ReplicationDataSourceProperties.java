package com.goodjob.global.config.db;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Getter
@Setter
@Configuration
@Profile("prod")
@ConfigurationProperties(prefix = "spring.datasource.replication")
public class ReplicationDataSourceProperties {

    private String username;
    private String password;
    private String driverClassName;
    private Write write;
    private List<Read> reads;

    @Getter
    @Setter
    public static class Write {
        private String name;
        private String url;
    }

    @Getter
    @Setter
    public static class Read {
        private String name;
        private String url;
    }
}


