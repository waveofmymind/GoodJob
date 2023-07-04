package com.goodjob.resume.ports.outs;

import com.goodjob.resume.domain.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PredictionRepository extends JpaRepository<Prediction,Long> {

    Optional<Prediction> findByMemberId(Long member);

    @Query("SELECT p FROM Prediction p where p.memberId= :member ORDER BY p.createdDate DESC")
    List<Prediction> findAllByMemberId(@Param("member") Long member);
}
