package com.goodjob.core.domain.resume.dto.request;

import com.goodjob.core.domain.resume.domain.*;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class PredictionServiceRequest {

    private Long memberId;
    private Titles titles;
    private Contents contents;
    private ServiceType serviceType;

    public Prediction toEntity() {

        return Prediction.builder()
                .serviceType(serviceType)
                .memberId(memberId)
                .titles(titles)
                .contents(contents)
                .build();
    }
}
