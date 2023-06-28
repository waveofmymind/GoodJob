package com.goodjob.core.domain.resume.ports.outs;

import com.goodjob.core.domain.resume.domain.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PredictionRepository extends JpaRepository<Prediction,Long> {
}
