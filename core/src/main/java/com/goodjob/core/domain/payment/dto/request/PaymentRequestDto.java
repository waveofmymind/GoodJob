package com.goodjob.core.domain.payment.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequestDto {
    String orderId;

    String paymentKey;

    Long amount;
}
