package com.goodjob.common.payment.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequestDto {
    String orderId;
    String paymentKey;
    long amount;
}
