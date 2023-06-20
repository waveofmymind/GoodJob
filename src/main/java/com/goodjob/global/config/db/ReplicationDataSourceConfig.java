package com.goodjob.global.config.db;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@Profile("prod")
public class ReplicationDataSourceConfig {
    private final ReplicationDataSourceProperties replicationDataSourceProperties;

    @Primary
    @Bean(name = "dataSource")
    public DataSource routingDataSource() {
        ReplicationRoutingDataSource replicationRoutingDataSource = new ReplicationRoutingDataSource();

        ReplicationDataSourceProperties.Write write = replicationDataSourceProperties.getWrite();
        DataSource writeDataSource = createDataSource(write.getUrl());

        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put(write.getName(), writeDataSource);

        List<ReplicationDataSourceProperties.Read> reads = replicationDataSourceProperties.getReads();
        for (ReplicationDataSourceProperties.Read read : reads) {
            dataSourceMap.put(read.getName(), createDataSource(read.getUrl()));
        }

        replicationRoutingDataSource.setDefaultTargetDataSource(writeDataSource);
        replicationRoutingDataSource.setTargetDataSources(dataSourceMap);
        replicationRoutingDataSource.afterPropertiesSet();

        return new LazyConnectionDataSourceProxy(replicationRoutingDataSource);
    }

    private DataSource createDataSource(String url) {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setDriverClassName(replicationDataSourceProperties.getDriverClassName());
        hikariDataSource.setUsername(replicationDataSourceProperties.getUsername());
        hikariDataSource.setPassword(replicationDataSourceProperties.getPassword());
        hikariDataSource.setJdbcUrl(url);

        return hikariDataSource;
    }
}