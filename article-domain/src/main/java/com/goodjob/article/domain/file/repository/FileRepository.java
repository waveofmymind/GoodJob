package com.goodjob.article.domain.file.repository;


import com.goodjob.article.domain.article.entity.Article;
import com.goodjob.article.domain.file.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepository  extends JpaRepository<File, Long> {
    List<File> findAllByArticle(Article article);

    List<File> findAllByArticleId(Long id);

    Optional<File> findByArticleAndFileNo(Article article, int fileNo);
}
