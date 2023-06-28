package com.goodjob.core.domain.resume.usecase;

import com.goodjob.core.domain.resume.dto.request.PredictionServiceRequest;

public interface SavePredictionUseCase {

    void savePrediction(PredictionServiceRequest request);
}
