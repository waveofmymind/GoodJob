package com.goodjob.core.domain.payment.dto.request;

import lombok.Getter;

@Getter
public class PaymentRequestDto {
    String orderId;

    String paymentKey;

    Integer amount;
}
