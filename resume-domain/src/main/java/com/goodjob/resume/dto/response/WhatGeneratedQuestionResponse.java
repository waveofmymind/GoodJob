package com.goodjob.resume.dto.response;


import com.goodjob.resume.domain.Contents;
import com.goodjob.resume.domain.ServiceType;
import com.goodjob.resume.domain.Titles;
import com.goodjob.resume.dto.request.PredictionServiceRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    public static WhatGeneratedQuestionResponse of(Titles titles, Contents contents) {
        List<String> titleList = titles.getTitles();
        List<String> contentList = contents.getContents();

        List<PredictionResponse> predictionResponses = IntStream.range(0, titleList.size())
                .mapToObj(i -> new PredictionResponse(titleList.get(i), contentList.get(i)))
                .toList();

        return new WhatGeneratedQuestionResponse(predictionResponses);
    }

}
