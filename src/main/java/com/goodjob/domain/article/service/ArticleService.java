package com.goodjob.domain.article.service;

import com.goodjob.domain.article.dto.response.ArticleResponseDto;
import com.goodjob.domain.article.entity.Article;
import com.goodjob.domain.article.mapper.ArticleMapper;
import com.goodjob.domain.article.repository.ArticleRepository;
import com.goodjob.global.base.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;

    @Transactional
    public List<ArticleResponseDto> getList() {
        List<Article> articles = articleRepository.findAll();

        return articles.stream()
                .map(articleMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<ArticleResponseDto> findAll() {
        List<Article> articles = articleRepository.findAll();

        return articles
                .stream()
                .map(articleMapper::toDto)
                .collect(Collectors.toList());
    }

    public ArticleResponseDto getArticleResponseDto(Long id) {
        return articleMapper.toDto(articleRepository.findById(id).orElseThrow());
    }

    public Article getArticle(Long id) {
        return articleRepository.findById(id).orElseThrow();
    }
}
