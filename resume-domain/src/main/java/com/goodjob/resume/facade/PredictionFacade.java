package com.goodjob.resume.facade;

import com.goodjob.resume.config.error.ErrorCode;
import com.goodjob.resume.config.error.exception.BusinessException;
import com.goodjob.resume.domain.Prediction;
import com.goodjob.resume.dto.response.ResponsePredictionDto;
import com.goodjob.resume.ports.in.FindPredictionPort;
import com.goodjob.resume.usecase.FindPredictionUseCase;
import com.goodjob.resume.usecase.SavePredictionUseCase;
import com.goodjob.resume.dto.request.PredictionServiceRequest;
import com.goodjob.resume.ports.in.SavePredictionPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PredictionFacade implements SavePredictionUseCase, FindPredictionUseCase {

    private final SavePredictionPort savePredictionPort;
    private final FindPredictionPort findPredictionPort;

    @Override
    @Transactional
    public void savePrediction(PredictionServiceRequest request) {
        Prediction prediction = request.toEntity();
        savePredictionPort.savePrediction(prediction);
    }

    @Override
    public ResponsePredictionDto getPredictionByMemberId(Long memberId) {
        Prediction prediction = findPredictionPort.findPredictionByMemberId(memberId).orElseThrow(() -> new BusinessException(ErrorCode.PREDICTION_NOT_FOUND));
        return ResponsePredictionDto.toDto(prediction);
    }

    @Override
    public ResponsePredictionDto getPredictionById(Long id) {
        Prediction prediction = findPredictionPort.findPredictionById(id).orElseThrow(() -> new BusinessException(ErrorCode.PREDICTION_NOT_FOUND));
        return ResponsePredictionDto.toDto(prediction);
    }

    @Override
    public List<ResponsePredictionDto> getPredictions(Long memberId) {
        List<Prediction> predictions = findPredictionPort.findPredictionsByMemberId(memberId);

        return predictions.stream()
                .map(ResponsePredictionDto::toDto)
                .collect(Collectors.toList());
    }
}
