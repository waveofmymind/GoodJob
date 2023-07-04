package com.goodjob.core.domain.mentoring.repository;

import com.goodjob.core.domain.article.entity.Article;
import com.goodjob.core.domain.mentoring.entity.Mentoring;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MentoringRepository extends JpaRepository<Mentoring, Long>, MentoringRepositoryCustom {
    @Query("SELECT m FROM Mentoring m ORDER BY m.id DESC")
    List<Mentoring> findAll();
}
