package com.goodjob.domain.article.service;

import com.goodjob.domain.article.dto.response.ArticleResponseDto;
import com.goodjob.domain.article.entity.Article;
import com.goodjob.domain.article.mapper.ArticleMapper;
import com.goodjob.domain.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;

    public List<ArticleResponseDto> getList() {
        List<Article> articles = this.articleRepository.findAll();

        return articles.stream()
                .map(articleMapper::toDto)
                .collect(Collectors.toList());
    }

    public ArticleResponseDto getArticle(Long id) {
        Article article = this.articleRepository.findById(id).orElseThrow();
        return articleMapper.toDto(article);
    }
}
