package com.goodjob.resume.domain;

import com.goodjob.resume.config.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Where(clause = "is_deleted = false")
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

    @Builder.Default
    private Boolean isDeleted = false;
}
