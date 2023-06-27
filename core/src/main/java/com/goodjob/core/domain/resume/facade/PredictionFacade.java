package com.goodjob.core.domain.resume.facade;

import com.goodjob.core.domain.resume.domain.Prediction;
import com.goodjob.core.domain.resume.domain.usecase.SavePredictionUseCase;
import com.goodjob.core.domain.resume.dto.request.PredictionServiceRequest;
import com.goodjob.core.domain.resume.ports.in.SavePredictionPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PredictionFacade implements SavePredictionUseCase {

    private final SavePredictionPort savePredictionPort;

    @Override
    @Transactional
    public void savePrediction(PredictionServiceRequest request) {
        Prediction prediction = request.toEntity();
        savePredictionPort.savePrediction(prediction);
    }
}