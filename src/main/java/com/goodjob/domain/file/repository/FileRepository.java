package com.goodjob.domain.file.repository;

import com.goodjob.domain.article.entity.Article;
import com.goodjob.domain.comment.entity.Comment;
import com.goodjob.domain.file.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepository  extends JpaRepository<File, Long> {
    List<File> findAllByArticle(Article article);

    List<File> findAllByArticleId(Long id);

    Optional<File> findByArticleAndFileNo(Article article, int fileNo);
}
