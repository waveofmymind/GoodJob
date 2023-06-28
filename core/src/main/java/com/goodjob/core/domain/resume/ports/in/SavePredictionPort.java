package com.goodjob.core.domain.resume.ports.in;


import com.goodjob.core.domain.resume.domain.Prediction;


public interface SavePredictionPort {

    void savePrediction(Prediction prediction);
}
