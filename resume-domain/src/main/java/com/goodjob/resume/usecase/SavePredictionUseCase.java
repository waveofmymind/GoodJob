package com.goodjob.resume.usecase;

import com.goodjob.resume.dto.request.PredictionServiceRequest;

public interface SavePredictionUseCase {

    void savePrediction(PredictionServiceRequest request);
}
