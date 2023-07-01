package com.goodjob.resume.domain;

import com.goodjob.resume.config.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Prediction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    @Embedded
    private Titles titles;

    @Embedded
    private Contents contents;
}
