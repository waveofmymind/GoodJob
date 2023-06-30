package com.goodjob.resume.dto.response;

import com.goodjob.resume.domain.Contents;
import com.goodjob.resume.domain.Prediction;
import com.goodjob.resume.domain.ServiceType;
import com.goodjob.resume.domain.Titles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ResponsePredictionDto {

    private Long id;

    private ServiceType serviceType;

    Titles titles;

    Contents contents;

    LocalDateTime createdDate;

    public static ResponsePredictionDto toDto(Prediction prediction) {
        return ResponsePredictionDto.builder()
                .id(prediction.getId())
                .serviceType(prediction.getServiceType())
                .contents(prediction.getContents())
                .titles(prediction.getTitles())
                .createdDate(prediction.getCreatedDate()).build();
    }
}
