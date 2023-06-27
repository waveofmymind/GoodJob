package com.goodjob.core.domain.resume.ports.in;


import com.goodjob.core.domain.resume.domain.Prediction;
import com.goodjob.core.domain.resume.dto.request.PredictionServiceRequest;

public interface SavePredictionPort {

    void savePrediction(Prediction prediction);
}
