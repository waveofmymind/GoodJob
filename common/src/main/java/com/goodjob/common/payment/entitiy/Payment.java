package com.goodjob.common.payment.entitiy;

import com.goodjob.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String productName; // 상품명 - 프리미엄 회원권

    private Long paymentAmount; // 가격

    private String paymentMethod; // 결제방법 - 카드, 계좌이체, 간편결제

    private String paymentStatus; // 결제상태 - 성공, 실패, 보류

    @Column(unique = true)
    private String orderId;
}
