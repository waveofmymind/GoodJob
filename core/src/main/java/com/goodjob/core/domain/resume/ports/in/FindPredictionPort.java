package com.goodjob.core.domain.resume.ports.in;

import com.goodjob.core.domain.resume.domain.Prediction;

import java.util.Optional;

public interface FindPredictionPort {

    Optional<Prediction> findPredictionByMemberId(Long memberId);
}
