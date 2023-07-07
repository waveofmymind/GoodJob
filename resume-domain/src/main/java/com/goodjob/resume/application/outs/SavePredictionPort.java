package com.goodjob.resume.application.outs;


import com.goodjob.resume.domain.Prediction;

public interface SavePredictionPort {

    void savePrediction(Prediction prediction);
}
