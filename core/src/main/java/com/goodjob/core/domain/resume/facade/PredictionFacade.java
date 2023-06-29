package com.goodjob.core.domain.resume.facade;

import com.goodjob.core.domain.resume.domain.Prediction;
import com.goodjob.core.domain.resume.dto.response.ResponsePredictionDto;
import com.goodjob.core.domain.resume.ports.in.FindPredictionPort;
import com.goodjob.core.domain.resume.usecase.FindPredictionUseCase;
import com.goodjob.core.domain.resume.usecase.SavePredictionUseCase;
import com.goodjob.core.domain.resume.dto.request.PredictionServiceRequest;
import com.goodjob.core.domain.resume.ports.in.SavePredictionPort;
import com.goodjob.core.global.error.ErrorCode;
import com.goodjob.core.global.error.exception.BusinessException;
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
