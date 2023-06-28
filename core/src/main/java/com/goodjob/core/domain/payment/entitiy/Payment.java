package com.goodjob.core.domain.payment.entitiy;

import com.goodjob.core.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    // TODO: 추후수정
    private String 결제일자;
    private String 결제금액;
    private String 결제수단;
    private String 카드정보; // 카드번호, 소유자
    private String 결제상태; // 성공, 실패, 보류
    private String 주문정보;
    private String 상품정보;
}
