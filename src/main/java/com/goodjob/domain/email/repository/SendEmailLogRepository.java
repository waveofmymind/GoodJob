package com.goodjob.domain.email.repository;

import com.goodjob.domain.email.entity.SendEmailLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SendEmailLogRepository extends JpaRepository<SendEmailLog, Long> {

    Optional<SendEmailLog> findByVerificationCode(String verificationCode);
}
