package com.goodjob.core.domain.payment.repository;

import com.goodjob.core.domain.payment.entitiy.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderId(String orderId);
}
