package com.goodjob.domain.article.repository;

import com.goodjob.domain.article.entity.Article;

import java.util.List;

public interface ArticleRepositoryCustom {
    List<Article> findQslBySortCode(int sortCode, String category, String kw);
}
