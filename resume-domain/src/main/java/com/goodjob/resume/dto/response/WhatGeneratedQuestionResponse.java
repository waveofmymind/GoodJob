package com.goodjob.resume.dto.response;


import com.goodjob.resume.domain.*;
import com.goodjob.resume.dto.request.PredictionServiceRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@AllArgsConstructor
public class WhatGeneratedQuestionResponse {
    private List<PredictionResponse> predictionResponse;

    public List<PredictionResponse> getPredictionResponse() {
        return this.predictionResponse;
    }

    public PredictionServiceRequest toServiceDto(Long memberId) {
        List<String> titleList = predictionResponse.stream()
                .map(PredictionResponse::getQuestion)
                .toList();

        List<String> contentList = predictionResponse.stream()
                .map(PredictionResponse::getBestAnswer)
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
        List<Title> titleList = titles.getTitles();
        List<Content> contentList = contents.getContents();

        List<PredictionResponse> predictionResponses = IntStream.range(0, titleList.size())
                .mapToObj(i -> new PredictionResponse(titleList.get(i).getTitle(), contentList.get(i).getContent()))
                .toList();

        return new WhatGeneratedQuestionResponse(predictionResponses);
    }
}
