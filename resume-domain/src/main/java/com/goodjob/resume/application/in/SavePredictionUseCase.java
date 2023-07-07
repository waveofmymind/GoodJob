package com.goodjob.resume.application.in;

import com.goodjob.resume.dto.request.PredictionServiceRequest;

public interface SavePredictionUseCase {

    void savePrediction(PredictionServiceRequest request);
}
