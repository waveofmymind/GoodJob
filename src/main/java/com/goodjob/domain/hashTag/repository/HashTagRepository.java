package com.goodjob.domain.hashTag.repository;

import com.goodjob.domain.article.entity.Article;
import com.goodjob.domain.hashTag.entity.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {
    Optional<HashTag> findByArticleIdAndKeywordId(Long articleId, Long keywordId);

    List<HashTag> findAllByArticleId(Long articleId);
}
