package com.goodjob.mentoring.domain.mentoring.repository;

import com.goodjob.mentoring.domain.mentoring.entity.Mentoring;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MentoringRepository extends JpaRepository<Mentoring, Long>, MentoringRepositoryCustom {
    @Query("SELECT m FROM Mentoring m ORDER BY m.id DESC")
    List<Mentoring> findAll();
}
