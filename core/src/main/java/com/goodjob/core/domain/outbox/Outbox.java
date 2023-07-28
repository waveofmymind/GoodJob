package com.goodjob.core.domain.outbox;

import com.goodjob.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Outbox extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long aggregateId;

    @Enumerated(EnumType.STRING)
    private AggregateType aggregateType;

    @Column
    private ProcessStatus processStatus = ProcessStatus.READY;
}
