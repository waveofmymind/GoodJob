package com.goodjob.common.email.repository;

import com.goodjob.common.email.entity.SendEmailLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SendEmailLogRepository extends JpaRepository<SendEmailLog, Long> {
    Optional<SendEmailLog> findByEmail(String email);

    Optional<SendEmailLog> findByUsername(String username);
}
