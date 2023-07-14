package com.goodjob.resume.facade;

import com.goodjob.common.error.ErrorCode;
import com.goodjob.common.error.exception.BusinessException;
import com.goodjob.resume.application.in.DeletePredictionUseCase;
import com.goodjob.resume.application.outs.DeletePredictionPort;
import com.goodjob.resume.domain.Prediction;
import com.goodjob.resume.dto.response.ResponsePredictionDto;
import com.goodjob.resume.application.outs.FindPredictionPort;
import com.goodjob.resume.application.in.FindPredictionUseCase;
import com.goodjob.resume.application.in.SavePredictionUseCase;
import com.goodjob.resume.dto.request.PredictionServiceRequest;
import com.goodjob.resume.application.outs.SavePredictionPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PredictionFacade implements SavePredictionUseCase, FindPredictionUseCase, DeletePredictionUseCase {

    private final SavePredictionPort savePredictionPort;
    private final FindPredictionPort findPredictionPort;
    private final DeletePredictionPort deletePredictionPort;

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

    @Override
    @Transactional
    public void deletePrediction(Long id) {
        deletePredictionPort.deletePrediction(id);
    }
}
