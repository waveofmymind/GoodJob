package com.goodjob.core.domain.mentoring.repository;

import com.goodjob.core.domain.article.entity.Article;
import com.goodjob.core.domain.mentoring.entity.Mentoring;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MentoringRepository extends JpaRepository<Mentoring, Long>{
}
