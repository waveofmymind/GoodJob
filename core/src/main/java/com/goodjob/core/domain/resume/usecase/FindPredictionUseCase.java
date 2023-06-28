package com.goodjob.core.domain.resume.usecase;

import com.goodjob.core.domain.resume.domain.Prediction;

import java.util.List;

public interface FindPredictionUseCase {

    Prediction getPrediction(Long memberId);

    List<Prediction> getPredictions(Long memberId);
}
