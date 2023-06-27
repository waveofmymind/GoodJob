package com.goodjob.core.domain.resume.domain.usecase;

import com.goodjob.core.domain.resume.dto.request.PredictionServiceRequest;

public interface SavePredictionUseCase {

    void savePrediction(PredictionServiceRequest request);
}
