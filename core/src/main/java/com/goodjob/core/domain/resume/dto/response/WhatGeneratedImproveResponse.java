package com.goodjob.core.domain.resume.dto.response;

import java.util.ArrayList;
import java.util.List;

public record WhatGeneratedImproveResponse (

    List<ImprovementResponse> improvementResponse

)
    {
    public WhatGeneratedImproveResponse() {
        this(new ArrayList<>());
    }

    }
