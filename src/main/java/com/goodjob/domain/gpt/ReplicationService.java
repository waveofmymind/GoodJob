package com.goodjob.domain.gpt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReplicationService {

    private final DataSource lazyDataSource;

    @Transactional(readOnly = true)
    public void read() {
        try (Connection connection = lazyDataSource.getConnection()) {
            log.info("read url = {}",connection.getMetaData().getURL());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void write() {
        try (Connection connection = lazyDataSource.getConnection()) {
            log.info("write url = {}",connection.getMetaData().getURL());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
