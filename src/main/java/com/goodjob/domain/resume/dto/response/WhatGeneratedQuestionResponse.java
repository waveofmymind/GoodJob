package com.goodjob.domain.resume.dto.response;


import java.util.ArrayList;
import java.util.List;

public record WhatGeneratedQuestionResponse(
        List<PredictionResponse> predictionResponse

) {
    public WhatGeneratedQuestionResponse() {
        this(new ArrayList<>());
    }
}
