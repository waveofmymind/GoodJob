package com.goodjob.core.domain.resume.dto.response;

import com.goodjob.core.domain.resume.domain.Contents;
import com.goodjob.core.domain.resume.domain.ServiceType;
import com.goodjob.core.domain.resume.domain.Titles;
import com.goodjob.core.domain.resume.dto.request.PredictionServiceRequest;

import java.util.ArrayList;
import java.util.List;

public record WhatGeneratedImproveResponse (

    List<ImprovementResponse> improvementResponse

)
    {
    public WhatGeneratedImproveResponse() {
        this(new ArrayList<>());
    }

    public PredictionServiceRequest toServiceDto(Long memberId) {
        List<String> titleList = improvementResponse.stream()
                .map(ImprovementResponse::advice)
                .toList();

        List<String> contentList = improvementResponse.stream()
                .map(ImprovementResponse::advice)
                .toList();

        Titles titles = Titles.of(titleList);
        Contents contents = Contents.of(contentList);

        return PredictionServiceRequest.builder()
                .serviceType(ServiceType.EXPECTED_ADVICE)
                .memberId(memberId)
                .titles(titles)
                .contents(contents)
                .build();
    }
    }
