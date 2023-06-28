package com.goodjob.core.domain.resume.usecase;

import com.goodjob.core.domain.resume.domain.Prediction;

public interface FindPredictionUseCase {

    Prediction getPrediction(Long memberId);
}
