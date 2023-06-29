package com.goodjob.core.domain.resume.usecase;

import com.goodjob.core.domain.resume.dto.response.ResponsePredictionDto;

import java.util.List;

public interface FindPredictionUseCase {

    ResponsePredictionDto getPredictionByMemberId(Long memberId);

    ResponsePredictionDto getPredictionById(Long id);

    List<ResponsePredictionDto> getPredictions(Long memberId);
}
