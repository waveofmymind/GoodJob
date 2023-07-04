package com.goodjob.core.domain.article.repository;

import com.goodjob.core.domain.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleRepositoryCustom {

    @Query("SELECT a FROM Article a WHERE a.member.id= :memberId ORDER BY a.createdDate DESC")
    List<Article> findAllByMemberIdOrderByCreatedDateDesc(@Param("memberId") Long memberId);
}
