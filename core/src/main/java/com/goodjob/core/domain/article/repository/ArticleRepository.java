package com.goodjob.core.domain.article.repository;

import com.goodjob.core.domain.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleRepositoryCustom {

    List<Article> findAllByMemberId(Long memberId);
}
