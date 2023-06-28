package com.goodjob.core.domain.resume.dto.response;


import com.goodjob.core.domain.resume.domain.Contents;
import com.goodjob.core.domain.resume.domain.ServiceType;
import com.goodjob.core.domain.resume.domain.Titles;
import com.goodjob.core.domain.resume.dto.request.PredictionServiceRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public record WhatGeneratedQuestionResponse(
        List<PredictionResponse> predictionResponse

) {
    public WhatGeneratedQuestionResponse() {
        this(new ArrayList<>());
    }

    public PredictionServiceRequest toServiceDto(Long memberId) {
        List<String> titleList = predictionResponse.stream()
                .map(PredictionResponse::question)
                .toList();

        List<String> contentList = predictionResponse.stream()
                .map(PredictionResponse::bestAnswer)
                .toList();

        Titles titles = Titles.of(titleList);
        Contents contents = Contents.of(contentList);

        return PredictionServiceRequest.builder()
                .serviceType(ServiceType.EXPECTED_QUESTION)
                .memberId(memberId)
                .titles(titles)
                .contents(contents)
                .build();
    }

}
