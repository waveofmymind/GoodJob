package com.goodjob.article.domain.keyword.repository;


import com.goodjob.article.domain.keyword.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    Optional<Keyword> findByContent(String keywordContent);
}
