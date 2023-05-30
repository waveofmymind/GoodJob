package com.goodjob.domain.resume.dto.response;


import java.util.ArrayList;
import java.util.List;

public record WhatGeneratedResponse(
        List<PredictionResponse> predictionResponse

) {
    public WhatGeneratedResponse() {
        this(new ArrayList<>());
    }
}
