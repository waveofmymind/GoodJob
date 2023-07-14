package com.goodjob.resume.adaptor.outs.persistence;



import com.goodjob.resume.application.outs.DeletePredictionPort;
import com.goodjob.resume.domain.Prediction;
import com.goodjob.resume.application.outs.FindPredictionPort;
import com.goodjob.resume.application.outs.SavePredictionPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PredictionPersistenceAdaptor implements SavePredictionPort, FindPredictionPort, DeletePredictionPort {

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

    @Override
    public void deletePrediction(Long id) {
        predictionRepository.deleteById(id);
    }
}
