package com.goodjob.core.domain.payment.repository;

import com.goodjob.core.domain.payment.entitiy.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
