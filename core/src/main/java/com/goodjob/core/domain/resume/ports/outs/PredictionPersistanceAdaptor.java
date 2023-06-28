package com.goodjob.core.domain.resume.ports.outs;


import com.goodjob.core.domain.resume.domain.Prediction;
import com.goodjob.core.domain.resume.ports.in.SavePredictionPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PredictionPersistanceAdaptor implements SavePredictionPort{

    private final PredictionRepository predictionRepository;
    @Override
    public void savePrediction(Prediction prediction) {
        predictionRepository.save(prediction);
    }
}
