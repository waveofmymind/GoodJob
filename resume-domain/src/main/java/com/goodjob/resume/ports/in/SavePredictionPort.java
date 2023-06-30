package com.goodjob.resume.ports.in;


import com.goodjob.resume.domain.Prediction;

public interface SavePredictionPort {

    void savePrediction(Prediction prediction);
}
