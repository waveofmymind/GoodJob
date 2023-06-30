package com.goodjob.resume.ports.outs;



import com.goodjob.resume.domain.Prediction;
import com.goodjob.resume.ports.in.FindPredictionPort;
import com.goodjob.resume.ports.in.SavePredictionPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PredictionPersistenceAdaptor implements SavePredictionPort, FindPredictionPort {

    private final PredictionRepository predictionRepository;
    @Override
    public void savePrediction(Prediction prediction) {
        predictionRepository.save(prediction);
    }

    @Override
    public Optional<Prediction> findPredictionById(Long id) {
        return predictionRepository.findById(id);
    }

    @Override
    public Optional<Prediction> findPredictionByMemberId(Long memberId) {
        return predictionRepository.findByMemberId(memberId);
    }

    @Override
    public List<Prediction> findPredictionsByMemberId(Long memberId) {
        return predictionRepository.findAllByMemberId(memberId);
    }
}
