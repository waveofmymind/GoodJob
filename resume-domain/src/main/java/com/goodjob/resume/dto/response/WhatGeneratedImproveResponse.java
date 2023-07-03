package com.goodjob.resume.dto.response;


import com.goodjob.resume.domain.*;
import com.goodjob.resume.dto.request.PredictionServiceRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public record WhatGeneratedImproveResponse (

        List<ImprovementResponse> improvementResponse

) {
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

    public static WhatGeneratedImproveResponse of(Titles titles, Contents contents) {
        List<Title> titleList = titles.getTitles();
        List<Content> contentList = contents.getContents();

        List<ImprovementResponse> improvementResponses = IntStream.range(0, titleList.size())
                .mapToObj(i -> new ImprovementResponse(titleList.get(i).getTitle(), contentList.get(i).getContent()))
                .collect(Collectors.toList());

        return new WhatGeneratedImproveResponse(improvementResponses);
    }

}
