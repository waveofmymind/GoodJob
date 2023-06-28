package com.goodjob.core.domain.resume.ports.outs;


import com.goodjob.core.domain.resume.domain.Prediction;
import com.goodjob.core.domain.resume.ports.in.FindPredictionPort;
import com.goodjob.core.domain.resume.ports.in.SavePredictionPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PredictionPersistanceAdaptor implements SavePredictionPort, FindPredictionPort {

    private final PredictionRepository predictionRepository;
    @Override
    public void savePrediction(Prediction prediction) {
        predictionRepository.save(prediction);
    }

    @Override
    public Optional<Prediction> findPredictionByMemberId(Long memberId) {
        return predictionRepository.findByMemberId(memberId);
    }
}
